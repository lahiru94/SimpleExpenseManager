package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAcountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;

/**
 * Created by User on 21/11/2016.
 */

public class PersistantExpenseManager extends ExpenseManager {

    private Context cntxt;
    public PersistantExpenseManager(Context ctx){
        this.cntxt = ctx;
        setup();
    }

    @Override
    public void setup() {
        SQLiteDatabase mydatabase = cntxt.openOrCreateDatabase("140098R", cntxt.MODE_PRIVATE, null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "Account_no VARCHAR PRIMARY KEY," +
                "Bank VARCHAR," +
                "Holder VARCHAR," +
                "Initial_amt REAL" +
                " );");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "Account_no VARCHAR," +
                "Type INT," +
                "Amt REAL," +
                "Log_date DATE," +
                "FOREIGN KEY (Account_no) REFERENCES Account(Account_no)" +
                ");");


        PersistantAcountDAO accountDAO = new PersistantAcountDAO(mydatabase);

        setAccountsDAO(accountDAO);

        setTransactionsDAO(new PersistantTransactionDAO(mydatabase));
    }
}
