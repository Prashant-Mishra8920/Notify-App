package com.example.notify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class authenticationpage extends AppCompatActivity {
    private String email,password;
    private TextView login_selector,sign_selector,forget_selector;
    private Button login_button,sign_button;
    LinearLayout login_layout,sign_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticationpage);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        EditText user_email_address = findViewById(R.id.user_email_address);
        EditText user_password = findViewById(R.id.user_password);

        login_selector = findViewById(R.id.login_selector);
        sign_selector = findViewById(R.id.sign_selector);
        login_layout = findViewById(R.id.login_linear_layout);
        sign_layout = findViewById(R.id.sign_linear_layout);
        login_layout.setVisibility(View.GONE);


        loginFragmentManager();
        login_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragmentManager();
                login_layout.setVisibility(View.GONE);
                sign_layout.setVisibility(View.VISIBLE);
            }
        });

        sign_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signFragmentManager();
                sign_layout.setVisibility(View.GONE);
                login_layout.setVisibility(View.VISIBLE);
            }
        });




    }

    void loginFragmentManager(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.authframe,new LoginFragment(),"h");
        fragmentTransaction.commit();
        login_selector.setTypeface(null,Typeface.BOLD);
        sign_selector.setTypeface(null,Typeface.NORMAL);
    }
    void signFragmentManager(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit);
        fragmentTransaction.replace(R.id.authframe,new SignFragment(),"h");
        fragmentTransaction.commit();
        sign_selector.setTypeface(null,Typeface.BOLD);
        login_selector.setTypeface(null,Typeface.NORMAL);
    }
    void forgetFragmentManager(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.authframe,new forgetFragment());
        fragmentTransaction.commit();
    }
}