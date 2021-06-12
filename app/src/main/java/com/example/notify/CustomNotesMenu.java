package com.example.notify;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

public class CustomNotesMenu extends Dialog implements View.OnClickListener {
    FirebaseAuth firebaseAuth;
    public Activity c;
    TextView logout_menu_btn;

    public CustomNotesMenu(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_notes_menu);

        firebaseAuth = FirebaseAuth.getInstance();
        logout_menu_btn = findViewById(R.id.logout_menu_btn);
        logout_menu_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.logout_menu_btn:
                    Intent intent = new Intent(c,authenticationpage.class);
                    c.startActivity(intent);
                    FirebaseAuth.getInstance().signOut();
            }

//            ((Notes)getOwnerActivity()).logout();
//            Notes notes = new Notes();
//            notes.logout();
        }
}
