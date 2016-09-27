package com.example.wladek.wira;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wladek.wira.fragments.tab_fragments.ClaimsFragment;
import com.example.wladek.wira.fragments.tab_fragments.ExpenseFragment;
import com.example.wladek.wira.fragments.tab_fragments.ProfileFragment;
import com.example.wladek.wira.pager_adapters.ViewPagerAdapter;
import com.example.wladek.wira.pojo.ExpenseItem;
import com.example.wladek.wira.utils.DatabaseHelper;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    AddFloatingActionButton fab;
    TabLayout tabLayout;
    final int CAMERA_REQUEST = 321;
    final int GALLERY_REQUEST = 3233;
    final int QRCODE_REQUEST = 567;

    GalleryPhoto galleryPhoto;

    ArrayList<ExpenseItem> expenseExpenseItems = new ArrayList<>();
    ExpenseFragment expenseFragment;

    MaterialDialog.Builder builder;
    MaterialDialog dialog;

    DatabaseHelper myDb;

    ActionBar actionBar;
    Toolbar toolbar;
    private String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDb = new DatabaseHelper(this);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Expenses");

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        setUpViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.mipmap.expense_active);
        tabLayout.getTabAt(1).setIcon(R.mipmap.report_inactive_icon);
        tabLayout.getTabAt(2).setIcon(R.mipmap.contact_inactive);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));

        fab = (AddFloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGalleryOptions();
            }
        });
        fab.setPlusColor(R.color.blue_btn_bg_color);

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    toolbar.setTitle("Expenses");
                    tab.setIcon(R.mipmap.expense_active);
                    fab.setVisibility(View.VISIBLE);
                    fab.setImageResource(android.R.drawable.ic_menu_camera);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showGalleryOptions();
                        }
                    });

                } else if (tab.getPosition() == 1) {
                    toolbar.setTitle("Claims");
                    tab.setIcon(R.mipmap.report_active);
                } else {
                    toolbar.setTitle("Profile");
                    tab.setIcon(R.mipmap.contact_active);
                    fab.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    toolbar.setTitle("Expenses");
                    tab.setIcon(R.mipmap.expense_inactive);
                    fab.setVisibility(View.VISIBLE);
                    fab.setImageResource(android.R.drawable.ic_menu_camera);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showGalleryOptions();
                        }
                    });

                } else if (tab.getPosition() == 1) {
                    toolbar.setTitle("Claims");
                    tab.setIcon(R.mipmap.report_inactive_icon);
                } else {
                    toolbar.setTitle("Profile");
                    tab.setIcon(R.mipmap.contact_inactive);
                    fab.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    toolbar.setTitle("Expenses");
                    tab.setIcon(R.mipmap.expense_active);
                    fab.setVisibility(View.VISIBLE);
                    fab.setImageResource(android.R.drawable.ic_menu_camera);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showGalleryOptions();
                        }
                    });

                } else if (tab.getPosition() == 1) {
                    toolbar.setTitle("Claims");
                    tab.setIcon(R.mipmap.report_active);
                } else {
                    toolbar.setTitle("Profile");
                    tab.setIcon(R.mipmap.contact_active);
                    fab.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void showGalleryOptions() {
        boolean wrapInScrollView = true;

        builder = new MaterialDialog.Builder(this);
        builder.title("Upload");
        builder.customView(R.layout.select_gallery_layout, wrapInScrollView);
        builder.cancelable(true);

        dialog = builder.build();
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
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (in.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {

//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.wladek.wira",
//                        photoFile);

                in.putExtra("output", Uri.fromFile(photoFile));
            }
        }

        startActivityForResult(in, CAMERA_REQUEST);
        addToGallery();
    }

    //Create file
    private File createImageFile() {

        String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        photoPath = image.getAbsolutePath();

        return image;
    }

    public void addToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(this.photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void launchGallery(Context context) {
        galleryPhoto = new GalleryPhoto(context);
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST);
        startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                if (photoPath != null) {
                    updateExpense(photoPath);
                }

            } else if (requestCode == GALLERY_REQUEST) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
//                photoPath = getPhotoPathFromGallery(uri);
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

    public String getPhotoPathFromGallery(Uri uri){
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver()
                .query(uri, filePathColumn, null, null,
                        null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return picturePath;
    }

}
