package com.example.quanlibenhvien.ACTIVITIES;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.R;

import java.sql.ResultSet;

public class LoginAC extends AppCompatActivity {
    private Button btn1, btn2;
    private CheckBox cb1;
    private EditText ed1,ed2;
    private String susername,spassword;
    private int Check;
    public static String username;
    private Connect connect;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alogin);

        mapping();
        init();
        setup();
        setClick();
    }
    private void init(){
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        String savedUsername = pref.getString("username", "");
        String savedPassword = pref.getString("password", "");
        if(!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            ed1.setText(savedUsername);
            ed2.setText(savedPassword);
        }
    }  // đọc tài khoản từng đăng nhập
    private void setup(){
        connect = new Connect();
    }  // tạo kết nối mới
    private void setClick() {
        btn1.setOnClickListener(v -> getLogin());
        btn2.setOnClickListener(v -> getCall());
        cb1.setOnClickListener(v -> showPass());
    }        // bấm vào
    private void getCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0917833552"));
        startActivity(intent);
    }           // gọi điện
    private void showPass() {
        if (cb1.isChecked()) {
            ed2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ed2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }           // hiện thị mật khẩu
    private void getLogin(){
       checkLogin();
        if(Check == 1){
            login1();
        }
        else if(Check == 2){
            login2();
        }
        else if(Check == 3){
            login3();
        } else {
        }
    }           //  đăng nhập
    private void checkLogin(){
        susername = ed1.getText().toString().trim();
        spassword = ed2.getText().toString().trim();
        if (susername == null || susername.equals("")){
            Toast.makeText(this, getResources().getString(R.string.pls_username), Toast.LENGTH_SHORT).show();
                 return;
        }
         if (spassword == null || spassword.equals("")){
            Toast.makeText(this, getResources().getString(R.string.pls_pass), Toast.LENGTH_SHORT).show();
             return;
         }
        try {
            connect = new Connect();
            ResultSet rs = connect.read("EXEC LoginGetPer '" + susername + "','" + spassword + "' "
            );
            if( rs!= null) {
                while (rs.next()) {
                    Check = rs.getInt("per");
                    if(Check !=0 ){  username = susername;
                        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", susername);
                        editor.putString("password", spassword);
                        editor.apply();
                    }
                    else {
                        Toast.makeText(this, getResources().getString(R.string.err_login), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            connect .closeConnection();
        }
    }       // kiểm tra đăng nhập
    private void login3() {
        Intent intent = new Intent(this, PatientAC.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }       //  bệnh nhân đăng nhập
    private void login2(){
        Intent intent = new Intent(this, PharmaAC.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }       // dược sĩ đăng nhập
    private void login1() {
        Intent intent = new Intent(this, DoctorAC.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }   // bác sĩ đăng nhập
    private void mapping() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        cb1 = findViewById(R.id.cb1);
        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
    } // ánh xạ  view

}
