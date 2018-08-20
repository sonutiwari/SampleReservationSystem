package in.co.chicmic.samplereservationsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.sessionManager.SessionManager;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;

public class SplashScreenActivity extends AppCompatActivity {
    SessionManager mSession;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        // Session class instance
        mSession = new SessionManager(getApplicationContext());

        mSession.checkLogin();

        // get user data from session
        HashMap<String, String> user = mSession.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (mSession.isLoggedIn()){
                    Intent mainIntent =
                            new Intent(SplashScreenActivity.this,UsersMainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                }else{
                    Intent mainIntent =
                            new Intent(SplashScreenActivity.this,LoginActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                }
                SplashScreenActivity.this.finish();
            }
        }, AppConstants.sSPLASH_SCREEN_DELAY);
    }
}
