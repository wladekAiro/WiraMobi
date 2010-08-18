package com.example.wladek.wira;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wladek.wira.activity.ClaimsActivity;
import com.example.wladek.wira.fragments.tab_fragments.ClaimsFragment;
import com.example.wladek.wira.fragments.tab_fragments.ExpenseFragment;
import com.example.wladek.wira.fragments.tab_fragments.ProfileFragment;
import com.example.wladek.wira.pager_adapters.ViewPagerAdapter;
import com.example.wladek.wira.pojo.ExpenseItem;
import com.example.wladek.wira.utils.DatabaseHelper;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    FloatingActionButton fab;
    TabLayout tabLayout;
    private int[] tabIcons = {android.R.drawable.ic_menu_agenda, android.R.drawable.ic_menu_add,
            android.R.drawable.ic_menu_help};

    final int CAMERA_REQUEST = 321;
    final int GALLERY_REQUEST = 3233;
    final int RESULT_OK = -1;

    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;

    ArrayList<ExpenseItem> expenseExpenseItems = new ArrayList<>();
    ExpenseFragment expenseFragment;

    MaterialDialog.Builder materialDialog;
    MaterialDialog dialog;

    DatabaseHelper myDb;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDb = new DatabaseHelper(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Wira");

        actionBar = getSupportActionBar();

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        setUpViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGalleryOptions();
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0){

                    fab.setVisibility(View.VISIBLE);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showGalleryOptions();
                        }
                    });

                }else if(tab.getPosition() == 1){
                    fab.setVisibility(View.VISIBLE);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), ClaimsActivity.class);
                            startActivity(intent);
                        }
                    });

                }else {
                    fab.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0){

                    fab.setVisibility(View.VISIBLE);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showGalleryOptions();
                        }
                    });

                }else if(tab.getPosition() == 1){
                    fab.setVisibility(View.VISIBLE);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(getApplicationContext(), "Raise claim" , Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), ClaimsActivity.class);
                            startActivity(intent);
                        }
                    });

                }else {
                    fab.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0){

                    fab.setVisibility(View.VISIBLE);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showGalleryOptions();
                        }
                    });

                }else if(tab.getPosition() == 1){
                    fab.setVisibility(View.VISIBLE);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(getApplicationContext(), "Raise claim" , Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), ClaimsActivity.class);
                            startActivity(intent);
                        }
                    });

                }else {
                    fab.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void showGalleryOptions() {
        boolean wrapInScrollView = true;

        materialDialog = new MaterialDialog.Builder(this);
        materialDialog.title("Upload");
        materialDialog.customView(R.layout.select_gallery_layout, wrapInScrollView);
        materialDialog.cancelable(true);

        dialog = materialDialog.build();
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

                dialog.dismiss();

                launchCamera(context);

            } else if (btnName.equals("imgBtnGallery")) {
                dialog.dismiss();
                launchGallery(context);
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

    public void launchGallery(Context context){
        galleryPhoto = new GalleryPhoto(context);
        startActivityForResult(galleryPhoto.openGalleryIntent() , GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String photoPath = null;

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {

                photoPath = cameraPhoto.getPhotoPath();
                updateExpense(photoPath);

            }else if (requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                photoPath = galleryPhoto.getPath();
                updateExpense(photoPath);
            }
        }
    }

    public void updateExpense(String imageSrc) {

        ExpenseItem expenseItem = new ExpenseItem();
        expenseItem.setImagePath(imageSrc);
        expenseItem.setExpenseAmount(new Double(0));

        expenseItem = myDb.save(expenseItem);

        if (expenseFragment != null) {
            expenseFragment.loadExpenses();
            expenseFragment.updateFragment1ListView();
        }

    }

}
