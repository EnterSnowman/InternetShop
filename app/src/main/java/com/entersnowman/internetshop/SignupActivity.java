package com.entersnowman.internetshop;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    @BindView(R.id.link_login) TextView login;
    @BindView(R.id.input_email) EditText inputEmail;
    @BindView(R.id.input_password) EditText inputPassword;
    @BindView(R.id.input_name) EditText inputName;
    @BindView(R.id.btn_signup)    Button signupButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.left_in,R.anim.right_out);
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(inputEmail.getText().toString(), inputPassword.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(LoginActivity.TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
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
                                                        Log.d(LoginActivity.TAG, "User profile updated.");
                                                    }
                                                }
                                            });
                                    mDatabase.child(user.getUid()).child("basket").setValue("empty");
                                    mDatabase.child(user.getUid()).child("favorite").setValue("empty");
                                    startActivity(new Intent(SignupActivity.this,GeneralActivity.class));
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
