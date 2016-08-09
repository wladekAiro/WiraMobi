package com.example.wladek.wira.fragments.tab_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wladek.wira.R;

/**
 * Created by wladek on 8/9/16.
 */
public class ExpenseFragment extends Fragment {

    private static final String ARG_EXAMPLE = "Args";
    private String example_data = "";

    public ExpenseFragment(){

    }

    public static ExpenseFragment newInstance(String expenseArgument){

        ExpenseFragment expenseFragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXAMPLE , expenseArgument);

        return expenseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.expense_layout , container , false);
    }
}
