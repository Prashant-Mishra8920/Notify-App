package com.example.notify;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomMenu extends Dialog implements View.OnClickListener {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    public Activity c;
    public TextView setting_btn,delete_btn,color_btn;
    int value = 2;
    String id;
    Context context;
    public CustomMenu(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_menu);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setting_btn = findViewById(R.id.setting_menu_btn);
        delete_btn = findViewById(R.id.delete_menu_btn);

        setting_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_menu_btn:
                Toast.makeText(getContext(),"delete",Toast.LENGTH_SHORT).show();
//                DocumentReference documentReference = firebaseFirestore.collection("Notify").document(firebaseUser.getUid()).collection("Notes").document(id);
//                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getContext(),"delete",Toast.LENGTH_SHORT).show();
//                        c.finish();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(),"cant't delete",Toast.LENGTH_SHORT).show();
//                    }
//                });
            case R.id.setting_menu_btn:
                Toast.makeText(getContext(),"setting",Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }
}
