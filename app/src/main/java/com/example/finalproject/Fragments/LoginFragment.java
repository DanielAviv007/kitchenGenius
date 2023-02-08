package com.example.finalproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.FeedActivity;
import com.example.finalproject.R;
import com.example.finalproject.Storage.SystemStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    FirebaseAuth mAuth;
    EditText emailEditText;
    EditText passwordEditText;
    Button login;
    TextView register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = view.findViewById(R.id.loginEmail);
        passwordEditText = view.findViewById(R.id.loginPassword);

        login = view.findViewById(R.id.loginBtn);
        register = view.findViewById(R.id.toRegisterText);

        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null && getArguments().getStringArray("credentials") != null) {
            emailEditText.setText(getArguments().getStringArray("credentials")[0]);
            passwordEditText.setText(getArguments().getStringArray("credentials")[1]);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (validateInfo(email, password)) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String currentUID = task.getResult().getUser().getUid();

                                        SystemStorage.setCurrentUID(currentUID);

                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), FeedActivity.class);
                                        getActivity().startActivity(intent);
                                    } else {
                                        Toast.makeText(getActivity(),"Email and password do not match.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
            public boolean validateInfo(String email, String password) {
                boolean isValid = false;

                if ((email == null && password == null) || (email.isEmpty() && password.isEmpty()))
                    Toast.makeText(getActivity(),"Please enter your email, and password.",Toast.LENGTH_SHORT).show();
                else if (email == null || email.isEmpty())
                    Toast.makeText(getActivity(),"Please enter your email address",Toast.LENGTH_SHORT).show();
                else if (password == null || password.isEmpty())
                    Toast.makeText(getActivity(),"Please enter your password",Toast.LENGTH_SHORT).show();
                else
                    isValid = true;

                return isValid;
            }
        });

        register.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment));

        return view;
    }
}