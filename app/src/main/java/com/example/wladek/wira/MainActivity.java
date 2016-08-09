package com.example.wladek.wira;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wladek.wira.fragments.tab_fragments.ClaimsFragment;
import com.example.wladek.wira.fragments.tab_fragments.ExpenseFragment;
import com.example.wladek.wira.fragments.tab_fragments.ProfileFragment;
import com.example.wladek.wira.pager_adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    FloatingActionButton fab;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        setUpViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

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

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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

        viewPagerAdapter.addFragment(ExpenseFragment.newInstance("Data for Exp fragment 1"), "Expense");
        viewPagerAdapter.addFragment(ClaimsFragment.newInstance("Data for Clms fragment 2"), "Claims");
        viewPagerAdapter.addFragment(ProfileFragment.newInstance("Data for Pf fragment 3"), "Profile");

        viewPager.setAdapter(viewPagerAdapter);
    }
}
