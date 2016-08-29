package com.example.wladek.wira.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_claim);

        dbHelper = new DatabaseHelper(getApplicationContext());

        expenseClaim = (ExpenseClaim) getIntent().getSerializableExtra("claim");

        actionBar = getSupportActionBar();

        if (expenseClaim != null){
            loadClaimExpenses(expenseClaim);
            actionBar.setTitle(expenseClaim.getTitle());
        }

        layoutExpenses = (LinearLayout) findViewById(R.id.layoutExpenses);
        layoutAttach = (LinearLayout) findViewById(R.id.layoutAttach);

        txtClaimTitle = (TextView) findViewById(R.id.txtClaimTitle);
        txtTotalClaim = (TextView) findViewById(R.id.txtTotalClaim);
        txtNoExpenses = (TextView) findViewById(R.id.txtNoExpenses);
        lstClaimExpenses = (ListView) findViewById(R.id.lstClaimExpenses);

        if (expenseClaim == null){
            expenseClaim = new ExpenseClaim();
        }else{
            txtClaimTitle.setText("Add expense");
        }

        if (customListAdaptor == null) {
            customListAdaptor = new CustomAdaptor(getApplicationContext(), claimExpenses);
        }else {
            customListAdaptor.notifyDataSetChanged();
        }

        lstClaimExpenses.setAdapter(customListAdaptor);

        Double total = new Double(0);

        if (claimExpenses.isEmpty()){
            layoutExpenses.setVisibility(View.INVISIBLE);
            txtNoExpenses.setVisibility(View.VISIBLE);
        }else {
            layoutExpenses.setVisibility(View.VISIBLE);
            txtNoExpenses.setVisibility(View.INVISIBLE);

            for (ExpenseItem i : claimExpenses){
                total = total+i.getExpenseAmount();
            }
        }

        txtTotalClaim.setText("Ksh. "+total);

        layoutAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachExpense();
            }
        });
    }

    private class CustomAdaptor extends BaseAdapter{

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

                convertView = layoutInflater.inflate(R.layout.claim_lst_expenses_custom_item , null);

                viewHolder.imgExpensePic = (ImageView) convertView.findViewById(R.id.imgExpensePic);
                viewHolder.txtExpenseTitle = (TextView) convertView.findViewById(R.id.txtExpenseTitle);
                viewHolder.txtExpenseAmount = (TextView) convertView.findViewById(R.id.txtExpenseAmount);
                viewHolder.btnAttachExpense = (Button) convertView.findViewById(R.id.btnAttachExpense);

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

            viewHolder.txtExpenseTitle.setText(expenseItem.getExpenseName());
            viewHolder.txtExpenseAmount.setText("Ksh. "+expenseItem.getExpenseAmount());

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView imgExpensePic;
        TextView txtExpenseTitle;
        TextView txtExpenseAmount;
        Button btnAttachExpense;
    }

    public void loadClaimExpenses(ExpenseClaim expenseClaim){
        claimExpenses.clear();
        claimExpenses.addAll(dbHelper.getClaimExpenses(expenseClaim));
    }

    public void attachExpense(){
        Intent intent = new Intent(getApplicationContext() , AttachExpenseActivity.class);
        intent.putExtra("claim", expenseClaim);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (customListAdaptor != null){
            customListAdaptor.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (customListAdaptor != null){
            customListAdaptor.notifyDataSetChanged();
        }
    }
}
