package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import android.database.sqlite.SQLiteStatement;
import android.database.Cursor;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;

import java.util.ArrayList;

/**
 * Created by User on 20/11/2016.
 */

public class PersistantAcountDAO implements AccountDAO {

    private SQLiteDatabase database;


    public PersistantAcountDAO(SQLiteDatabase db){
        this.database = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor resultList = database.rawQuery("SELECT Account_no FROM Account",null);
        List<String> accounts = new ArrayList<String>();
        if(resultList.moveToFirst()) {
            do {
                accounts.add(resultList.getString(resultList.getColumnIndex("Account_no")));
            } while (resultList.moveToNext());
        }
        //Return the list
        return accounts;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor resultList = database.rawQuery("SELECT * FROM Account",null);
        List<Account> accounts = new ArrayList<Account>();

        if(resultList.moveToFirst()) {
            do {
                Account account = new Account(resultList.getString(resultList.getColumnIndex("Account_no")),
                        resultList.getString(resultList.getColumnIndex("Bank")),
                        resultList.getString(resultList.getColumnIndex("Holder")),
                        resultList.getDouble(resultList.getColumnIndex("Initial_amt")));
                accounts.add(account);
            } while (resultList.moveToNext());
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = database.rawQuery("SELECT * FROM Account WHERE Account_no = " + accountNo,null);
        Account account = null;

        if(resultSet.moveToFirst()) {
            do {
                account = new Account(resultSet.getString(resultSet.getColumnIndex("Account_no")),
                        resultSet.getString(resultSet.getColumnIndex("Bank")),
                        resultSet.getString(resultSet.getColumnIndex("Holder")),
                        resultSet.getDouble(resultSet.getColumnIndex("Initial_amt")));
            } while (resultSet.moveToNext());
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {

        String sql = "INSERT INTO Account (Account_no,Bank,Holder,Initial_amt) VALUES (?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());

        statement.executeInsert();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql = "DELETE FROM Account WHERE Account_no = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE Account SET Initial_amt = Initial_amt + ?";
        SQLiteStatement statement = database.compileStatement(sql);
        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }

        statement.executeUpdateDelete();

    }
}
