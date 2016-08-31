package com.example.wladek.wira.fragments.tab_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wladek.wira.R;
import com.example.wladek.wira.activity.ExpenseActivity;
import com.example.wladek.wira.pojo.ExpenseItem;
import com.example.wladek.wira.utils.DatabaseHelper;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wladek on 8/9/16.
 */
public class ExpenseFragment extends Fragment {

    private final String TAG = this.getClass().getName();

    View myView;
    private static final String ARG_EXAMPLE = "Args";
    private String example_data = "";
    ListView listView;

    CameraPhoto cameraPhoto;
    final int CAMERA_REQUEST = 123;
    final int RESULT_OK = -1;

    String photoPath = "";

    ArrayList<ExpenseItem> expenseExpenseItems = new ArrayList<>();

    CustomListAdaptor customListAdaptor;

    int itemPosition;

    public ExpenseFragment() {

    }

    public static ExpenseFragment newInstance(String expenseArgument) {

        ExpenseFragment expenseFragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXAMPLE, expenseArgument);

        return expenseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadExpenses();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.expense_layout, container, false);

//        myDb = new DatabaseHelper(getActivity());

        listView = (ListView) myView.findViewById(R.id.lstExpenses);

        Log.e(TAG+" liST SIZE +++ ", " " + expenseExpenseItems.size());

        customListAdaptor = new CustomListAdaptor(this.getActivity(), expenseExpenseItems);
        listView.setAdapter(customListAdaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ExpenseItem expense = expenseExpenseItems.get(position);
                viewExpense(expense);
            }
        });

        return myView;
    }

    private class CustomListAdaptor extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<ExpenseItem> itemsList = new ArrayList<ExpenseItem>();

        ViewHolder viewHolder;
        Context context;
        Picasso mPicasso;

        public CustomListAdaptor(Context context, ArrayList<ExpenseItem> expenseExpenseItems) {
            layoutInflater = LayoutInflater.from(context);
            this.itemsList = expenseExpenseItems;
            this.context = context;
            this.mPicasso = Picasso.with(context);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ExpenseItem lstExpenseItem = itemsList.get(position);

            if (convertView == null) {

                convertView = layoutInflater.inflate(R.layout.list_view_item_layout, null);

                viewHolder = new ViewHolder();
                viewHolder.txtClaimTitle = (TextView) convertView.findViewById(R.id.txtClaimTitle);
                viewHolder.txtClaimAmount = (TextView) convertView.findViewById(R.id.txtClaimAmount);
                viewHolder.txtClaimDate = (TextView) convertView.findViewById(R.id.txtClaimDate);
                viewHolder.imgItemView = (ImageView) convertView.findViewById(R.id.imgItemView);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtClaimTitle.setText(lstExpenseItem.getExpenseName());
            viewHolder.txtClaimAmount.setText(lstExpenseItem.getExpenseAmount()+"");
            viewHolder.txtClaimDate.setText(lstExpenseItem.getExpenseDate());

            if (lstExpenseItem.getImagePath() != null) {

                mPicasso
                        .load(new File(lstExpenseItem.getImagePath()))
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.error_circle)
                        .resize(250, 200)
                        .into(viewHolder.imgItemView);
            }

            viewHolder.imgItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemPosition = position;
                    viewExpense(lstExpenseItem);
                }
            });

            return convertView;
        }

        public void setImageInItem(int position, String imageSrc) {
            ExpenseItem dataSet = itemsList.get(position);
            dataSet.setImagePath(imageSrc);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        ImageView imgItemView;
        TextView txtClaimTitle;
        TextView txtClaimDate;
        TextView txtClaimAmount;
    }

    private void viewExpense(ExpenseItem expenseItem) {
        Intent intent = new Intent(getActivity(), ExpenseActivity.class);
        intent.putExtra("expenseItem", expenseItem);
        startActivity(intent);
    }

    private class CustomClickListener implements View.OnClickListener {
        String btnName;
        Context context;
        ImageView imageView;

        public CustomClickListener(Context context, String imgBtnCam, ImageView imageView) {
            this.btnName = imgBtnCam;
            this.context = context;
            cameraPhoto = new CameraPhoto(context);
            this.imageView = imageView;
        }

        @Override
        public void onClick(View v) {
            if (btnName.equals("imgBtnCam")) {

                launchCamera(context);

            } else if (btnName.equals("imgBtnGallery")) {

                Toast.makeText(context, "Get Gallery", Toast.LENGTH_SHORT).show();

            }
        }

    }

    public void launchCamera(Context context) {
        cameraPhoto = new CameraPhoto(context);
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
            cameraPhoto.addToGallery();
        } catch (IOException e) {
            Toast.makeText(context, "Something went wrong while taking a photo",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {

                photoPath = cameraPhoto.getPhotoPath();

                customListAdaptor.setImageInItem(itemPosition, photoPath);

            }
        }

    }

    public void setExpenseExpenseItems(ArrayList<ExpenseItem> expenseExpenseItems) {
        this.expenseExpenseItems = expenseExpenseItems;
    }

    public void loadExpenses(){
        expenseExpenseItems.clear();
        LoadExpensesTask task = (LoadExpensesTask) new LoadExpensesTask().execute();
    }

    public class LoadExpensesTask extends AsyncTask<DatabaseHelper , Void , Void>{

        protected ProgressDialog mProgressDialog;
        private DatabaseHelper helper;

        @Override
        protected Void doInBackground(DatabaseHelper... params) {
            helper = new DatabaseHelper(getActivity());
            expenseExpenseItems.addAll(helper.getExpenseItems());
            return null;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.getWindow().setGravity(Gravity.CENTER);
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressDialog.dismiss();
        }
    }

    public ArrayList<ExpenseItem> getExpenseExpenseItems() {
        return expenseExpenseItems;
    }

    public void updateFragment1ListView() {
        if (customListAdaptor != null) {
            customListAdaptor.notifyDataSetChanged();
        }
    }
}
