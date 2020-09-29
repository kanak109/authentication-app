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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signin_password, signin_email;
    private Button loginbutton;
    private TextView signupClick;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Sign In");

        mAuth = FirebaseAuth.getInstance();

        signin_email = findViewById(R.id.signin_email);
        signin_password = findViewById(R.id.signin_password);
        loginbutton = findViewById(R.id.loginbutton);
        signupClick = findViewById(R.id.signupClick);
        progressBar = findViewById(R.id.progressBar);

        signupClick.setOnClickListener(this);
        loginbutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginbutton:
                userLogin();
                break;

            case R.id.signupClick:
                Intent intent = new Intent(getApplicationContext(),RegisterUser.class);
                startActivity(intent);
                break;
        }


    }

    private void userLogin() {

        String email = signin_email.getText().toString().trim();  // dui line e duita variable email ar password ta pass korar jonno
        String password = signin_password.getText().toString().trim();


        //checking the validity of the email
        if(email.isEmpty())
        {
            signin_email.setError("Enter an email address");
            signin_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signin_email.setError("Enter a valid email address");
            signin_email.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            signin_password.setError("Enter a password");
            signin_password.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            signin_password.setError("Minimum length of a passowrd should be 6 characters");
            signin_password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(getApplicationContext(),Menu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
