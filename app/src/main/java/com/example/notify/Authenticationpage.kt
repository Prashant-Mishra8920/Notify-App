package com.example.notify

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.notify.databinding.ActivityAuthenticationpageBinding
import com.google.firebase.auth.FirebaseAuth

class authenticationpage : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationpageBinding
    private val email: String? = null
    private val password: String? = null
    var login_layout: LinearLayout? = null
    var sign_layout: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticationpage)
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user_email_address = findViewById<EditText>(R.id.user_email_address)
        val user_password = findViewById<EditText>(R.id.user_password)

        binding.loginLinearLayout.setVisibility(View.GONE)
        loginFragmentManager()
        binding.loginSelector.setOnClickListener(View.OnClickListener {
            loginFragmentManager()
            binding.loginLinearLayout.visibility = View.GONE
            binding.signLinearLayout.visibility = View.VISIBLE
        })
        binding.signSelector.setOnClickListener(View.OnClickListener {
            signFragmentManager()
            binding.signLinearLayout.visibility = View.GONE
            binding.loginLinearLayout.visibility = View.VISIBLE
        })
    }

    fun loginFragmentManager() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
        fragmentTransaction.replace(R.id.authframe, LoginFragment(), "h")
        fragmentTransaction.commit()
        binding.loginSelector.setTypeface(null, Typeface.BOLD)
        binding.signSelector.setTypeface(null, Typeface.NORMAL)
    }

    fun signFragmentManager() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit)
        fragmentTransaction.replace(R.id.authframe, SignFragment(), "h")
        fragmentTransaction.commit()
        binding.signSelector.setTypeface(null, Typeface.BOLD)
        binding.loginSelector.setTypeface(null, Typeface.NORMAL)
    }

    fun forgetFragmentManager() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.authframe, forgetFragment())
        fragmentTransaction.commit()
    }
}