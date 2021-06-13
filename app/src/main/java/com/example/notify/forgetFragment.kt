package com.example.notify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class forgetFragment : Fragment() {
    var firebaseAuth: FirebaseAuth? = null
    private var email: EditText? = null
    private var recover: Button? = null
    var forget_layout: RelativeLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.forget_password_fragment, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        email = v.findViewById<View>(R.id.user_email_address) as EditText
        recover = v.findViewById<View>(R.id.recover_button) as Button
        recover!!.setOnClickListener {
            val email_txt = email!!.text.toString()
            if (email_txt.isEmpty()) {
                email!!.error = "Email required"
            } else {
                val firebaseUser = firebaseAuth!!.getCurrentUser()
                if (firebaseUser != null) {
                    Toast.makeText(activity, "User not found", Toast.LENGTH_SHORT).show()
                } else {
                    firebaseAuth!!.sendPasswordResetEmail(email_txt)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "Email sent", Toast.LENGTH_SHORT)
                                    .show()
                                (activity as authenticationpage?)!!.loginFragmentManager()
                            } else {
                                Toast.makeText(activity, "User not found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            }
        }
        forget_layout = v.findViewById<View>(R.id.forget_parent_layout) as RelativeLayout
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}