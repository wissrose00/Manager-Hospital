package com.example.quanlibenhvien.ACTIVITIES;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.FRAGMENTDOCTOR.HomeDoctorFM;
import com.example.quanlibenhvien.FRAGMENTDOCTOR.ListPatientFM;
import com.example.quanlibenhvien.FRAGMENTDOCTOR.SickFM;
import com.example.quanlibenhvien.R;
import com.example.quanlibenhvien.UTILS.ChangePassFM;
import com.example.quanlibenhvien.UTILS.InfoFM;
import com.example.quanlibenhvien.UTILS.ScheduleFM;
import com.google.android.material.navigation.NavigationView;

import java.sql.ResultSet;
public class DoctorAC extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener{
    private Connect connect;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private  String username;
    private  View headerView;
    private TextView idTextView,usernameTextView;
    private static final int FM_HOME = 0;
    private static final int FM_SCHEDULE = 1;
    private static final int FM_PATIENTS = 2;
    private static final int FM_SICK = 3;
    private static final int FM_INFO = 4;
    private static final int FM_CHANGEPASS = 5;
    private int mCurrentFM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amain_doctor);
        mapping();
        init();
        setup();
    }
    private void init() {
        repalceFM(new HomeDoctorFM());
        navigationView.getMenu().findItem(R.id.nav_1).setChecked(true);
        mCurrentFM = FM_HOME;
        username = LoginAC.username.toString();
        headerView = navigationView.getHeaderView(0);
        idTextView = headerView.findViewById(R.id.tv1);
        usernameTextView = headerView.findViewById(R.id.tv2);
        idTextView.setText(username.toUpperCase());
        usernameTextView.setText(getName(username.toUpperCase()));
    }    // cài đặt chung
    private String getName(String id){
        String name = "";
            try {
                connect = new Connect();
                ResultSet rs = connect.read("EXEC getUserNameById @id = '"+id+"' "
                );
                if( rs!= null) {
                    while (rs.next()) {
                        name = rs.getString("name");
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            finally {
                connect .closeConnection();
            }
        return name;
    }  // lấy tên từ id
    private void repalceFM(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameMain, fragment);
        fragmentTransaction.commit();
    }    // đổi chỗ fragment
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    } // đóng mở toolbar - quay lại
    private void setup() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navi_drawer_open, R.string.navi_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }  // cài đặt toolbar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_1) {
            if (mCurrentFM != FM_HOME) {
                repalceFM(new HomeDoctorFM());
                mCurrentFM = FM_HOME;
            }
        }
        else if (id == R.id.nav_2) {
            if (mCurrentFM != FM_SCHEDULE) {
                repalceFM(new ScheduleFM());
                mCurrentFM = FM_SCHEDULE;
            }}
        else if (id == R.id.nav_3) {
            if (mCurrentFM != FM_PATIENTS) {
                repalceFM(new ListPatientFM());
                mCurrentFM = FM_PATIENTS;
            }}
        else if (id == R.id.nav_4) {
            if (mCurrentFM != FM_SICK) {
                repalceFM(new SickFM());
                mCurrentFM = FM_SICK;
            }}
        else if (id == R.id.nav_5) {
            if (mCurrentFM != FM_INFO) {
                repalceFM(new InfoFM());
                mCurrentFM = FM_INFO;
            }}
        else if (id == R.id.nav_6) {
            if (mCurrentFM != FM_CHANGEPASS) {
                repalceFM(new ChangePassFM());
                mCurrentFM = FM_CHANGEPASS;
            }
        }
        else if (id == R.id.nav_7) {
            Intent loginIntent = new Intent(this, LoginAC.class);
            startActivity(loginIntent);
            finish();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }  // nhấn vào item trong menu
    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.naviview);
    }  // ánh xạ view
}
