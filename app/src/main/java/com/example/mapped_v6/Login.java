package com.example.mapped_v6;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment  {

    private FirebaseAuth mAuth;
    private String email;
    private String password;
    EditText Email;
    EditText Password;
    View v;
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+");


    public Login() {
        // Required empty public constructor
    }

    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        if (getArguments() != null) {

        }
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


         v =inflater.inflate(R.layout.fragment_login,container,false);
        Button demobutton = (Button) v.findViewById(R.id.demobutton);
        Button btnLogIn=(Button) v.findViewById(R.id.btnLogIn);
        TextView Signup =(TextView)v.findViewById(R.id.textSignup);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignup();
            }
        });



        demobutton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                openLandingPage();
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });

        return v;




    }

    private void launchSignup() {
        Intent intent = new Intent(getContext(), Registration.class);
        startActivity(intent);

    }






    public void signIn(){



        Email= v.findViewById(R.id.txtUsername);
        Password=v.findViewById(R.id.txtPassword);




       mAuth = FirebaseAuth.getInstance();


      email=Email.getText().toString();
      password=Password.getText().toString();

      mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.d(TAG,"signInWithEmail:success");
                    Toast.makeText(getActivity(), "22222222", Toast.LENGTH_SHORT).show();
                    FirebaseUser user=mAuth.getCurrentUser();
                    updateUI(user);
                    openLandingPage();

                }
                else{
                    Log.w(TAG,"SignWithEmail:Failure",task.getException());// showToast( );
                    Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });



    }



    private void openLandingPage() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void updateUI(FirebaseUser user) {
    }

}
