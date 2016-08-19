package com.example.wladek.wira.fragments.tab_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.ExpenseClaim;
import com.example.wladek.wira.pojo.ExpenseItem;
import com.example.wladek.wira.utils.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by wladek on 8/9/16.
 */
public class ClaimsFragment extends Fragment {

    private static final String ARG_EXAMPLE = "This is an expense argument";
    private String example_data="";

    public ClaimsFragment(){

    }

    ArrayList<ExpenseClaim> claims = new ArrayList<>();

    DatabaseHelper dbHelper;
    View myView;

    ListView lstClaims;

    CustomListAdaptor customListAdaptor;

    public static ClaimsFragment newInstance(String expenseArgument){

        ClaimsFragment expenseFragment = new ClaimsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXAMPLE , expenseArgument);

        return expenseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.claims_layout , container , false);

        lstClaims = (ListView) myView.findViewById(R.id.lstClaims);

        customListAdaptor = new CustomListAdaptor(this.getActivity() , claims);
        lstClaims.setAdapter(customListAdaptor);

        loadClaims();

        updateFragmentListView();

        return myView;
    }

    private class CustomListAdaptor extends BaseAdapter{

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
            return claims.size();
        }

        @Override
        public Object getItem(int position) {
            return claims.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ExpenseClaim expenseClaim = expenseClaims.get(position);

            if (convertView == null){

                convertView = layoutInflater.inflate(R.layout.claim_cutom_list_item , null);

                viewHolder = new ViewHolder();

                viewHolder.claimTitle = (TextView) convertView.findViewById(R.id.txtClaimTitle);
                viewHolder.txtClaimTotal = (TextView) convertView.findViewById(R.id.txtClaimTotal);

                convertView.setTag(viewHolder);

            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Double total = new Double(0);

            if (!expenseClaim.getExpenses().isEmpty()) {
                for (ExpenseItem i : expenseClaim.getExpenses()) {
                    total = total + i.getExpenseAmount();
                }
            }

            viewHolder.claimTitle.setText(expenseClaim.getTitle());
            viewHolder.txtClaimTotal.setText("Ksh "+total);

            return convertView;
        }
    }

    static class ViewHolder{
        TextView claimTitle;
        TextView txtClaimTotal;
    }

    public void loadClaims() {
        claims.clear();
        claims.addAll(dbHelper.getClaims());
    }

    public void updateFragmentListView() {
        if (customListAdaptor != null) {
            customListAdaptor.notifyDataSetChanged();
        }
    }
}
