package com.example.wladek.wira.fragments.tab_fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.Item;
import com.kosalgeek.android.photoutil.CameraPhoto;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wladek on 8/9/16.
 */
public class ExpenseFragment extends Fragment {

    View myView;
    private static final String ARG_EXAMPLE = "Args";
    private String example_data = "";
    ListView listView;

    ArrayList<Item> expenseItems = new ArrayList<>();

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
        myView = inflater.inflate(R.layout.expense_layout , container , false);

        loadData();

        listView = (ListView) myView.findViewById(R.id.lstExpenses);

        Log.e("liST SIZE +++ " , " "+expenseItems.size());

        listView.setAdapter(new CustomListAdaptor(this.getActivity(), expenseItems));

        return myView;
    }

    public class CustomListAdaptor extends BaseAdapter{

        private LayoutInflater layoutInflater;
        private ArrayList<Item> itemsList = new ArrayList<Item>();

        ViewHolder viewHolder;

        public CustomListAdaptor(Context context, ArrayList<Item> expenseItems) {
            layoutInflater = LayoutInflater.from(context);
            this.itemsList = expenseItems;
        }

        @Override
        public int getCount() {
            return itemsList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Item lstItem = itemsList.get(position);

            if (convertView == null){

                convertView = layoutInflater.inflate(R.layout.list_view_item_layout , null);

                viewHolder = new ViewHolder();
                viewHolder.txtClaimTitle = (TextView) convertView.findViewById(R.id.txtClaimTitle);
                viewHolder.txtClaimAmount = (TextView) convertView.findViewById(R.id.txtClaimAmount);
                viewHolder.txtClaimCenter = (TextView) convertView.findViewById(R.id.txtClaimCenter);
                viewHolder.txtClaimDate = (TextView) convertView.findViewById(R.id.txtClaimDate);
                viewHolder.imgItemView = (ImageView) convertView.findViewById(R.id.imgItemView);

                convertView.setTag(viewHolder);

            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtClaimTitle.setText(lstItem.getClaimTitle());
            viewHolder.txtClaimAmount.setText(lstItem.getClaimAmount());
            viewHolder.txtClaimCenter.setText(lstItem.getClaimCenter());
            viewHolder.txtClaimDate.setText(lstItem.getClaimDate());

            viewHolder.imgItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showGalleryOptions();
                }
            });

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView imgItemView;
        TextView txtClaimTitle;
        TextView txtClaimCenter;
        TextView txtClaimDate;
        TextView txtClaimAmount;
    }

    private void showGalleryOptions() {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.select_gallery_layout);
        dialog.setTitle(" Upload from ... ");
        dialog.setCancelable(true);

        ImageButton imgBtnGallery = (ImageButton) dialog.findViewById(R.id.imgBtnGallery);
        ImageButton imgBtnCam = (ImageButton) dialog.findViewById(R.id.imgBtnCam);

        imgBtnCam.setOnClickListener(new CustomClickListener(getContext() , "imgBtnCam"));
        imgBtnGallery.setOnClickListener(new CustomClickListener(getContext() , "imgBtnGallery"));

        dialog.show();

    }

    private void loadData() {

        expenseItems.clear();

        Item item = new Item();
        item.setClaimTitle("Break fast");
        item.setClaimCenter("BBQ Central");
        item.setClaimDate("Aug 2, 2016");
        item.setClaimAmount("Ksh. 100");

        expenseItems.add(item);

        item = new Item();
        item.setClaimTitle("Lunch");
        item.setClaimCenter("Hilton Hotel");
        item.setClaimDate("Aug 2, 2016");
        item.setClaimAmount("Ksh. 2000");
        expenseItems.add(item);

        item = new Item();
        item.setClaimTitle("Suppper");
        item.setClaimCenter("Serena");
        item.setClaimDate("Aug 3, 2016");
        item.setClaimAmount("Ksh. 2000");
        expenseItems.add(item);

        item = new Item();
        item.setClaimTitle("Lunch");
        item.setClaimCenter("BBQ Central");
        item.setClaimDate("Aug 5, 2016");
        item.setClaimAmount("Ksh. 250");
        expenseItems.add(item);

        item = new Item();
        item.setClaimTitle("Lunch");
        item.setClaimCenter("BBQ Central");
        item.setClaimDate("Aug 7, 2016");
        item.setClaimAmount("Ksh. 250");
        expenseItems.add(item);
    }

    private class CustomClickListener implements View.OnClickListener {
        String btnName;
        Context context;
        CameraPhoto cameraPhoto;
        final int CAMERA_REQUEST = 123;
        public CustomClickListener(Context context, String imgBtnCam) {
            this.btnName = imgBtnCam;
            this.context = context;
            cameraPhoto = new CameraPhoto(context);
        }

        @Override
        public void onClick(View v) {
            if (btnName.equals("imgBtnCam")){
                try {

                    startActivityForResult(cameraPhoto.takePhotoIntent() , CAMERA_REQUEST);

                } catch (IOException e) {
                    Toast.makeText(context , "Something went wrong while taking a photo" ,
                            Toast.LENGTH_LONG).show();
                }

                Toast.makeText(context , "Get Camera" , Toast.LENGTH_SHORT).show();
            }else if (btnName.equals("imgBtnGallery")){

                Toast.makeText(context , "Get Gallery" , Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void launchCamera(Context context){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
