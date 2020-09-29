package com.example.authenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private EditText signup_password, signup_email, signup_name;
    private Button signup_button;
    private TextView signinClick;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Sign Up");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_register_user);

        progressBar1 = findViewById(R.id.progressBar1);
        signup_name = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_button = findViewById(R.id.signup_button);
        signinClick = findViewById(R.id.signinClick);

        signinClick.setOnClickListener(this);
        signup_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_button:
                userRegister();

                break;

            case R.id.signinClick:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }


    }


    private void userRegister() {
        String name = signup_name.getText().toString().trim();
        String email = signup_email.getText().toString().trim();  // dui line e duita variable email ar password ta pass korar jonno
        String password = signup_password.getText().toString().trim();

        // checking name is given or not...
        if(name.isEmpty()){
            signup_name.setError("Enter a Name");
            signup_name.requestFocus();
            return;
        }

        //checking the validity of the email
        if(email.isEmpty())
        {
            signup_email.setError("Enter an email address");
            signup_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signup_email.setError("Enter a valid email address");
            signup_email.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            signup_password.setError("Enter a password");
            signup_password.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            signup_password.setError("Minimum length of a passowrd should be 6 characters");
            signup_password.requestFocus();
            return;
        }
        progressBar1.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar1.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterUser.this, "Registration is successsful", Toast.LENGTH_SHORT).show();
//                            finish();
//                            Intent intent = new Intent(getApplicationContext(),Menu.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(),"User is already registered!", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(RegisterUser.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }


                    }
                });

    }
}
