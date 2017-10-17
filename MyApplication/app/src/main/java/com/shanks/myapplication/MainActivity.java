package com.shanks.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn_login,btn_clean;
    EditText edt_account, edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_account = (EditText) findViewById(R.id.edt_account);
        edt_password = (EditText) findViewById(R.id.edt_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = edt_account.getText().toString();
                String password = edt_password.getText().toString();
                if (account.equals("shanks") && account != null && password.equals("123456") && password != null) {
                    Intent intent = new Intent(MainActivity.this, SecActivity.class);
                    intent.putExtra("data", "Hello,SecActivity!");
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "帐号或密码不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_clean = (Button) findViewById(R.id.btn_clean);
        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_account.setText("");
                edt_password.setText("");
            }
        });
    }
}
