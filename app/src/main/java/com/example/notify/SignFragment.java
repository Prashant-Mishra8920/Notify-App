package com.example.notify;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    private Button sign_button;
    RelativeLayout parent_layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.signup_fragment,container,false);

        firebaseAuth = FirebaseAuth.getInstance();

        TextInputEditText user_email_address = (TextInputEditText) v.findViewById(R.id.user_email_address);
        TextInputEditText user_password = (TextInputEditText) v.findViewById(R.id.user_password);
        TextInputEditText confirm_password = (TextInputEditText) v.findViewById(R.id.confirm_password);

        parent_layout = (RelativeLayout) v.findViewById(R.id.sign_parent_layout);

        sign_button= (Button) v.findViewById(R.id.sign_button);
        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email_address.getText().toString();
                String pass = user_password.getText().toString();
                String confirm_pass = confirm_password.getText().toString();
                if(email.isEmpty()){
                    user_email_address.setError("Email Required");
                }
                if(pass.isEmpty()){
                    user_password.setError("Password Required");
                }
                if(!pass.equals(confirm_pass)){
                    confirm_password.setError("Wrong input");
                }
                if(confirm_pass.isEmpty()){
                    confirm_password.setError("Confirm password");
                }
                else if(pass.length()<8){
                    user_password.setError("Password too short");
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
                                sendemailverification();
                            }
                        }
                    });
                }
            }
        });

        return v;
    }



    private void sendemailverification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getActivity(),"email sent",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    ((authenticationpage)getActivity()).loginFragmentManager();
                }
            });
        }
        else{
            Toast.makeText(getActivity(),"Registration failed",Toast.LENGTH_SHORT).show();
            ((authenticationpage)getActivity()).loginFragmentManager();
        }
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
