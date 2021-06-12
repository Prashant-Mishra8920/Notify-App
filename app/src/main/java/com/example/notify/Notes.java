package com.example.notify;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Notes extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebaseModel,NoteViewHolder> noteAdapter;
    private Button logout;
    FloatingActionButton add_note;
    Random random;
    private int prevNum = 0,prevToNum;
    ImageView notes_menu_btn;

    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = findViewById(R.id.notes_toolbar);
        toolbar.setTitle("All Notes");
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        add_note = findViewById(R.id.add_note_btn);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(v.getContext(),EditNote.class);
                addIntent.putExtra("intentId","add");
                addIntent.putExtra("colorId",R.color.yellow);
                startActivity(addIntent);
            }
        });

        HashMap<Integer,Integer> colorCode = new HashMap<>();
        colorCode.put(R.color.orange,R.color.light_orange);
        colorCode.put(R.color.yellow,R.color.light_yellow);
//        colorCode.put(R.color.purple,R.color.light_purple);
//        colorCode.put(R.color.magenta,R.color.light_magenta);
        colorCode.put(R.color.red,R.color.light_red);
        colorCode.put(R.color.green,R.color.light_green);
        colorCode.put(R.color.cyan,R.color.light_cyan);
        colorCode.put(R.color.blue,R.color.light_blue);
//        colorCode.put(R.color.voilet,R.color.light_voilet);

        Query query = firebaseFirestore.collection("Notify").document(firebaseUser.getUid()).collection("Notes");
        FirestoreRecyclerOptions<firebaseModel> userNotes = new FirestoreRecyclerOptions.Builder<firebaseModel>().setQuery(query,firebaseModel.class).build();
        noteAdapter = new FirestoreRecyclerAdapter<firebaseModel, NoteViewHolder>(userNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebaseModel model) {
                int color_code = randomColor();
                int fireColor = Integer.valueOf(model.getColor());
                holder.header.setBackgroundColor(holder.itemView.getResources().getColor(fireColor,null));
                holder.notecontent.setBackgroundColor(holder.itemView.getResources().getColor(fireColor,null));
                holder.notedate.setBackgroundColor(holder.itemView.getResources().getColor(fireColor,null));
                holder.notetitle.setText(model.getTitle());
                holder.notedate.setText(model.getDate());
                holder.notecontent.setText(model.getContent().substring(0,StringLength(model)));


                if(fireColor == R.color.white){
                    holder.notetitle.setTextColor(getResources().getColor(R.color.black));
                    holder.notedate.setTextColor(getResources().getColor(R.color.grey));
                    holder.notecontent.setTextColor(getResources().getColor(R.color.grey));
                }

                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),EditNote.class);
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("content",model.getContent());
                        intent.putExtra("noteId",docId);
                        intent.putExtra("intentId","edit");
                        intent.putExtra("colorId",fireColor);
                        startActivity(intent);
                    }
                });
                holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Toast.makeText(getApplicationContext(),model.getColor(),Toast.LENGTH_SHORT).show();
                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.RIGHT);
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                DocumentReference documentReference = firebaseFirestore.collection("Notify").document(firebaseUser.getUid()).collection("Notes").document(docId);
                                documentReference.delete().addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"can't delete",Toast.LENGTH_SHORT).show();

                                    }
                                });

                                return false;
                            }
                        });
                        popupMenu.show();
                        PopupModal popupModal = new PopupModal(Notes.this);
                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.width = WindowManager.LayoutParams.FILL_PARENT;
                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        layoutParams.gravity = Gravity.BOTTOM;
                        layoutParams.windowAnimations = R.style.PopupModalAnimation;
                        layoutParams.dimAmount = 1;
//
                        popupModal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        popupModal.show();
                        popupModal.getWindow().setAttributes(layoutParams);
                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout,parent,false);
                return new NoteViewHolder(view);
            }

        };

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(noteAdapter);

        notes_menu_btn = findViewById(R.id.notes_menu_btn);
        notes_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CustomNotesMenu customNotesMenu = new CustomNotesMenu(Notes.this);
               customNotesMenu.getWindow().setGravity(Gravity.END|Gravity.TOP);
               customNotesMenu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
               customNotesMenu.getWindow().setDimAmount(0.5f);
               customNotesMenu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               customNotesMenu.show();
            }
        });


    }
    int StringLength(firebaseModel model){
        String[] arr = model.getContent().split("\r\n|\r|\n");
        if(arr.length > 10 && arr.length<25){
            return (int) (model.getContent().length()*0.5);
        }
        else if(arr.length>25){
            return (int) (model.getContent().length()*0.3);
        }
        else {
            return model.getContent().length();
        }
    }
    void logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(Notes.this,authenticationpage.class));
    }
    public static class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView notetitle;
        private  TextView notecontent;
        private  TextView notedate;
        LinearLayout linearLayout,header;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle = itemView.findViewById(R.id.notetitle);
            notecontent = itemView.findViewById(R.id.notecontent);
            notedate = itemView.findViewById(R.id.note_date);
            linearLayout = itemView.findViewById(R.id.note);
            header = itemView.findViewById(R.id.header);
        }
    }
    private int randomColor(){

        List<Integer> color_code = new ArrayList<>();
        color_code.add(R.color.orange);
        color_code.add(R.color.yellow);
//        color_code.add(R.color.purple);
//        color_code.add(R.color.magenta);
        color_code.add(R.color.red);
        color_code.add(R.color.green);
        color_code.add(R.color.cyan);
        color_code.add(R.color.blue);
//        color_code.add(R.color.voilet);

        random = new Random();
        int number = random.nextInt(color_code.size());
        prevToNum = prevNum;
        while(true){
            number = random.nextInt(color_code.size());
            if (number == prevNum || number == prevToNum){
                number = random.nextInt(color_code.size());
                continue;
            }
            else{
                prevToNum = prevNum;
                prevNum = number;
                break;
            }
        }
        return color_code.get(number);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter != null){
            noteAdapter.stopListening();
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu,menu);
//        CustomMenu customMenu = new CustomMenu(Notes.this);
//        customMenu.show();
//        return true;
//    }
////
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.notes_delete_btn:
////                firebaseAuth.signOut();
////                finish();
////                startActivity(new Intent(Notes.this,authenticationpage.class));
//                Toast.makeText(this,"delete_main",Toast.LENGTH_SHORT).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}