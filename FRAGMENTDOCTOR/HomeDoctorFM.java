package com.example.quanlibenhvien.FRAGMENTDOCTOR;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlibenhvien.ACTIVITIES.LoginAC;
import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.R;

import java.sql.ResultSet;
import java.util.ArrayList;
public class HomeDoctorFM extends Fragment {
    private TextView tv1,tv2;
    private  String a,b;
    private Connect conn;
    private Handler handler;
    private Runnable runnable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_doctor, container, false);
        mapping(view);
        init();
        return view;
    }


    private void startDataLoading() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                fillData(LoginAC.username.toString().trim());
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void stopDataLoading() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopDataLoading();
    }
    private void init(){
        startDataLoading();
    }  // lấy số lượng bệnh nhân
    private void fillData(String sid){
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  [GetRowCountByStatus] '" + sid +"'"
            );
            if( rs!= null) {
                while (rs.next()) {
                    b = rs.getString("b1");
                    a = rs.getString("total");
                    setup();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
    } // hàm lấy số lượng bệnh nhân
    private  void setup(){
        tv1.setText(a);
        tv2.setText(b);
    }  // hiện thị số lượng bệnh nhân
    private void mapping(View view) {
        tv1 =  view.findViewById(R.id.tv1);
        tv2 =  view.findViewById(R.id.tv2);
    }  // ánh xạ dữ liệu
}
