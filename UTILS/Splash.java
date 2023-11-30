package com.example.quanlibenhvien.UTILS;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlibenhvien.ACTIVITIES.LoginAC;
import com.example.quanlibenhvien.R;
public class Splash extends AppCompatActivity {
    private static final long SPLASH_DELAY = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asplash);
        init();
    }
    private void init(){
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(Splash.this, LoginAC.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DELAY);
    } // chạy rồi mở trang đăng nhập
}