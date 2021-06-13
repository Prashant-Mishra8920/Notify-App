package com.example.notify

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.notify.databinding.CustomNotesMenuBinding
import com.google.firebase.auth.FirebaseAuth

class CustomNotesMenu(var c: Activity) : Dialog(c), View.OnClickListener {
    lateinit var binding: CustomNotesMenuBinding
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_notes_menu)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.logoutMenuBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.logout_menu_btn -> {
                val intent = Intent(c, authenticationpage::class.java)
                c.startActivity(intent)
                FirebaseAuth.getInstance().signOut()
            }
        }

//            ((Notes)getOwnerActivity()).logout();
//            Notes notes = new Notes();
//            notes.logout();
    }
}