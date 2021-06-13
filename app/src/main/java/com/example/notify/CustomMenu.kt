package com.example.notify

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.example.notify.databinding.CustomMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class CustomMenu(var c: Activity) : Dialog(c), View.OnClickListener {
    lateinit var binding: CustomMenuBinding
    var firebaseAuth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var firebaseFirestore: FirebaseFirestore? = null
    var color_btn: TextView? = null
    var value = 2
    var id: String? = null
    override fun onCreate(savedInstanceState: Bundle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_menu)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser()
        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.settingMenuBtn.setOnClickListener(this)
        binding.deleteMenuBtn.setOnClickListener(this)
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.delete_menu_btn -> {
                Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show()
                Toast.makeText(getContext(), "setting", Toast.LENGTH_SHORT).show()
            }
            R.id.setting_menu_btn -> Toast.makeText(getContext(), "setting", Toast.LENGTH_SHORT)
                .show()
        }
        dismiss()
    }
}