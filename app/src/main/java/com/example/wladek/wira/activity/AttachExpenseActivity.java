package com.example.wladek.wira.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.ExpenseClaim;
import com.example.wladek.wira.pojo.ExpenseItem;
import com.example.wladek.wira.utils.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class AttachExpenseActivity extends AppCompatActivity {

    ExpenseClaim expenseClaim;
    DatabaseHelper databaseHelper;
    ActionBar actionBar;

    ArrayList<ExpenseItem> expenses = new ArrayList<>();

    ListView lstExpenses;

    CustomAdaptor customAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_expense);

        databaseHelper = new DatabaseHelper(this);
        loadExpenses();

        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        expenseClaim = (ExpenseClaim) getIntent().getSerializableExtra("claim");

        if (expenseClaim != null){
            actionBar.setTitle(expenseClaim.getTitle());

        }

        lstExpenses = (ListView) findViewById(R.id.expensesListView);
        customAdaptor = new CustomAdaptor(this , expenses);
        lstExpenses.setAdapter(customAdaptor);
    }

    private class CustomAdaptor extends BaseAdapter {

        private Context context;
        private ArrayList<ExpenseItem> items = new ArrayList<>();
        ViewHolder viewHolder;
        LayoutInflater layoutInflater;
        Picasso mPicasso;

        public CustomAdaptor(Context context, ArrayList<ExpenseItem> expenseItems) {
            layoutInflater = LayoutInflater.from(context);
            this.context = context;
            this.items = expenseItems;
            this.mPicasso = Picasso.with(context);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ExpenseItem expenseItem = items.get(position);

            if (convertView == null){
                viewHolder = new ViewHolder();

                convertView = layoutInflater.inflate(R.layout.claim_lst_expenses_to_attach_custom_item , null);

                viewHolder.imgExpensePic = (ImageView) convertView.findViewById(R.id.imgExpensePic);
                viewHolder.txtExpenseTitle = (TextView) convertView.findViewById(R.id.txtExpenseTitle);
                viewHolder.txtExpenseAmount = (TextView) convertView.findViewById(R.id.txtExpenseAmount);
                viewHolder.checkBoxAttach = (CheckBox) convertView.findViewById(R.id.checkBoxAttach);

                convertView.setTag(viewHolder);

            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (expenseItem.getImagePath() != null) {

                mPicasso
                        .load(new File(expenseItem.getImagePath()))
                        .placeholder(R.mipmap.doument_text_icon)
                        .error(R.mipmap.doument_text_icon)
                        .resize(150, 100)
                        .into(viewHolder.imgExpensePic);
            }

            if (expenseItem.getClaimId() != null){
                if (expenseClaim.getId().equals(expenseItem.getClaimId())){
                    viewHolder.checkBoxAttach.setChecked(true);
                }
            }

            viewHolder.txtExpenseTitle.setText(expenseItem.getExpenseName());
            viewHolder.txtExpenseAmount.setText("Ksh. " + expenseItem.getExpenseAmount());

            viewHolder.checkBoxAttach.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        attachExpense(expenseItem, context);
                    } else {
                        removeExpense(expenseItem, context);
                    }
                }
            });

            return convertView;
        }
    }

    private void removeExpense(ExpenseItem expenseItem , Context context) {
        String response = databaseHelper.removeExpenseFromClaim(expenseItem);
        Toast.makeText(context , response , Toast.LENGTH_SHORT).show();
        setResult(1);
    }

    private void attachExpense(ExpenseItem expenseItem , Context context) {
        String response = databaseHelper.attachExpenseToClaim(expenseItem, expenseClaim.getId());
        Toast.makeText(context , response , Toast.LENGTH_SHORT).show();
        setResult(1);
    }

    static class ViewHolder{
        ImageView imgExpensePic;
        TextView txtExpenseTitle;
        TextView txtExpenseAmount;
        CheckBox checkBoxAttach;
    }

    private void loadExpenses() {
        expenses.clear();
        expenses.addAll(databaseHelper.getExpenseItems());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses();
    }
}
