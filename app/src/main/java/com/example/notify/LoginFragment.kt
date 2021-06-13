package com.example.notify

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginFragment : Fragment() {
    private var firebaseAuth: FirebaseAuth? = null
    private var forget_selector: TextView? = null
    private var login_button: Button? = null
    private val sign_button: Button? = null
    var login_layout: RelativeLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.login_fragment, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth!!.getCurrentUser()
        if (firebaseUser != null) {
            (activity as authenticationpage?)!!.finish()
            startActivity(Intent(activity, Notes::class.java))
        }
        val user_email_address = v.findViewById<View>(R.id.user_email_address) as TextInputEditText
        val user_password = v.findViewById<View>(R.id.user_password) as TextInputEditText
        forget_selector = v.findViewById<View>(R.id.forget_password_selector) as TextView
        forget_selector!!.setOnClickListener { (activity as authenticationpage?)!!.forgetFragmentManager() }
        login_button = v.findViewById<View>(R.id.login_button) as Button
        login_button!!.setOnClickListener {
            val email = user_email_address.text.toString()
            val pass = user_password.text.toString()
            if (email.isEmpty()) {
                user_email_address.error = "Email Required"
            }
            if (pass.isEmpty()) {
                user_password.error = "Password Required"
            } else {
                firebaseAuth!!.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            checkaccountverification()
                        } else {
                            Toast.makeText(activity, "Account don't exist", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
        return v
    }

    private fun checkaccountverification() {
        val firebaseUser: FirebaseUser? = firebaseAuth?.getCurrentUser()
        if (firebaseUser != null) {
            if (firebaseUser.isEmailVerified() === true) {
                Toast.makeText(activity, "logged in ", Toast.LENGTH_SHORT).show()
                (activity as authenticationpage?)!!.finish()
                startActivity(Intent(activity, Notes::class.java))
            } else {
                Toast.makeText(activity, "Account not verified", Toast.LENGTH_SHORT).show()
                firebaseAuth?.signOut()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}