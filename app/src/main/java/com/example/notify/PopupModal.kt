package com.example.notify

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.notify.databinding.PopupModalBinding

class PopupModal(var activity: Activity) : Dialog(activity), View.OnClickListener {
    private lateinit var binding: PopupModalBinding
    var delete_btn: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_modal)
        binding.notesDeleteBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.notes_delete_btn -> Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show()
        }
        dismiss()
    }
}