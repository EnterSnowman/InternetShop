package com.entersnowman.internetshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TmpActivity extends AppCompatActivity {
    @BindView(R.id.logout) Button logoutButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                if (mAuth.getCurrentUser()==null){
                    Log.d(LoginActivity.TAG, "No current user");
                    startActivity(new Intent(TmpActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }
}
