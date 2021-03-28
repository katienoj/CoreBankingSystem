package business_controllers;

import helpers.DBConnectionHelper;
import helpers.PermissionHelper;

import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.ChangesStatus;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dao.LoanDAO;
import dao.LoanDAOImpl;
import dao.BankBranchDAO;
import dao.BankBranchDAOImpl;
import dao.CustomerDAO;
import dao.CustomerDAOImpl;
import dao.DAOFactory;
import dao.MapAccountCustomerDAO;
import dao.MapAccountCustomerDAOImpl;
import dao.TransactionDAOImpl;
import exceptions.BelowMinimumBalanceException;
import exceptions.NotFoundException;
import exceptions.OverDraftLimitExceededException;


import models.Account;
import models.Loan;
import models.MapAccountCustomer;
import models.Transaction;

public class LoanController {

	public LoanController() {
	}

	public Loan getAccount(int account_id) throws NotFoundException {

		Loan loan = new Loan();
		Connection connection = DBConnectionHelper.getConnection();
		LoanDAO loanDAO = DAOFactory.getLoanDAO();

		try {
			// Load the Account Object
			loan = loanDAO.getObject(connection, account_id);

			return loan;
		} catch (NotFoundException e) {
			e.printStackTrace();
			throw new NotFoundException("Loan not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public Loan getLoanDetails(int account_id) throws NotFoundException {

		Loan loan = getAccount(account_id);

		Connection connection = DBConnectionHelper.getConnection();

		// Finding the owner
		MapAccountCustomerDAO mapAccountCustomerDAO = DAOFactory.getMapAccountCustomerDAO();
		MapAccountCustomer mapAccountCustomer = new MapAccountCustomer();
		mapAccountCustomer.setAccount_id(loan.getAccount_id());
		List<MapAccountCustomer> relation;
		try {
			relation = mapAccountCustomerDAO.searchMatching(connection,
					mapAccountCustomer);

			// Adding the owner(s)
			CustomerDAO customerDAOImpl = DAOFactory.getCustomerDAO();
			for (MapAccountCustomer r : relation) {
				loan.addCustomer(customerDAOImpl.getObject(connection,
						r.getCustomer_id()));
			}

			// if there is more than one account it is joint account
			if (relation.size() > 1) {
				loan.setJoint_account(true);
			} else {
				loan.setJoint_account(false);
			}

			// Loading the Bank branch
			BankBranchDAO bankBranchDAO = DAOFactory.getBankBranchDAO();
			loan.setBank_branch(bankBranchDAO.getObject(connection,
					loan.getBank_branch_id()));

			return loan;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public ChangesStatus depositMoney(int account_id, int customer_id_by,
			double amount, int pin) {

		Connection connection = DBConnectionHelper.getConnection();

		LoanDAO loanDAO = DAOFactory.getLoanDAO();
		try {
			Loan loan = loanDAO.getObject(connection, account_id);

			// Checking Security Pin
			if (!(loan.getPin() == pin)) {
				return new ChangesStatus(false,
						"Security Pin do not match! Transaction Canceled.");
			}

			// Checking Permission
			if (!(PermissionHelper.isThisAccountOwnByThisCustomer(account_id,
					customer_id_by))) {
				return new ChangesStatus(false,
						"You do not own this account. Transaction Canceled.");
			}

			double newAmount = loan.getAmount() + amount;
			loan.setAmount(newAmount);

			loanDAO.save(connection, loan);

			Transaction transaction = new Transaction();
			transaction.setCustomer_id_by(customer_id_by);
			transaction.setAccount_id(account_id);
			transaction.setTransaction_amount(amount);
			transaction.setTransaction_type(1);

			Date date = new Date();
			transaction.setTransaction_time(new Timestamp(date.getTime()));
			TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
			transactionDAOImpl.create(connection, transaction);
			// If exception found roll back in catch block
			connection.commit();

			return new ChangesStatus(true, "Successfully Deposited " + amount);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				connection.rollback();
				e.printStackTrace();
				return new ChangesStatus(false,
						"Transaction Rolledbacked. Unsuccessful.");
			} catch (SQLException e1) {
				e1.printStackTrace();
				return new ChangesStatus(false, "Unsuccessful Rolledback.");
			}
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ChangesStatus withdrawMoney(int account_id, int customer_id_by,
			int amount, int pin) throws BelowMinimumBalanceException,
			OverDraftLimitExceededException {

		Connection connection = DBConnectionHelper.getConnection();
		LoanDAO loanDAO = DAOFactory.getLoanDAO();
		Loan loan = new Loan();

		try {
			loan = loanDAO.getObject(connection, account_id);
			if (loan.getAccount_type() == 1) {
				CheckingAccountController checkingAccountController = new CheckingAccountController();
				return checkingAccountController.withdrawMoney(account_id,
						customer_id_by, amount, pin);

			} else {
				SavingsAccountController savingAccountController = new SavingsAccountController();
				return savingAccountController.withdrawMoney(account_id,
						customer_id_by, amount, pin);
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ChangesStatus transferMoney(int account_id_from, int account_id_to,
			int customer_id_by, int amount, int pin)
			throws OverDraftLimitExceededException,
			BelowMinimumBalanceException {

		Connection connection = DBConnectionHelper.getConnection();
		LoanDAO loanDAO = DAOFactory.getLoanDAO();
		Loan loan = new Loan();

		try {
			loan = loanDAO.getObject(connection, account_id_from);

			// Checking Security Pin
			if (!(loan.getPin() == pin)) {
				return new ChangesStatus(false,
						"Security Pin do not match! Transaction Canceled.");
			}
			// Checking Permission
			if (!(PermissionHelper.isThisAccountOwnByThisCustomer(
					account_id_from, customer_id_by))) {
				return new ChangesStatus(false,
						"You do not own this account. Transaction Canceled.");
			}

			if (loan.getAccount_type() == 1) {
				CheckingAccountController checkingAccountController = new CheckingAccountController();
				return checkingAccountController.transferMoney(account_id_from,
						account_id_to, customer_id_by, amount);
			} else {
				SavingsAccountController savingAccountController = new SavingsAccountController();
				return savingAccountController.transferMoney(account_id_from,
						account_id_to, customer_id_by, amount);
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public ChangesStatus saveAccount(Loan loan,
			ArrayList<MapAccountCustomer> mapAccountCustomer) {
		Connection connection = DBConnectionHelper.getConnection();

		LoanDAO loanDAO = DAOFactory.getLoanDAO();
		MapAccountCustomerDAO mapAccountCustomerDAO = DAOFactory.getMapAccountCustomerDAO();

		try {
			if (loan.getAccount_id() > 0) {
				Loan _loan = loanDAO.getObject(connection,
						loan.getAccount_id());
				loan.setAccount_type(_loan.getAccount_type());
			}

			if (loan.getAccount_id() > 0) {// update
				loanDAO.save(connection, loan);
			} else { // insert
				loanDAO.create(connection, loan);
				loan.setAccount_id(loanDAO
						.getLastInsertedRowID(connection));
			}

			// Getting relations with account_id
			MapAccountCustomer mapAccountCustomerToDelete = new MapAccountCustomer();
			mapAccountCustomerToDelete.setAccount_id(loan.getAccount_id());
			// loding map list to delete
			List<MapAccountCustomer> mapAccountCustomerListToDelete = mapAccountCustomerDAO
					.searchMatching(connection, mapAccountCustomerToDelete);
			// delteing
			if (mapAccountCustomerListToDelete.size() > 0) {
				for (MapAccountCustomer m : mapAccountCustomerListToDelete) {
					mapAccountCustomerDAO.delete(connection, m);
				}
			}

			// Inserting new relations
			for (MapAccountCustomer m : mapAccountCustomer) {
				MapAccountCustomer _m = new MapAccountCustomer();
				_m.setAccount_id(loan.getAccount_id());
				_m.setCustomer_id(m.getCustomer_id());
				mapAccountCustomerDAO.create(connection, _m);
			}

			connection.commit();

			return new ChangesStatus(true, "Successfully updated.");
		} catch (NotFoundException e) {
			e.printStackTrace();
			return new ChangesStatus(false, "No Account ID found");
		} catch (SQLException e) {
			e.printStackTrace();
			return new ChangesStatus(false, "SQL Error. Can not be save.");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ChangesStatus deleteAccount(int account_id) {

		return null;
	}

}
