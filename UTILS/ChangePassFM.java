package com.example.quanlibenhvien.UTILS;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlibenhvien.ACTIVITIES.LoginAC;
import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.R;

import java.sql.ResultSet;
public class ChangePassFM  extends Fragment {
    private Connect connect;
    private Button btn1;
    private CheckBox cb1;
    private EditText passcur,newpass,renewpass;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.changepass, container, false);
        setup();
        mapping(view);
        setClick();
        return  view;
    }
    private void setup(){
        context = getContext();
    } // cài đặt
    private void setClick() {
        btn1.setOnClickListener(v -> {
            try {
                changePass();
            }
            catch (Exception e) {
                Toast.makeText(context, getResources().getString(R.string.err), Toast.LENGTH_SHORT).show();
            }
        });
        cb1.setOnClickListener(v -> showPass());
    } // bấm vào
    private void showPass() {
        if (cb1.isChecked()) {
            passcur.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            newpass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            renewpass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            passcur.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            newpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            renewpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    } // hiện mật khẩu
    private void changePass() {
        String username = LoginAC.username.toString().toUpperCase();
        String passcurr = passcur.getText().toString().trim();
        String newpass1 = newpass.getText().toString().trim();
        String renwpass = renewpass.getText().toString().trim();
        if (passcurr.isEmpty()) {
            Toast.makeText(context, getResources().getString(R.string.pls_pass_cur), Toast.LENGTH_SHORT).show();
            return;
        }
        if (newpass1.isEmpty() ) {
            Toast.makeText(context, getResources().getString(R.string.pls_pass_new), Toast.LENGTH_SHORT).show();
            return;
        }
        if (newpass1.equals(passcurr)) {
            Toast.makeText(context, getResources().getString(R.string.err_different_passcurr), Toast.LENGTH_SHORT).show();
            return;
        }if (renwpass.isEmpty() ) {
            Toast.makeText(context, getResources().getString(R.string.pls_pass_renew), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!renwpass.equals(newpass1)) {
            Toast.makeText(context, getResources().getString(R.string.err_different_pass), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            connect = new Connect();
            ResultSet rs = connect.read("EXEC changePassword  '"+username+"','"+passcurr+"','"+renwpass+"' "
            );
            if( rs!= null) {
                while (rs.next()) {
                   int check = rs.getInt("stt");
                   if(check == 0){
                       Toast.makeText(context, getResources().getString(R.string.changepassok), Toast.LENGTH_SHORT).show();
                       passcur.setText(null);
                       newpass.setText(null);
                       renewpass.setText(null);
                   }
                   else {
                       Toast.makeText(context, getResources().getString(R.string.changepasserr), Toast.LENGTH_SHORT).show();
                   }
                }
            }
        } catch (Exception exception) {
            Toast.makeText(context, getResources().getString(R.string.err), Toast.LENGTH_SHORT).show();
        }
        finally {
            connect .closeConnection();
        }
    } // đổi mật khẩu
    private void mapping(View view){
        passcur = view.findViewById(R.id.ed1);
        newpass = view.findViewById(R.id.ed2);
        renewpass = view.findViewById(R.id.ed3);
        btn1 = view.findViewById(R.id.btn1);
        cb1 = view.findViewById(R.id.cb1);
    } // ánh xạ
}
