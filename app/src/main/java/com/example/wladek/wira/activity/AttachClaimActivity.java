package com.example.wladek.wira.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.ExpenseClaim;
import com.example.wladek.wira.utils.DatabaseHelper;

import java.util.ArrayList;

public class AttachClaimActivity extends AppCompatActivity {

    ListView listViewAttachClaims;
    DatabaseHelper dbHelper;

    ArrayList<ExpenseClaim> claims = new ArrayList<>();
    CustomListAdaptor customListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_claim);
        dbHelper = new DatabaseHelper(this);
        loadClaims();


        listViewAttachClaims = (ListView) findViewById(R.id.listViewAttachClaims);

        customListAdaptor = new CustomListAdaptor(AttachClaimActivity.this , claims);

        listViewAttachClaims.setAdapter(customListAdaptor);
    }

    private class CustomListAdaptor extends BaseAdapter {

        private LayoutInflater layoutInflater;

        Context context;
        ArrayList<ExpenseClaim> expenseClaims = new ArrayList<>();
        ViewHolder viewHolder;

        public CustomListAdaptor(Context context, ArrayList<ExpenseClaim> claims) {
            layoutInflater = LayoutInflater.from(context);
            this.context = context;
            this.expenseClaims = claims;
        }

        @Override
        public int getCount() {
            return expenseClaims.size();
        }

        @Override
        public Object getItem(int position) {
            return expenseClaims.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ExpenseClaim expenseClaim = expenseClaims.get(position);

            if (convertView == null){

                convertView = layoutInflater.inflate(R.layout.attach_claim_listview_row , null);

                viewHolder = new ViewHolder();
                viewHolder.claimTitle = (TextView) convertView.findViewById(R.id.textClaimTitle);
                convertView.setTag(viewHolder);

            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.claimTitle.setText(expenseClaim.getTitle());

            return convertView;
        }
    }

    static class ViewHolder{
        TextView claimTitle;
    }

    public void loadClaims() {
        claims.clear();
        claims.addAll(dbHelper.getClaims());
    }
}
