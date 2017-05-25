package com.entersnowman.internetshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.simplify.android.sdk.CardEditor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardPayActivity extends AppCompatActivity {
    @BindView(R.id.pay_button)
    Button payButton;
    @BindView(R.id.card_editor)
    CardEditor cardEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_pay);
        ButterKnife.bind(this);
        cardEditor.addOnStateChangedListener(new CardEditor.OnStateChangedListener() {
            @Override
            public void onStateChange(CardEditor cardEditor) {
                payButton.setEnabled(cardEditor.isValid());
            }
        });
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
