package com.example.finalproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.Models.User;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {
    FirebaseAuth mAuth;
    EditText emailEditText;
    EditText password1EditText;
    EditText password2EditText;
    Button registerBtn;
    Button backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = view.findViewById(R.id.registerEmail);
        password1EditText = view.findViewById(R.id.registerPassword1);
        password2EditText = view.findViewById(R.id.registerPassword2);

        registerBtn = view.findViewById(R.id.registerBtn);
        backBtn = view.findViewById(R.id.backToLoginBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password1 = password1EditText.getText().toString().trim();
                String password2 = password2EditText.getText().toString().trim();

                if (validateInfo(email, password1, password2))
                    mAuth.createUserWithEmailAndPassword(email, password1)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        insertUserRealTime(new User(email, task.getResult().getUser().getUid()));

                                        Bundle bundle = new Bundle();
                                        bundle.putStringArray("credentials", new String[]{email, password1});

                                        Toast.makeText(getActivity(),"Register ok",Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment, bundle);
                                    } else {
                                        Toast.makeText(getActivity(),"Email address is already registered. Please try logging in.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }

            private boolean validateInfo(String email, String password1, String password2) {
                if (email == null || password1 == null || password2 == null ||
                        email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(getActivity(),"Please enter your email address, and the two passwords' fields.",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!password1.equals(password2)) {
                    Toast.makeText(getActivity(),"Passwords do not match. Please try again.",Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
            public void insertUserRealTime(User user) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users").child(user.getUid());

                myRef.setValue(user);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
        return view;
    }
}