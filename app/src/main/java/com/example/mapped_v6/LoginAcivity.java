package com.example.mapped_v6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginAcivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email;
    private String password;
    EditText Email;
    EditText Password;
    TextView login;
    Button demobtn;
    Button btnLogIn;
    private String TAG="EmailPassword";
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+");








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acivity);

        mAuth=FirebaseAuth.getInstance();
        Email=findViewById(R.id.txtUsername);
        Password=findViewById(R.id.txtPassword);


        login=findViewById(R.id.textSignup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent (LoginAcivity.this, SignUpActivity.class);
                startActivity(intent1);
            }
        });

        btnLogIn=findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });




    }

    private void signIn() {
        email=Email.getText().toString().trim();
        password=Password.getText().toString().trim();


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginAcivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"signInComplete");
                    FirebaseUser user=mAuth.getCurrentUser();
                    updateUI(user);

                    openLandingPage();
                } else{
                    Log.w(TAG,"signInFAILED",task.getException());
                    Toast.makeText(LoginAcivity.this, "signInFAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    private void openLandingPage() {


        Intent intent1 = new Intent (LoginAcivity.this, navActivity.class);
        startActivity(intent1);
    }

    private void updateUI(FirebaseUser user) {
    }
}
