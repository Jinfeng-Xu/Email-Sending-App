package com.example.email_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ReadActivity extends AppCompatActivity{

    private TextView tvTest, tvFrom, tvTo, tvCC, tvSubject, tvContent;
    private SharedPreferences mSharedPreferences, mSharedCount;
    private EditText etNumber;
    private Button btnBack, btnConfirm;
    private int max;
    private SharedPreferences.Editor mEditor, mCountEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        tvTest = findViewById(R.id.tv_test);
        tvFrom = findViewById(R.id.tv_from);
        tvTo = findViewById(R.id.tv_to);
        tvCC = findViewById(R.id.tv_cc);
        tvSubject = findViewById(R.id.tv_subject);
        tvContent = findViewById(R.id.tv_content);
        btnBack = findViewById(R.id.btn_back);
        btnConfirm = findViewById(R.id.btn_confirm);
        etNumber = findViewById(R.id.et_number);
        mSharedCount = getSharedPreferences("count", MODE_PRIVATE);
        mCountEditor = mSharedCount.edit();
        max = Integer.parseInt(mSharedCount.getString("SaveCount", String.valueOf(1)));
        tvTest.setText("You have sent " +  (Integer.parseInt(mSharedCount.getString("SaveCount", String.valueOf(1)))-1) + " emails. " +
                "Which one do you want to check");
        setListener();
        setTitle("Email Reader");
    }

    private void setListener(){
        Onclick onclick = new Onclick();
        btnBack.setOnClickListener(onclick);
        btnConfirm.setOnClickListener(onclick);
    }

    private class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch(view.getId()) {
                case R.id.btn_back:
                    intent = new Intent(ReadActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_confirm:
                    Log.d("confirm", "onClick: ");
                    showInfo();
                    break;
            }
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void showInfo(){
        if (Integer.parseInt(etNumber.getText().toString()) > max) {
            Toast.makeText(ReadActivity.this, "Wrong Input", Toast.LENGTH_SHORT).show();
        }
        else {
            int count = Integer.parseInt(etNumber.getText().toString());
            mSharedPreferences = getSharedPreferences("data_" + count, MODE_PRIVATE);
            tvFrom.setText(mSharedPreferences.getString("From", null));
            tvTo.setText(mSharedPreferences.getString("To", null));
            tvCC.setText(mSharedPreferences.getString("CC", null));
            tvSubject.setText(mSharedPreferences.getString("Subject", null));
            tvContent.setText(mSharedPreferences.getString("Content", null));
        }

    }


}