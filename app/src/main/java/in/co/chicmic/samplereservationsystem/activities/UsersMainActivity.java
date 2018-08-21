package in.co.chicmic.samplereservationsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.dataModels.User;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.sessionManager.SessionManager;

public class UsersMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , View.OnClickListener {

    private FloatingActionButton mFab;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private SessionManager mSessionManager;
    private DataBaseHelper mDataBaseHelper;

    private CircleImageView mProfileImageView;
    private TextView mUserNameTV;
    private TextView mUserEmailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_main);

        mSessionManager = new SessionManager(getApplicationContext());
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDataBaseHelper = new DataBaseHelper(this);
        initViews();
        setDrawer();
        setUserProfile();
        setListeners();
    }

    private void setUserProfile() {
       User user = mDataBaseHelper.getUserDetails(mSessionManager
                               .getUserDetails().get(SessionManager.KEY_EMAIL).trim());
       mUserEmailTV.setText(user.getEmail());
       mUserNameTV.setText(user.getName() + "       " + user.getGender());
       mProfileImageView.setImageBitmap(user.getProfileImage());
    }

    private void setDrawer() {
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open
                , R.string.navigation_drawer_close
        );
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    private void setListeners() {
        mFab.setOnClickListener(this);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initViews() {
        mFab = findViewById(R.id.fab);
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        View view = mNavigationView.getHeaderView(0);
        mProfileImageView = view.findViewById(R.id.user_image);
        mUserNameTV = view.findViewById(R.id.user_name);
        mUserEmailTV = view.findViewById(R.id.user_email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mSessionManager.logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_booking) {

        } else if (id == R.id.nav_booking_history) {

        }  else if (id == R.id.nav_edit_profile) {

        } else if (id == R.id.nav_logout) {
            mSessionManager.logoutUser();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }
}

