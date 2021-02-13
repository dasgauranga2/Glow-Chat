package com.example.glowchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EmailValidator {
    // Email Regex java
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    // static Pattern object, since pattern is fixed
    private static Pattern pattern;

    // non-static Matcher object because it's created from the input String
    private Matcher matcher;

    public EmailValidator() {
        // initialize the Pattern object
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
    }

    /**
     * This method validates the input email address with EMAIL_REGEX pattern
     *
     * @param email
     * @return boolean
     */
    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

public class SignupActivity extends AppCompatActivity {

    EditText email,password;
    Button signup;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.emailSignupInput);
        password = findViewById(R.id.passwordSignupInput);
        signup = findViewById(R.id.signupConfirmButton);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the email entered by the user
                String email_text = email.getText().toString();
                // get the password entered by the user
                String password_text = password.getText().toString();

                // check if email and password is empty or not
                if (email_text.length() > 0 && password_text.length() > 0) {
                    EmailValidator ev = new EmailValidator();
                    // validate the email
                    if (!ev.validateEmail(email_text)) {
                        Toast.makeText(SignupActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    } // validate the password
                    else if (!password_text.matches(".*[a-zA-Z]+.*") || !password_text.matches(".*[0-9]+.*")) {
                        Toast.makeText(SignupActivity.this, "Password must contain both letters and numbers", Toast.LENGTH_SHORT).show();
                    } // register the user
                    else {
                        set_username(email_text);
                        set_avatar(email_text);
                        register_user(email_text,password_text);

                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(SignupActivity.this, "Email/Password field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // function to register the user
    public void register_user(String e, String p) {

        auth = FirebaseAuth.getInstance();
        // create the user using email and password
        auth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // check if user signup is successful
                if (task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "USER SIGNUP SUCCESS", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignupActivity.this, "USER SIGNUP FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // set the default username of the user
    public void set_username(String email) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference username_ref = database.getReference("USERNAMES");
        String default_un = email.split("@")[0];
        username_ref.child(default_un).setValue(default_un);
        //Toast.makeText(SignupActivity.this, "DEFAULT USERNAME SET", Toast.LENGTH_SHORT).show();
    }

    // set the default avatar of the user
    public void set_avatar(String email) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference avatar_ref = database.getReference("AVATARS");
        String default_un = email.split("@")[0];
        avatar_ref.child(default_un).setValue("default_avatar");
        //Toast.makeText(SignupActivity.this, "DEFAULT USERNAME SET", Toast.LENGTH_SHORT).show();
    }
}