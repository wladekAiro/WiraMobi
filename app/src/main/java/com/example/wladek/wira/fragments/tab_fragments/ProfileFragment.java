package com.example.wladek.wira.fragments.tab_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wladek.wira.R;
import com.example.wladek.wira.activity.BarCodeActivity;
import com.example.wladek.wira.activity.ClaimsActivity;
import com.google.android.gms.common.api.CommonStatusCodes;

/**
 * Created by wladek on 8/9/16.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_EXAMPLE = "This is an expense argument";
    private String example_data = "";
    final int QRCODE_REQUEST = 567;

    public ProfileFragment(){
    }

    public static ProfileFragment newInstance(String expenseArgument){

        ProfileFragment expenseFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXAMPLE , expenseArgument);

        return expenseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_layout , container , false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_qrCode) {
            Intent intent = new Intent(getActivity(), BarCodeActivity.class);
            startActivityForResult(intent, QRCODE_REQUEST);
            return true;
        }

        if (item.getItemId() == R.id.action_new){
            Intent intent = new Intent(getActivity(), ClaimsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == CommonStatusCodes.SUCCESS) {
            if (requestCode == QRCODE_REQUEST) {
                if (data != null) {
                    String barcode = data.getStringExtra("qrCode");
                    Toast.makeText(getActivity(), "Data : " + barcode, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "No data received from scanner", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}


