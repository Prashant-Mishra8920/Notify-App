package com.example.notify;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class forgetFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    private EditText email;
    private Button recover;
    RelativeLayout forget_layout;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forget_password_fragment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        email = (EditText) v.findViewById(R.id.user_email_address);
        recover = (Button) v.findViewById(R.id.recover_button);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = email.getText().toString();
                if(email_txt.isEmpty()){
                    email.setError("Email required");
                }
                else{
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    if(firebaseUser != null){
                        Toast.makeText(getActivity(),"User not found",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        firebaseAuth.sendPasswordResetEmail(email_txt).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(),"Email sent",Toast.LENGTH_SHORT).show();
                                    ((authenticationpage)getActivity()).loginFragmentManager();
                                }
                                else {
                                    Toast.makeText(getActivity(),"User not found",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            }
        });

        forget_layout = (RelativeLayout) v.findViewById(R.id.forget_parent_layout);


        return v;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
