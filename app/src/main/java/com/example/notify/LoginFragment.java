package com.example.notify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private TextView forget_selector;
    private Button login_button,sign_button;
    RelativeLayout login_layout;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.login_fragment,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            ((authenticationpage)getActivity()).finish();
            startActivity(new Intent(getActivity(),Notes.class));
        }

        TextInputEditText user_email_address = (TextInputEditText) v.findViewById(R.id.user_email_address);
        TextInputEditText user_password = (TextInputEditText) v.findViewById(R.id.user_password);

        forget_selector = (TextView) v.findViewById(R.id.forget_password_selector);
        forget_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((authenticationpage)getActivity()).forgetFragmentManager();
            }
        });




        login_button= (Button) v.findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email_address.getText().toString();
                String pass = user_password.getText().toString();
                if(email.isEmpty()){
                    user_email_address.setError("Email Required");
                }
                if(pass.isEmpty()){
                    user_password.setError("Password Required");
                }
                else{
                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkaccountverification();
                            }
                            else{
                                Toast.makeText(getActivity(),"Account don't exist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        return v;
    }


    private void checkaccountverification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified() == true){
            Toast.makeText(getActivity(),"logged in ",Toast.LENGTH_SHORT).show();
            ((authenticationpage)getActivity()).finish();
            startActivity(new Intent(getActivity(),Notes.class));
        }
        else{
            Toast.makeText(getActivity(),"Account not verified",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
