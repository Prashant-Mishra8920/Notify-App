package com.example.notify;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class PopupModal extends Dialog implements View.OnClickListener {
    Activity activity;
    LinearLayout delete_btn;
    public PopupModal(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_modal);

        delete_btn = findViewById(R.id.notes_delete_btn);
        delete_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.notes_delete_btn:
                Toast.makeText(getContext(),"delete",Toast.LENGTH_SHORT).show();
        }
        dismiss();

    }
}
