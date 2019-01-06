package iitg.shubham.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import iitg.shubham.myapplication.About;
import iitg.shubham.myapplication.chat;
import iitg.shubham.myapplication.Events;
import iitg.shubham.myapplication.TimeTable;
import iitg.shubham.myapplication.R;
import iitg.shubham.myapplication.Settings;
import iitg.shubham.myapplication.Start;
import iitg.shubham.myapplication.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;
    private FirebaseAuth mauth;

    //This is our viewPager
    private ViewPager viewPager;

    ViewPagerAdapter adapter;

    //Fragments

   chat chatFragment;
   Events eventsFragment;
   TimeTable timetableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth=FirebaseAuth.getInstance();
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cu=mauth.getCurrentUser();
        if(cu == null){
            Logout();
        }
    }

    private void Logout() {
        Intent i = new Intent(getApplicationContext(),Start.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Learn:
                Toast.makeText(this, "Learn more", Toast.LENGTH_SHORT).show();
                Intent i =new Intent(getApplicationContext(), Learn.class);
                startActivity(i);

                return true;

            case R.id.action_settings:
                Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show();
                Intent i4 =new Intent(getApplicationContext(), Settings.class);
                startActivity(i4);

                return true;


            case R.id.logout:
                mauth.signOut();
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                Logout();
                return true;
            case R.id.about:
                Intent a =new Intent(getApplicationContext(), About.class);
                startActivity(a);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        chatFragment=new chat();
        eventsFragment  =new Events();
        timetableFragment=new TimeTable();
        adapter.addFragment(eventsFragment,"EVENTS");
        adapter.addFragment(chatFragment,"CHATS");
        adapter.addFragment(timetableFragment,"CALENDER");
        viewPager.setAdapter(adapter);
    }

}
