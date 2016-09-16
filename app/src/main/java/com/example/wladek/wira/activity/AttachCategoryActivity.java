package com.example.wladek.wira.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wladek.wira.R;
import com.example.wladek.wira.utils.ExpenseCategory;

import java.util.ArrayList;
import java.util.EnumSet;

public class AttachCategoryActivity extends AppCompatActivity {

    ArrayList<ExpenseCategory> expenseCategories =
            new ArrayList<ExpenseCategory>(EnumSet.allOf(ExpenseCategory.class));

    CustomListAdaptor customListAdaptor;
    ListView listViewCategories;
    static final int CATEGORY_RESULT_CODE = 146;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_category);

        listViewCategories = (ListView) findViewById(R.id.listViewCategories);

        customListAdaptor = new CustomListAdaptor(AttachCategoryActivity.this, expenseCategories);

        listViewCategories.setAdapter(customListAdaptor);


        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("claimCategory" , expenseCategories.get(position).name());
                setResult(CATEGORY_RESULT_CODE , intent);
                finish();
            }
        });
    }

    private class CustomListAdaptor extends BaseAdapter {

        private LayoutInflater layoutInflater;

        Context context;
        ArrayList<ExpenseCategory> categories = new ArrayList<>();
        ViewHolder viewHolder;

        public CustomListAdaptor(AttachCategoryActivity context, ArrayList<ExpenseCategory> expenseCategories) {
            layoutInflater = LayoutInflater.from(context);
            this.context = context;
            this.categories = expenseCategories;
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ExpenseCategory category = categories.get(position);

            if (convertView == null) {

                convertView = layoutInflater.inflate(R.layout.attach_claim_listview_row, null);

                viewHolder = new ViewHolder();
                viewHolder.claimTitle = (TextView) convertView.findViewById(R.id.textClaimTitle);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.claimTitle.setText(category.name());

            return convertView;
        }
    }

    static class ViewHolder {
        TextView claimTitle;
    }
}
