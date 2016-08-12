package com.example.wladek.wira;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wladek.wira.fragments.tab_fragments.ClaimsFragment;
import com.example.wladek.wira.fragments.tab_fragments.ExpenseFragment;
import com.example.wladek.wira.fragments.tab_fragments.ProfileFragment;
import com.example.wladek.wira.pager_adapters.ViewPagerAdapter;
import com.example.wladek.wira.pojo.Item;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    FloatingActionButton fab;
    TabLayout tabLayout;
    private int[] tabIcons = {android.R.drawable.ic_menu_agenda, android.R.drawable.ic_menu_add,
            android.R.drawable.ic_menu_help};

    final int CAMERA_REQUEST = 321;
    final int RESULT_OK = -1;
    CameraPhoto cameraPhoto;

    ArrayList<Item> expenseItems = new ArrayList<>();
    ExpenseFragment expenseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Wira");
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        setUpViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGalleryOptions();
            }
        });

    }

    private void showGalleryOptions() {
        boolean wrapInScrollView = true;

        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this);
        materialDialog.title("Upload");
        materialDialog.customView(R.layout.select_gallery_layout, wrapInScrollView);
        materialDialog.cancelable(true);

        MaterialDialog dialog = materialDialog.build();
        dialog.show();

        View customView = dialog.getCustomView();

        ImageButton imgBtnGallery = (ImageButton) customView.findViewById(R.id.imgBtnGallery);
        ImageButton imgBtnCam = (ImageButton) customView.findViewById(R.id.imgBtnCam);
        TextView txtCamera = (TextView) customView.findViewById(R.id.txtCamera);
        TextView txtGallery = (TextView) customView.findViewById(R.id.txtGallery);

        imgBtnCam.setOnClickListener(new CustomClickListener(this, "imgBtnCam"));
        imgBtnGallery.setOnClickListener(new CustomClickListener(this, "imgBtnGallery"));
        txtGallery.setOnClickListener(new CustomClickListener(this, "imgBtnGallery"));
        txtCamera.setOnClickListener(new CustomClickListener(this, "imgBtnCam"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpViewPager(ViewPager viewPager) {
        //TO DO: write an adapter for this viewpager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        expenseFragment = ExpenseFragment.newInstance("Data for Exp fragment 1");

        viewPagerAdapter.addFragment(expenseFragment, "Expense");
        viewPagerAdapter.addFragment(ClaimsFragment.newInstance("Data for Clms fragment 2"), "Claims");
        viewPagerAdapter.addFragment(ProfileFragment.newInstance("Data for Pf fragment 3"), "Profile");

        viewPager.setAdapter(viewPagerAdapter);
    }

    private class CustomClickListener implements View.OnClickListener {
        String btnName;
        Context context;

        public CustomClickListener(Context context, String imgBtnCam) {
            this.btnName = imgBtnCam;
            this.context = context;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {

                String photoPath = cameraPhoto.getPhotoPath();

                updateExpense(photoPath);

            }
        }
    }

    public void updateExpense(String imageSrc) {

        Item item = new Item();
        item.setImagePath(imageSrc);
        item.setClaimTitle("Break fast");
        item.setClaimCenter("BBQ Central");
        item.setClaimDate("Aug 2, 2016");
        item.setClaimAmount("Ksh. 100");

        if (expenseFragment != null) {
            expenseFragment.getExpenseItems().add(item);
            expenseFragment.updateFragment1ListView();
        }

    }
}
