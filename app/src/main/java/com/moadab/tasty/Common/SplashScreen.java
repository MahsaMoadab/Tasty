package com.moadab.tasty.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.moadab.tasty.LoginOrSignUpActivity;
import com.moadab.tasty.MainActivity;
import com.moadab.tasty.R;
import com.moadab.tasty.ui.home.HomeActivity;

public class SplashScreen extends AppCompatActivity {

    /* Initialize variables */
    Animation titleAnimation,detailAnimation,logoAnimation;
    ImageView splashTitle,splashDetail,splashLogo,splashMiniLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* Access to activity elements */
        splashTitle = findViewById(R.id.splash_title);
        splashDetail = findViewById(R.id.spalsh_detail);
        splashLogo = findViewById(R.id.splash_back);
        splashMiniLogo = findViewById(R.id.mini_logo);

        /* set app Name Animation */
        titleAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        detailAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        logoAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        splashTitle.setAnimation(titleAnimation);
        splashLogo.setAnimation(logoAnimation);
        splashDetail.setAnimation(detailAnimation);
        splashMiniLogo.setAnimation(logoAnimation);

        /* Method Time for SplashScreen Activity */
        Thread splashTime= new Thread(() -> {
            try {
                Thread.sleep(4500);
            } catch (Exception e) {
            }
            finally {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    /* Start MainActivity if User was Login in FireBase */
                    startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                    finish();

                   }
                    else {
                       /* Start LoginActivity if User didn't Login in FireBase */
//                        startActivity(new Intent(SplashScreenActivity.this, OnBoardingActivity.class));
                        startActivity(new Intent(SplashScreen.this, LoginOrSignUpActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                       finish();
                    }
            }
        });
        splashTime.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}