package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import android.database.sqlite.SQLiteStatement;
import android.database.Cursor;

import java.util.ArrayList;
/**
 * Created by User on 20/11/2016.
 */

public class PersistantTransactionDAO implements TransactionDAO {
    private SQLiteDatabase myDatabase;

    public PersistantTransactionDAO(SQLiteDatabase db){
        myDatabase=db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteStatement statement = myDatabase.compileStatement("INSERT INTO TransactionLog (Account_no,Type,Amt,Log_date) VALUES (?,?,?,?)");

        statement.bindString(1,accountNo);
        statement.bindLong(2,(expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(3,amount);
        statement.bindLong(4,date.getTime());

        statement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor resultList = myDatabase.rawQuery("SELECT * FROM TransactionLog",null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(resultList.moveToFirst()) {
            do{
                Transaction myTransaction = new Transaction(new Date(resultList.getLong(resultList.getColumnIndex("Log_date"))),
                        resultList.getString(resultList.getColumnIndex("Account_no")),
                        (resultList.getInt(resultList.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        resultList.getDouble(resultList.getColumnIndex("Amt")));
                transactions.add(myTransaction);
            }while (resultList.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor resultSet = myDatabase.rawQuery("SELECT * FROM TransactionLog LIMIT " + limit,null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(resultSet.moveToFirst()) {
            do {
                Transaction t = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Log_date"))),
                        resultSet.getString(resultSet.getColumnIndex("Account_no")),
                        (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        resultSet.getDouble(resultSet.getColumnIndex("Amt")));
                transactions.add(t);
            } while (resultSet.moveToNext());
        }

        return transactions;
    }
}
