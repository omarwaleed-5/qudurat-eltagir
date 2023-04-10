package com.eltagir.qudurat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.eltagir.qudurat.Constant;
import com.eltagir.qudurat.R;
import com.eltagir.qudurat.helper.ApiConfig;
import com.eltagir.qudurat.helper.Session;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InviteFriendActivity extends AppCompatActivity {

    TextView txtrefercoin, txtcode, txtcopy, txtinvite;
    Toolbar toolbar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_frnd);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.refer_amp_earn));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtrefercoin = findViewById(R.id.txtrefercoin);
        txtrefercoin.setText(getString(R.string.refer_message_1) + Constant.EARN_COIN_VALUE + getString(R.string.refer_message_2) + Constant.REFER_COIN_VALUE + getString(R.string.refer_message_3));
        txtcode = findViewById(R.id.txtcode);
        txtcopy = findViewById(R.id.txtcopy);
        txtinvite = findViewById(R.id.txtinvite);
        if (Session.getUserData(Session.REFER_CODE, getApplicationContext()) == null) {
            GetReferCode();
        } else {
            txtcode.setText(Session.getUserData(Session.REFER_CODE, getApplicationContext()));
        }
        txtcopy.setOnClickListener(v -> {

            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", txtcode.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(InviteFriendActivity.this, R.string.refer_code_copied, Toast.LENGTH_SHORT).show();
        });
    }

    public void GetReferCode() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.GET_USER_BY_ID, "1");
        params.put(Constant.ID, Session.getUserData(Session.USER_ID, InviteFriendActivity.this));
        ApiConfig.RequestToVolley((result, response) -> {

            if (result) {
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean error = obj.getBoolean("error");
                    if (!error) {
                        JSONObject jsonobj = obj.getJSONObject("data");
                        txtcode.setText(jsonobj.getString("refer_code"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, params);

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void OnInviteFrdClick(View view) {
        if (!txtcode.getText().toString().equals("code")) {
            try {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.refer_share_msg_1) + getResources().getString(R.string.app_name) + getString(R.string.refer_share_msg_2) + "\n\" " + txtcode.getText().toString() + " \"\n\n" + Constant.APP_LINK);
                startActivity(Intent.createChooser(sharingIntent, "Invite Friend Using"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.refer_code_generate_error_msg), Toast.LENGTH_SHORT).show();
        }
    }
}
