package org.coscup.ccip.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.coscup.ccip.R;
import org.coscup.ccip.fragment.AnnouncementFragment;
import org.coscup.ccip.fragment.IRCFragment;
import org.coscup.ccip.fragment.MainFragment;
import org.coscup.ccip.fragment.ScheduleFragment;
import org.coscup.ccip.fragment.SponsorFragment;
import org.coscup.ccip.fragment.StaffFragment;
import org.coscup.ccip.util.PreferenceUtil;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private static TextView userIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        userIdTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_id);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        setupDrawerContent(navigationView);

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        setTitle(R.string.fast_pass);
        Fragment fragment = new MainFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.fast_pass:
                                fragment = new MainFragment();
                                break;
                            case R.id.schedule:
                                fragment = new ScheduleFragment();
                                break;
                            case R.id.announcement:
                                fragment = new AnnouncementFragment();
                                break;
                            case R.id.irc:
                                fragment = new IRCFragment();
                                break;
                            case R.id.sponsors:
                                fragment = new SponsorFragment();
                                break;
                            case R.id.staffs:
                                fragment = new StaffFragment();
                                break;
                        }

                        mDrawerLayout.closeDrawers();
                        setTitle(menuItem.getTitle());
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();

                        return true;
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public static void setUserId(String userId) {
        userIdTextView.setText(userId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null && result.getContents() != null) {
                PreferenceUtil.setToken(this, result.getContents());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
