package com.example.wladek.wira.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wladek.wira.pojo.ExpenseClaim;
import com.example.wladek.wira.pojo.ExpenseItem;

import java.util.ArrayList;

/**
 * Created by wladek on 8/15/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WECMS.db";
    public static final String TABLE_EXPENSES = "tbl_expenses";
    public static final String TABLE_EXPENSE_CLAIMS = "tbl_expense_claims";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, EXPENSE_NAME TEXT, " +
                " EXPENSE_DATE, EXPENSE_PHOTO_URL TEXT, EXPENSE_AMOUNT DOUBLE ," +
                " CLAIM_ID INTEGER , FOREIGN KEY(CLAIM_ID) REFERENCES tbl_expense_claims(ID))");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_CLAIMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, CLAIM_TITLE TEXT, " +
                "CLAIM_DESCRIPTION TEXT, CLAIM_TOTAL_AMOUNT DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE_CLAIMS);
        onCreate(db);
    }

    public ArrayList<ExpenseItem> getExpenseItems(){
        ArrayList<ExpenseItem> expenseItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " ORDER BY ID DESC", null);

        if (res.getCount() > 0){
            while (res.moveToNext()) {

                ExpenseItem expenseItem = new ExpenseItem();

                expenseItem.setId(new Long(res.getInt(0)));
                expenseItem.setExpenseName(res.getString(1));
                expenseItem.setExpenseDate(res.getString(2));
                expenseItem.setImagePath(res.getString(3));
                expenseItem.setExpenseAmount(res.getDouble(4));

                expenseItems.add(expenseItem);
            }
        }

        db.close();

        return expenseItems;
    }

    public ExpenseItem save(ExpenseItem expenseItem){
        SQLiteDatabase db = this.getWritableDatabase();

        String pic_url = expenseItem.getImagePath();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE EXPENSE_PHOTO_URL =?",
                new String[]{pic_url});

        if(cursor.getCount() == 0){
            //Create new record
            ContentValues contentValues = new ContentValues();
            contentValues.put("EXPENSE_NAME", expenseItem.getExpenseName());
            contentValues.put("EXPENSE_DATE", expenseItem.getExpenseDate());
            contentValues.put("EXPENSE_PHOTO_URL", expenseItem.getImagePath());
            contentValues.put("EXPENSE_AMOUNT", expenseItem.getExpenseAmount());

            Long result = db.insert(TABLE_EXPENSES, null, contentValues);

            if (result == -1) {

                return null;

            }
        }else {
            //Update record
            ContentValues contentValues = new ContentValues();
            contentValues.put("EXPENSE_NAME", expenseItem.getExpenseName());
            contentValues.put("EXPENSE_DATE", expenseItem.getExpenseDate());
            contentValues.put("EXPENSE_PHOTO_URL", expenseItem.getImagePath());
            contentValues.put("EXPENSE_AMOUNT", expenseItem.getExpenseAmount());

            db.update(TABLE_EXPENSES, contentValues, "EXPENSE_PHOTO_URL='" + pic_url + "'", null);

        }

        db.close();

        return expenseItem;
    }

    public ExpenseClaim createClaim(ExpenseClaim expenseClaim){
        SQLiteDatabase db = this.getWritableDatabase();


        if(expenseClaim.getId() == null){
            //Insert
            ContentValues contentValues = new ContentValues();
            contentValues.put("CLAIM_TITLE",  expenseClaim.getTitle());
            contentValues.put("CLAIM_DESCRIPTION", expenseClaim.getDescription());
            contentValues.put("CLAIM_TOTAL_AMOUNT", expenseClaim.getTotalAmount());

            Long result = db.insert(TABLE_EXPENSE_CLAIMS, null, contentValues);

            if (result == -1) {

                return null;

            }
        }else {
            //Update
            ContentValues contentValues = new ContentValues();
            contentValues.put("CLAIM_TITLE",  expenseClaim.getTitle());
            contentValues.put("CLAIM_DESCRIPTION", expenseClaim.getDescription());
            contentValues.put("CLAIM_TOTAL_AMOUNT", expenseClaim.getTotalAmount());

            Long result = db.insert(TABLE_EXPENSE_CLAIMS, null, contentValues);

            db.update(TABLE_EXPENSE_CLAIMS, contentValues, "ID='" + expenseClaim.getId() , null);

            if (result == -1) {

                return null;

            }

        }

        db.close();

        return expenseClaim;
    }

    private String attachExpenseToClaim(ExpenseClaim expenseClaim){
        String response = null;



        return null;
    }

    public ArrayList<ExpenseClaim> getClaims(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ExpenseClaim> claims = new ArrayList<>();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE_CLAIMS + " ORDER BY ID DESC", null);

        if (res.getCount() > 0){
            while (res.moveToNext()) {

                ExpenseClaim expenseClaim = new ExpenseClaim();
                expenseClaim.setId(new Long(res.getInt(0)));
                expenseClaim.setTitle(res.getString(1));
                expenseClaim.setDescription(res.getString(2));
                expenseClaim.setTotalAmount(res.getDouble(3));

                claims.add(expenseClaim);
            }
        }

        db.close();

        return claims;
    }
}
