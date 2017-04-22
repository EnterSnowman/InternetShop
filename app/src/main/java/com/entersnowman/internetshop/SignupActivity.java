package com.entersnowman.internetshop;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    @BindView(R.id.link_login) TextView login;
    @BindView(R.id.input_email) EditText inputEmail;
    @BindView(R.id.input_password) EditText inputPassword;
    @BindView(R.id.input_name) EditText inputName;
    @BindView(R.id.btn_signup)    Button signupButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,MainActivity.class));
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(inputEmail.getText().toString(), inputPassword.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(MainActivity.TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                if (task.isSuccessful()){
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.sendEmailVerification();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(inputName.getText().toString())
                                            .build();
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(MainActivity.TAG, "User profile updated.");
                                                    }
                                                }
                                            });
                                    startActivity(new Intent(SignupActivity.this,TmpActivity.class));
                                    finish();
                                }

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

        });
    }

    public boolean validate() {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inputPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }
}
