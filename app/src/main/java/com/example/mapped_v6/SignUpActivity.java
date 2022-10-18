package com.example.mapped_v6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView login;
    private Button register;
    private CheckBox checktnc;
    private static final String TAG = "EmailPassword";

    EditText Name;
    EditText Surname;
    EditText Email;
    EditText Password;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth=FirebaseAuth.getInstance();

        Name=findViewById(R.id.editTextTextPersonName);
        Surname=findViewById(R.id.editTextTextPersonName2);
        Email=findViewById(R.id.editTextTextEmailAddress);
        Password=findViewById(R.id.editTextTextPassword);

        login=(TextView) findViewById(R.id.btnSignIn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignIn();
            }
        });


        checktnc=findViewById(R.id.checkBox);

        register=(Button) findViewById(R.id.btnContinue);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });











    }

    private void validation() {
        if(checktnc.isChecked()){
            createAccount();

        }
        else{
            Toast.makeText(this, "Please Agree to T&Cs", Toast.LENGTH_SHORT).show();
        }

    }

    private void createAccount() {

        String email1=Email.getText().toString();
        String password1 = Password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email1, password1)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(SignUpActivity.this, "Please Navigate to Settings Page before using the application ", Toast.LENGTH_LONG).show();
                            openLandingPage();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    private void openSettings() {


    }

    private void openLandingPage() {
        Intent intent1 = new Intent (SignUpActivity.this, navActivity.class);
        startActivity(intent1);
    }

    private void updateUI(Object o) {
    }

    private void openSignIn() {
        Intent intent = new Intent(SignUpActivity.this, LoginAcivity.class);
        startActivity(intent);

    }
}
