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

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSE_CLAIMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, CLAIM_TITLE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE_CLAIMS);
        onCreate(db);
    }

    public ArrayList<ExpenseItem> getExpenseItems() {
        ArrayList<ExpenseItem> expenseItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " ORDER BY ID DESC", null);

        if (res.getCount() > 0) {
            while (res.moveToNext()) {

                ExpenseItem expenseItem = new ExpenseItem();

                expenseItem.setId(new Long(res.getInt(0)));
                expenseItem.setExpenseName(res.getString(1));
                expenseItem.setExpenseDate(res.getString(2));
                expenseItem.setImagePath(res.getString(3));
                expenseItem.setExpenseAmount(res.getDouble(4));
                expenseItem.setClaimId(new Long(res.getInt(5)));

                expenseItems.add(expenseItem);
            }
        }

        if (!expenseItems.isEmpty()){
            for (ExpenseItem item : expenseItems){

                res = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE_CLAIMS + " WHERE ID =?",
                        new String[]{item.getClaimId() + ""});

                if(res.getCount() > 0){
                    while (res.moveToNext()){

                        ExpenseClaim expenseClaim = new ExpenseClaim();
                        expenseClaim.setId(new Long(res.getInt(0)));
                        expenseClaim.setTitle(res.getString(1));

                        item.setClaim(expenseClaim);
                    }
                }

            }
        }

        db.close();

        return expenseItems;
    }

    public ExpenseItem save(ExpenseItem expenseItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        String pic_url = expenseItem.getImagePath();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE EXPENSE_PHOTO_URL =?",
                new String[]{pic_url});

        if (cursor.getCount() == 0) {
            //Create new record
            ContentValues contentValues = new ContentValues();
            contentValues.put("EXPENSE_NAME", expenseItem.getExpenseName());
            contentValues.put("EXPENSE_DATE", expenseItem.getExpenseDate());
            contentValues.put("EXPENSE_PHOTO_URL", expenseItem.getImagePath());
            contentValues.put("EXPENSE_AMOUNT", expenseItem.getExpenseAmount());

            if (expenseItem.getClaimId() != null) {
                contentValues.put("CLAIM_ID", expenseItem.getClaimId());
            }

            Long result = db.insert(TABLE_EXPENSES, null, contentValues);

            if (result == -1) {

                return null;

            }
        } else {
            //Update record
            ContentValues contentValues = new ContentValues();
            contentValues.put("EXPENSE_NAME", expenseItem.getExpenseName());
            contentValues.put("EXPENSE_DATE", expenseItem.getExpenseDate());
            contentValues.put("EXPENSE_PHOTO_URL", expenseItem.getImagePath());
            contentValues.put("EXPENSE_AMOUNT", expenseItem.getExpenseAmount());

            if (expenseItem.getClaimId() != null) {
                contentValues.put("CLAIM_ID", expenseItem.getClaimId());
            }

            db.update(TABLE_EXPENSES, contentValues, "EXPENSE_PHOTO_URL='" + pic_url + "'", null);

        }

        db.close();

        return expenseItem;
    }

    public String createClaim(ExpenseClaim expenseClaim) {
        SQLiteDatabase db = this.getWritableDatabase();

        String response = " Failed ";

        if (expenseClaim.getId() == null) {
            //Insert
            ContentValues contentValues = new ContentValues();
            contentValues.put("CLAIM_TITLE", expenseClaim.getTitle());
            Long result = db.insert(TABLE_EXPENSE_CLAIMS, null, contentValues);

            if (result == -1) {

                return "Expense claim not saved";

            } else {
                response = "Saved";
            }
        } else {
            //Update
            ContentValues contentValues = new ContentValues();
            contentValues.put("CLAIM_TITLE", expenseClaim.getTitle());
            try {
                int result = db.update(TABLE_EXPENSE_CLAIMS, contentValues, "ID='" + expenseClaim.getId() + "'", null);

                if (result == -1) {

                    return "Expense claim not updated";

                } else {
                    response = "Updated";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE_CLAIMS + " ORDER BY ID DESC LIMIT 1",
                null);

        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                expenseClaim.setId(new Long(res.getInt(0)));
                expenseClaim.setTitle(res.getString(1));
            }
        }

        db.close();

        if (!expenseClaim.getExpenses().isEmpty() && expenseClaim.getExpenses().size() > 0) {

            response = attachExpensesToClaim(expenseClaim.getExpenses(), expenseClaim.getId());

        }

        return response;
    }

    private String attachExpensesToClaim(ArrayList<ExpenseItem> expenseItems, Long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String response = null;

        ContentValues contentValues = new ContentValues();
        contentValues.put("CLAIM_ID", 0);

        db.update(TABLE_EXPENSES, contentValues, "CLAIM_ID='" + id + "'", null);

        for (ExpenseItem i : expenseItems) {
            contentValues = new ContentValues();
            contentValues.put("CLAIM_ID", id);

            int result = db.update(TABLE_EXPENSES, contentValues, "EXPENSE_PHOTO_URL='" + i.getImagePath() + "'", null);

            if (result > 0) {
                response = "Success";
            } else {
                return "Failed to set claim on expense";
            }
        }

        return response;
    }

    public String attachExpenseToClaim(ExpenseItem expenseItem, Long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String response;

        ContentValues contentValues = new ContentValues();
        contentValues.put("CLAIM_ID", id);

        int result = db.update(TABLE_EXPENSES, contentValues, "EXPENSE_PHOTO_URL='" + expenseItem.getImagePath() + "'", null);


        if (result > 0) {
            response = "Attached";
        } else {
            return "Failed to attach expense";
        }

        return response;
    }

    public String removeExpenseFromClaim(ExpenseItem expenseItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        String response;

        ContentValues contentValues = new ContentValues();
        contentValues.put("CLAIM_ID", 0);

        int result = db.update(TABLE_EXPENSES, contentValues, "EXPENSE_PHOTO_URL='" + expenseItem.getImagePath() + "'", null);


        if (result > 0) {
            response = "Detached";
        } else {
            return "Failed to detach expense";
        }

        return response;
    }

    public ArrayList<ExpenseClaim> getClaims() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ExpenseClaim> claims = new ArrayList<>();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE_CLAIMS + " ORDER BY ID DESC", null);

        if (res.getCount() > 0) {
            while (res.moveToNext()) {

                ExpenseClaim expenseClaim = new ExpenseClaim();
                expenseClaim.setId(new Long(res.getInt(0)));
                expenseClaim.setTitle(res.getString(1));

                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE CLAIM_ID =?",
                        new String[]{expenseClaim.getId() + ""});

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {

                        ExpenseItem expenseItem = new ExpenseItem();

                        expenseItem.setId(new Long(cursor.getInt(0)));
                        expenseItem.setExpenseName(cursor.getString(1));
                        expenseItem.setExpenseDate(cursor.getString(2));
                        expenseItem.setImagePath(cursor.getString(3));
                        expenseItem.setExpenseAmount(cursor.getDouble(4));
                        expenseItem.setClaimId(new Long(cursor.getInt(5)));

                        expenseClaim.getExpenses().add(expenseItem);
                    }
                }

                claims.add(expenseClaim);
            }
        }

        db.close();

        return claims;
    }

    public ArrayList<ExpenseItem> getClaimExpenses(ExpenseClaim expenseClaim) {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ExpenseItem> expenseItems = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE CLAIM_ID =?",
                new String[]{expenseClaim.getId() + ""});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                ExpenseItem expenseItem = new ExpenseItem();

                expenseItem.setId(new Long(cursor.getInt(0)));
                expenseItem.setExpenseName(cursor.getString(1));
                expenseItem.setExpenseDate(cursor.getString(2));
                expenseItem.setImagePath(cursor.getString(3));
                expenseItem.setExpenseAmount(cursor.getDouble(4));
                expenseItem.setClaimId(new Long(cursor.getInt(5)));

                expenseItems.add(expenseItem);
            }
        }

        db.close();

        return expenseItems;
    }
}
