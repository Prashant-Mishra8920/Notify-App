package com.example.notify;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class EditNote extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    private EditText note_title,note_content;
    private TextView note_date_time;
    Intent data;
    String string;
    ImageView menu_button,save_note;
    Button colorButton;
    RelativeLayout editnote_parent;
    int cNumber=0;
    List<Integer> color_code;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        note_title = findViewById(R.id.note_title);
        note_content = findViewById(R.id.note_content);
        menu_button = findViewById(R.id.menu_button);
        colorButton = findViewById(R.id.color_button);
        note_date_time = findViewById(R.id.note_date_time);
        save_note = findViewById(R.id.save_note);

        color_code = new ArrayList<>();
        color_code.add(R.color.white);
        color_code.add(R.color.orange);
        color_code.add(R.color.yellow);
        color_code.add(R.color.red);
        color_code.add(R.color.green);
        color_code.add(R.color.cyan);
        color_code.add(R.color.blue);

        editnote_parent = findViewById(R.id.editnote_parent);
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yy hh:mm");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent();
        note_title.setText(data.getStringExtra("title"));
        note_content.setText(data.getStringExtra("content"));
        note_date_time.setText(sd.format(date));
        colorButton.setBackgroundColor(getColor(data.getIntExtra("colorId",0)));

        cNumber = color_code.indexOf(data.getIntExtra("colorId",0));

        save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButton.setBackgroundColor(getColor(color()));
            }
        });

        string = data.getStringExtra("intentId");
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomMenu cm = new CustomMenu(EditNote.this);
                cm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cm.getWindow().setDimAmount(0.5F);
                cm.getWindow().setGravity(Gravity.RIGHT|Gravity.TOP);
                cm.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                cm.show();
            }
        });

    }

    void click(){
        Toast.makeText(this,"method is working",Toast.LENGTH_SHORT).show();
    }
    public EditNote EditContext(){
        return this;
    }

    @Override
    public void onBackPressed() {
        String title = note_title.getText().toString();
        String content = note_content.getText().toString();
        String date = note_date_time.getText().toString();
        String color = (color_code.get(cNumber)).toString();
        if(content.isEmpty()){
            finish();
            Toast.makeText(getApplicationContext(),"note is empty",Toast.LENGTH_SHORT).show();
        }
        else{
            if(string.equals("add")){
                DocumentReference documentReference = firebaseFirestore.collection("Notify").document(firebaseUser.getUid()).collection("Notes").document();
                HashMap<String,Object> note = new HashMap<>();
                note.put("title",title);
                note.put("content",content);
                note.put("date",date);
                note.put("color",color);

                documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"note saved",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"note not saved",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if(string.equals("edit")){
                DocumentReference documentReference = firebaseFirestore.collection("Notify").document(firebaseUser.getUid()).collection("Notes").document(data.getStringExtra("noteId"));
                HashMap<String,Object> newNote = new HashMap<>();
                newNote.put("title",title);
                newNote.put("content",content);
                newNote.put("date",date);
                newNote.put("color",color);
                documentReference.set(newNote);

            }

        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    int color(){
        if(cNumber == color_code.size()-1){
            cNumber = 0;
        }
        else {
            cNumber += 1;
        }
        return color_code.get(cNumber);
    }
}