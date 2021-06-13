package com.example.notify

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.notify.databinding.ActivityEditNoteBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditNote : AppCompatActivity() {
    private lateinit var binding: ActivityEditNoteBinding
    var firebaseAuth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var firebaseFirestore: FirebaseFirestore? = null
    private var note_title: EditText? = null
    private var note_content: EditText? = null
    private var note_date_time: TextView? = null
    var string: String? = null
    var menu_button: ImageView? = null
    var save_note: ImageView? = null
    var colorButton: Button? = null
    var cNumber = 0
    lateinit var data:Bundle
    var color_code: ArrayList<Int> = arrayListOf()

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser()
        note_title = findViewById(R.id.note_title)
        note_content = findViewById(R.id.note_content)
        menu_button = findViewById(R.id.menu_button)
        colorButton = findViewById(R.id.color_button)
        note_date_time = findViewById(R.id.note_date_time)
        save_note = findViewById(R.id.save_note)
        color_code = ArrayList()
        color_code.add(R.color.white)
        color_code.add(R.color.orange)
        color_code.add(R.color.yellow)
        color_code.add(R.color.red)
        color_code.add(R.color.green)
        color_code.add(R.color.cyan)
        color_code.add(R.color.blue)
        val date = Date()
        val sd = SimpleDateFormat("dd/MM/yy hh:mm")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        data = intent?.extras!!
        binding.noteTitle.setText(data.getString("title"))
        binding.noteContent.setText(data.getString("content"))
        binding.noteDateTime.setText(sd.format(date))
        binding.colorButton.setBackgroundColor(getColor(data.getInt("colorId", 0)))
        cNumber = color_code.indexOf(data.getInt("colorId", 0))
        binding.saveNote.setOnClickListener(View.OnClickListener { onBackPressed() })
        binding.colorButton.setOnClickListener(View.OnClickListener {
            binding.colorButton.setBackgroundColor(
                getColor(color())
            )
        })
        string = data.getString("intentId")
        binding.menuButton.setOnClickListener(View.OnClickListener {
            val cm = CustomMenu(this@EditNote)
            cm.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            cm.window?.setDimAmount(0.5f)
            cm.window?.setGravity(Gravity.RIGHT or Gravity.TOP)
            cm.window?.attributes!!.windowAnimations = R.style.DialogAnimation
            cm.show()
        })
    }

    fun click() {
        Toast.makeText(this, "method is working", Toast.LENGTH_SHORT).show()
    }

    fun EditContext(): EditNote {
        return this
    }

    override fun onBackPressed() {
        val title = note_title!!.text.toString()
        val content = note_content!!.text.toString()
        val date = note_date_time!!.text.toString()
        val color = color_code!![cNumber].toString()
        if (content.isEmpty()) {
            finish()
            Toast.makeText(applicationContext, "note is empty", Toast.LENGTH_SHORT).show()
        } else {
            if (string == "add") {
                val documentReference: DocumentReference =
                    firebaseFirestore?.collection("Notify")?.document(firebaseUser!!.uid)
                        ?.collection("Notes")!!.document()
                val note = HashMap<String, Any>()
                note["title"] = title
                note["content"] = content
                note["date"] = date
                note["color"] = color
                documentReference.set(note)
                    .addOnCompleteListener {
                        Toast.makeText(applicationContext, "note saved", Toast.LENGTH_SHORT)
                            .show()
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext, "note not saved", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else if (string == "edit") {
                val documentReference: DocumentReference =
                    firebaseFirestore?.collection("Notify")?.document(firebaseUser!!.uid)
                        ?.collection("Notes")!!.document(
                            data.getString("noteId")!!
                        )
                val newNote = HashMap<String, Any>()
                newNote["title"] = title
                newNote["content"] = content
                newNote["date"] = date
                newNote["color"] = color
                documentReference.set(newNote)
            }
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun color(): Int {
        if (cNumber == color_code!!.size - 1) {
            cNumber = 0
        } else {
            cNumber += 1
        }
        return color_code!![cNumber]
    }
}