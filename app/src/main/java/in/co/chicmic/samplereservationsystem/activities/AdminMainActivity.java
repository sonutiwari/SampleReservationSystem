package in.co.chicmic.samplereservationsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.fragments.AddTrainFragment;
import in.co.chicmic.samplereservationsystem.fragments.ApproveUsersFragment;
import in.co.chicmic.samplereservationsystem.fragments.BlockUserFragment;
import in.co.chicmic.samplereservationsystem.listeners.AddTrainFragmentListener;
import in.co.chicmic.samplereservationsystem.listeners.ApproveUsersClickListener;
import in.co.chicmic.samplereservationsystem.listeners.BlockUserFragmentListener;
import in.co.chicmic.samplereservationsystem.sessionManager.SessionManager;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;

public class AdminMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , ApproveUsersClickListener
        , BlockUserFragmentListener
        , AddTrainFragmentListener
        , View.OnClickListener
{

    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        mSessionManager = new SessionManager(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.admin_main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_approve_new_users) {
            getSupportFragmentManager().beginTransaction().add(R.id.frame
                    , new ApproveUsersFragment(), AppConstants.sAPPROVE_USER_FRAGMENT).commit();
        } else if (id == R.id.nav_trains_menu) {

        } else if (id == R.id.nav_block_users) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame
                    , new BlockUserFragment(), AppConstants.sBLOCK_USER_FRAGMENT).commit();
        } else if (id == R.id.nav_update_pnr_requests) {

        } else if (id == R.id.nav_edit_profile) {

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
                getSupportFragmentManager().beginTransaction().replace(R.id.frame
                        , new AddTrainFragment(), AppConstants.sADD_TRAIN_TAG).commit();
                break;
        }
    }
}
