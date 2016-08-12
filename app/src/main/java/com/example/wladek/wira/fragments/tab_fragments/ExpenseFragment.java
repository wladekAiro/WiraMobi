package com.example.wladek.wira.fragments.tab_fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.wladek.wira.MainActivity;
import com.example.wladek.wira.R;
import com.example.wladek.wira.activity.ClaimActivity;
import com.example.wladek.wira.pojo.Item;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
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

    ArrayList<Item> expenseItems = new ArrayList<>();

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.expense_layout, container, false);

        listView = (ListView) myView.findViewById(R.id.lstExpenses);

        Log.e("liST SIZE +++ ", " " + expenseItems.size());

        customListAdaptor = new CustomListAdaptor(this.getActivity(), expenseItems);
        listView.setAdapter(customListAdaptor);

        return myView;
    }

    public class CustomListAdaptor extends BaseAdapter {

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Item lstItem = itemsList.get(position);

            if (convertView == null) {

                convertView = layoutInflater.inflate(R.layout.list_view_item_layout, null);

                viewHolder = new ViewHolder();
                viewHolder.txtClaimTitle = (TextView) convertView.findViewById(R.id.txtClaimTitle);
                viewHolder.txtClaimAmount = (TextView) convertView.findViewById(R.id.txtClaimAmount);
                viewHolder.txtClaimCenter = (TextView) convertView.findViewById(R.id.txtClaimCenter);
                viewHolder.txtClaimDate = (TextView) convertView.findViewById(R.id.txtClaimDate);
                viewHolder.imgItemView = (ImageView) convertView.findViewById(R.id.imgItemView);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtClaimTitle.setText(lstItem.getClaimTitle());
            viewHolder.txtClaimAmount.setText(lstItem.getClaimAmount());
            viewHolder.txtClaimCenter.setText(lstItem.getClaimCenter());
            viewHolder.txtClaimDate.setText(lstItem.getClaimDate());

            if (lstItem.getImagePath() != null) {
                try {
                    Bitmap bitmap = ImageLoader.init().from(lstItem.getImagePath()).requestSize(100, 100).getBitmap();
                    viewHolder.imgItemView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            viewHolder.imgItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemPosition = position;
                    showGalleryOptions(lstItem);
                }
            });

            return convertView;
        }

        public void setImageInItem(int position, String imageSrc) {
            Item dataSet = itemsList.get(position);
            dataSet.setImagePath(imageSrc);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        ImageView imgItemView;
        TextView txtClaimTitle;
        TextView txtClaimCenter;
        TextView txtClaimDate;
        TextView txtClaimAmount;
    }

    private void showGalleryOptions(Item item) {
        Intent intent = new Intent(getActivity(), ClaimActivity.class);
        intent.putExtra("item", item);
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

    public void setExpenseItems(ArrayList<Item> expenseItems) {
        this.expenseItems = expenseItems;
    }

    public ArrayList<Item> getExpenseItems() {
        return expenseItems;
    }

    public void updateFragment1ListView() {
        if (customListAdaptor != null) {
            customListAdaptor.notifyDataSetChanged();
        }
    }
}
