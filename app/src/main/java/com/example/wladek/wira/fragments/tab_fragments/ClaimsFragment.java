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
public class ClaimsFragment extends Fragment {

    private static final String ARG_EXAMPLE = "This is an expense argument";
    private String example_data="";

    public ClaimsFragment(){

    }

    public static ClaimsFragment newInstance(String expenseArgument){

        ClaimsFragment expenseFragment = new ClaimsFragment();
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

        return inflater.inflate(R.layout.claims_layout , container , false);
    }
}
