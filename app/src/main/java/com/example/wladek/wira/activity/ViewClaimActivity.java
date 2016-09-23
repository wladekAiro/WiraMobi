package com.example.wladek.wira.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ViewClaimActivity extends AppCompatActivity {

    ExpenseClaim expenseClaim;

    TextView txtClaimTitle;
    ListView lstClaimExpenses;
    TextView txtNoExpenses;
    TextView txtTotalClaim;
    LinearLayout layoutExpenses;
    LinearLayout layoutAttach;

    ArrayList<ExpenseItem> claimExpenses = new ArrayList<>();

    CustomAdaptor customListAdaptor;

    DatabaseHelper dbHelper;
    ActionBar actionBar;
    Double total = new Double(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_claim);

        dbHelper = new DatabaseHelper(getApplicationContext());

        expenseClaim = (ExpenseClaim) getIntent().getSerializableExtra("claim");

        actionBar = getSupportActionBar();

        if (expenseClaim != null) {
            loadClaimExpenses(expenseClaim);
            actionBar.setTitle(expenseClaim.getTitle());
        }

        layoutExpenses = (LinearLayout) findViewById(R.id.layoutExpenses);
        layoutAttach = (LinearLayout) findViewById(R.id.layoutAttach);

        txtClaimTitle = (TextView) findViewById(R.id.txtClaimTitle);
        txtTotalClaim = (TextView) findViewById(R.id.txtTotalClaim);
        txtNoExpenses = (TextView) findViewById(R.id.txtNoExpenses);
        lstClaimExpenses = (ListView) findViewById(R.id.lstClaimExpenses);

        if (expenseClaim == null) {
            expenseClaim = new ExpenseClaim();
        } else {
            txtClaimTitle.setText("Add expense");
        }

        if (customListAdaptor == null) {
            customListAdaptor = new CustomAdaptor(getApplicationContext(), claimExpenses);
        } else {
            customListAdaptor.notifyDataSetChanged();
        }

        lstClaimExpenses.setAdapter(customListAdaptor);

        checkData();

        layoutAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachExpense();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shared_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.okIcon:
                onBackPressed();
                return true;
            default:
                return false;
        }
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

            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = layoutInflater.inflate(R.layout.claim_lst_expenses_custom_item, null);

                viewHolder.imgExpensePic = (ImageView) convertView.findViewById(R.id.imgExpensePic);
                viewHolder.txtExpenseTitle = (TextView) convertView.findViewById(R.id.txtExpenseTitle);
                viewHolder.txtExpenseAmount = (TextView) convertView.findViewById(R.id.txtExpenseAmount);
                viewHolder.btnRemoveExpense = (Button) convertView.findViewById(R.id.btnAttachExpense);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (expenseItem.getImagePath() != null) {

                mPicasso
                        .load(new File(expenseItem.getImagePath()))
                        .placeholder(R.mipmap.document_icon)
                        .error(R.mipmap.document_icon)
                        .resize(450, 400)
                        .into(viewHolder.imgExpensePic);
            }

            viewHolder.txtExpenseTitle.setText(expenseItem.getExpenseName());
            viewHolder.txtExpenseAmount.setText("Ksh. " + expenseItem.getExpenseAmount());

//            viewHolder.btnRemoveExpense.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    removeExpenseFromClaim(expenseItem);
//                }
//            });

            return convertView;
        }
    }

    private void removeExpenseFromClaim(ExpenseItem expenseItem) {
        String response = dbHelper.removeExpenseFromClaim(expenseItem);
        loadClaimExpenses(expenseClaim);
        checkData();
        customListAdaptor.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
    }

    static class ViewHolder {
        ImageView imgExpensePic;
        TextView txtExpenseTitle;
        TextView txtExpenseAmount;
        Button btnRemoveExpense;
    }

    public void loadClaimExpenses(ExpenseClaim expenseClaim) {
        claimExpenses.clear();
        claimExpenses.addAll(dbHelper.getClaimExpenses(expenseClaim));
    }

    public void attachExpense() {
        Intent intent = new Intent(getApplicationContext(), AttachExpenseActivity.class);
        intent.putExtra("claim", expenseClaim);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {

            loadClaimExpenses(expenseClaim);

            checkData();

            customListAdaptor.notifyDataSetChanged();
        }
    }

    private void checkData() {

        if (claimExpenses.isEmpty()) {
            layoutExpenses.setVisibility(View.INVISIBLE);
            txtNoExpenses.setVisibility(View.VISIBLE);

            customListAdaptor.notifyDataSetInvalidated();

        } else {
            layoutExpenses.setVisibility(View.VISIBLE);
            txtNoExpenses.setVisibility(View.INVISIBLE);

            total = new Double(0);

            for (ExpenseItem i : claimExpenses) {
                total = total + i.getExpenseAmount();
            }

            txtTotalClaim.setText("Ksh. " + total);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (customListAdaptor != null) {
            customListAdaptor.notifyDataSetChanged();
        }
    }
}
