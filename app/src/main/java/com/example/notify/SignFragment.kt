package com.example.notify

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignFragment : Fragment() {
    var firebaseAuth: FirebaseAuth? = null
    private var sign_button: Button? = null
    var parent_layout: RelativeLayout? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.signup_fragment, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        val user_email_address = v.findViewById<View>(R.id.user_email_address) as TextInputEditText
        val user_password = v.findViewById<View>(R.id.user_password) as TextInputEditText
        val confirm_password = v.findViewById<View>(R.id.confirm_password) as TextInputEditText
        parent_layout = v.findViewById<View>(R.id.sign_parent_layout) as RelativeLayout
        sign_button = v.findViewById<View>(R.id.sign_button) as Button
        sign_button!!.setOnClickListener {
            val email = user_email_address.text.toString()
            val pass = user_password.text.toString()
            val confirm_pass = confirm_password.text.toString()
            if (email.isEmpty()) {
                user_email_address.error = "Email Required"
            }
            if (pass.isEmpty()) {
                user_password.error = "Password Required"
            }
            if (pass != confirm_pass) {
                confirm_password.error = "Wrong input"
            }
            if (confirm_pass.isEmpty()) {
                confirm_password.error = "Confirm password"
            } else if (pass.length < 8) {
                user_password.error = "Password too short"
            } else {
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult?>() {
                        fun onComplete(task: Task<AuthResult?>) {
                            if (task.isSuccessful()) {
                                Toast.makeText(
                                    activity,
                                    "Registration successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                sendemailverification()
                            }
                        }
                    })
            }
        }
        return v
    }

    private fun sendemailverification() {
        val firebaseUser: FirebaseUser = firebaseAuth.getCurrentUser()
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification()
                .addOnCompleteListener(object : OnCompleteListener<Void?>() {
                    fun onComplete(task: Task<Void?>) {
                        Toast.makeText(activity, "email sent", Toast.LENGTH_SHORT).show()
                        firebaseAuth.signOut()
                        (activity as authenticationpage?)!!.loginFragmentManager()
                    }
                })
        } else {
            Toast.makeText(activity, "Registration failed", Toast.LENGTH_SHORT).show()
            (activity as authenticationpage?)!!.loginFragmentManager()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}