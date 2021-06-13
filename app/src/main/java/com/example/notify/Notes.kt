package com.example.notify

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class Notes : AppCompatActivity() {
    var firebaseAuth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var firebaseFirestore: FirebaseFirestore? = null
    var noteAdapter: FirestoreRecyclerAdapter<firebaseModel, NoteViewHolder>? = null
    private val logout: Button? = null
    var add_note: FloatingActionButton? = null
    var random: Random? = null
    private var prevNum = 0
    private var prevToNum = 0
    var notes_menu_btn: ImageView? = null
    var recyclerView: RecyclerView? = null
    var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        val toolbar = findViewById<Toolbar>(R.id.notes_toolbar)
        toolbar.title = "All Notes"
        setSupportActionBar(toolbar)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser()
        firebaseFirestore = FirebaseFirestore.getInstance()
        add_note = findViewById(R.id.add_note_btn)
        add_note.setOnClickListener(View.OnClickListener { v ->
            val addIntent = Intent(v.context, EditNote::class.java)
            addIntent.putExtra("intentId", "add")
            addIntent.putExtra("colorId", R.color.yellow)
            startActivity(addIntent)
        })
        val colorCode = HashMap<Int, Int>()
        colorCode[R.color.orange] = R.color.light_orange
        colorCode[R.color.yellow] = R.color.light_yellow
        //        colorCode.put(R.color.purple,R.color.light_purple);
//        colorCode.put(R.color.magenta,R.color.light_magenta);
        colorCode[R.color.red] = R.color.light_red
        colorCode[R.color.green] = R.color.light_green
        colorCode[R.color.cyan] = R.color.light_cyan
        colorCode[R.color.blue] = R.color.light_blue
        //        colorCode.put(R.color.voilet,R.color.light_voilet);
        val query: Query = firebaseFirestore.collection("Notify").document(firebaseUser.getUid())
            .collection("Notes")
        val userNotes: FirestoreRecyclerOptions<firebaseModel> = Builder<firebaseModel>().setQuery(
            query,
            firebaseModel::class.java
        ).build()
        noteAdapter =
            object : FirestoreRecyclerAdapter<firebaseModel?, NoteViewHolder?>(userNotes) {
                protected fun onBindViewHolder(
                    holder: NoteViewHolder,
                    position: Int,
                    model: firebaseModel
                ) {
                    val color_code = randomColor()
                    val fireColor = Integer.valueOf(model.color)
                    holder.header.setBackgroundColor(
                        holder.itemView.resources.getColor(
                            fireColor,
                            null
                        )
                    )
                    holder.notecontent.setBackgroundColor(
                        holder.itemView.resources.getColor(
                            fireColor,
                            null
                        )
                    )
                    holder.notedate.setBackgroundColor(
                        holder.itemView.resources.getColor(
                            fireColor,
                            null
                        )
                    )
                    holder.notetitle.text = model.title
                    holder.notedate.text = model.date
                    holder.notecontent.text = model.content!!.substring(0, StringLength(model))
                    if (fireColor == R.color.white) {
                        holder.notetitle.setTextColor(resources.getColor(R.color.black))
                        holder.notedate.setTextColor(resources.getColor(R.color.grey))
                        holder.notecontent.setTextColor(resources.getColor(R.color.grey))
                    }
                    val docId: String = noteAdapter.getSnapshots().getSnapshot(position).getId()
                    holder.linearLayout.setOnClickListener { v ->
                        val intent = Intent(v.context, EditNote::class.java)
                        intent.putExtra("title", model.title)
                        intent.putExtra("content", model.content)
                        intent.putExtra("noteId", docId)
                        intent.putExtra("intentId", "edit")
                        intent.putExtra("colorId", fireColor)
                        startActivity(intent)
                    }
                    holder.linearLayout.setOnLongClickListener { v ->
                        Toast.makeText(applicationContext, model.color, Toast.LENGTH_SHORT).show()
                        val popupMenu = PopupMenu(v.context, v)
                        popupMenu.gravity = Gravity.RIGHT
                        popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                            val documentReference: DocumentReference =
                                firebaseFirestore.collection("Notify")
                                    .document(firebaseUser.getUid()).collection("Notes")
                                    .document(docId)
                            documentReference.delete()
                                .addOnFailureListener(object : OnFailureListener() {
                                    fun onFailure(e: Exception) {
                                        Toast.makeText(
                                            applicationContext,
                                            "can't delete",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            false
                        }
                        popupMenu.show()
                        val popupModal = PopupModal(this@Notes)
                        val layoutParams = WindowManager.LayoutParams()
                        layoutParams.width = WindowManager.LayoutParams.FILL_PARENT
                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                        layoutParams.gravity = Gravity.BOTTOM
                        layoutParams.windowAnimations = R.style.PopupModalAnimation
                        layoutParams.dimAmount = 1f
                        //
                        popupModal.getWindow()
                            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        popupModal.show()
                        popupModal.getWindow().setAttributes(layoutParams)
                        true
                    }
                }

                fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.note_layout, parent, false)
                    return NoteViewHolder(view)
                }
            }
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(staggeredGridLayoutManager)
        recyclerView.setAdapter(noteAdapter)
        notes_menu_btn = findViewById(R.id.notes_menu_btn)
        notes_menu_btn.setOnClickListener(View.OnClickListener {
            val customNotesMenu = CustomNotesMenu(this@Notes)
            customNotesMenu.window!!.setGravity(Gravity.END or Gravity.TOP)
            customNotesMenu.window!!.attributes.windowAnimations = R.style.DialogAnimation
            customNotesMenu.window!!.setDimAmount(0.5f)
            customNotesMenu.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            customNotesMenu.show()
        })
    }

    fun StringLength(model: firebaseModel): Int {
        val arr = model.content!!.split("\r\n|\r|\n").toTypedArray()
        return if (arr.size > 10 && arr.size < 25) {
            (model.content!!.length * 0.5).toInt()
        } else if (arr.size > 25) {
            (model.content!!.length * 0.3).toInt()
        } else {
            model.content!!.length
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        finish()
        startActivity(Intent(this@Notes, authenticationpage::class.java))
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val notetitle: TextView
        private val notecontent: TextView
        private val notedate: TextView
        var linearLayout: LinearLayout
        var header: LinearLayout

        init {
            notetitle = itemView.findViewById(R.id.notetitle)
            notecontent = itemView.findViewById(R.id.notecontent)
            notedate = itemView.findViewById(R.id.note_date)
            linearLayout = itemView.findViewById(R.id.note)
            header = itemView.findViewById(R.id.header)
        }
    }

    private fun randomColor(): Int {
        val color_code: MutableList<Int> = ArrayList()
        color_code.add(R.color.orange)
        color_code.add(R.color.yellow)
        //        color_code.add(R.color.purple);
//        color_code.add(R.color.magenta);
        color_code.add(R.color.red)
        color_code.add(R.color.green)
        color_code.add(R.color.cyan)
        color_code.add(R.color.blue)
        //        color_code.add(R.color.voilet);
        random = Random()
        var number = random!!.nextInt(color_code.size)
        prevToNum = prevNum
        while (true) {
            number = random!!.nextInt(color_code.size)
            if (number == prevNum || number == prevToNum) {
                number = random!!.nextInt(color_code.size)
                continue
            } else {
                prevToNum = prevNum
                prevNum = number
                break
            }
        }
        return color_code[number]
    }

    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (noteAdapter != null) {
            noteAdapter.stopListening()
        }
    } //    @Override
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