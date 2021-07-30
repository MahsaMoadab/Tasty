package com.moadab.tasty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.moadab.tasty.Fragments.LoginFragment;
import com.moadab.tasty.Fragments.SignUpFragment;

public class LoginOrSignUpActivity extends AppCompatActivity  implements View.OnClickListener{

    /* Initialize variables */
    private ColorStateList def;
    private TextView loginTab,signUpTab;
    private TextView selectTab;
    private FrameLayout frameLayout;
    private Animation fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);


        loginTab = findViewById(R.id.login_tab);
        signUpTab = findViewById(R.id.signup_tab);
        selectTab = findViewById(R.id.select_tab);
        frameLayout = findViewById(R.id.flContainer);
        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new LoginFragment()).commit();


        loginTab.setOnClickListener(this);
        signUpTab.setOnClickListener(this);

        def = signUpTab.getTextColors();

        fragment = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        frameLayout.setAnimation(fragment);

    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.login_tab) {
            selectTab.animate().x(0).setDuration(500);
            signUpTab.setTextColor(def);
            loginTab.setTextColor(Color.WHITE);
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new LoginFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

        } else if (view.getId() == R.id.signup_tab) {
            int size = signUpTab.getWidth();
            selectTab.animate().x(size).setDuration(500);
            loginTab.setTextColor(def);
            signUpTab.setTextColor(Color.WHITE);
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new SignUpFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

        }
    }

  }