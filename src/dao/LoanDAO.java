package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import models.Loan;
import exceptions.NotFoundException;

public interface LoanDAO{
	public int getLastInsertedRowID(Connection conn) throws SQLException;
    public Loan createValueObject();
    public Loan getObject(Connection conn, int account_id) throws NotFoundException, SQLException;
    public void load(Connection conn, Loan valueObject) throws NotFoundException, SQLException;
    public List loadAll(Connection conn) throws SQLException;
    public void create(Connection conn, Loan valueObject) throws SQLException;
    public void save(Connection conn, Loan valueObject) throws NotFoundException, SQLException;
    public void delete(Connection conn, Loan valueObject) throws NotFoundException, SQLException;
    public void deleteAll(Connection conn) throws SQLException;
    public int countAll(Connection conn) throws SQLException;
    public List searchMatching(Connection conn, Loan valueObject) throws SQLException;
    public int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException;
    public void singleQuery(Connection conn, PreparedStatement stmt, Loan valueObject) throws NotFoundException, SQLException;
    public List listQuery(Connection conn, PreparedStatement stmt) throws SQLException;
}
