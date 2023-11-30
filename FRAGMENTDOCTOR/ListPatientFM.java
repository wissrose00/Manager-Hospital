package com.example.quanlibenhvien.FRAGMENTDOCTOR;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlibenhvien.ACTIVITIES.LoginAC;
import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.FRAGMENTPATIENT.DiagnosiDetailFM;
import com.example.quanlibenhvien.FRAGMENTPHARMA.PrescriptionDetailFM;
import com.example.quanlibenhvien.R;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ListPatientFM extends Fragment {
    private TabHost tabHost;
    private ListView lv1,lv2;
    private EditText ed1;
    private ImageButton btn1,btn2;
    private String sid;
    private Context context;
    private Connect conn;
    private FragmentTransaction transaction;
    private DianosiDetail diagnosiDetailFM;
    private PrescriptionDetailFM diagnosiDetail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_patient_fm, container, false);
        mapping(view);
        setup();
        setClick();
        return  view;
    }
    @Override
    public void onResume() {
        super.onResume();
        setuptab1(LoginAC.username);
    } // trở lại
    private void setupTab(){
        tabHost.setup();
        TabHost.TabSpec sp1,sp2;
        sp1 = tabHost.newTabSpec("t1");
        sp1.setContent(R.id.tab_1);
        sp1.setIndicator(getString(R.string.today));
        tabHost.addTab(sp1);
        sp2 = tabHost.newTabSpec("t2");
        sp2.setContent(R.id.tab_2);
        sp2.setIndicator(getString(R.string.history));
        tabHost.addTab(sp2);

        setuptab1(LoginAC.username);
        setuptab2(LoginAC.username);
    }  //  cài đặt tabhost
    private void setuptab1(String sId) {
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  GetDiagnosisByDoctor '" + sId +"'"
            );
            ArrayList<String> data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    String value ="NO: " +rs.getString("number") +"-"+rs.getString("id").toUpperCase() +"-"+rs.getString("patient") +" "+rs.getString("name") ;
                    data.add(value);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, data);
                lv1.setAdapter(adapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
    } // cài đặt tab1
    private void setuptab2(String sId) {
        btn2.setVisibility(View.GONE);
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  GetDiagnosisedByDoctor '" + sId +"'"
            );
            ArrayList<String> data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    String value ="NO: " +rs.getString("number") +"-"+rs.getString("id").toUpperCase() +"-"+rs.getString("patient") +" "+rs.getString("name") ;
                    data.add(value);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, data);
                lv2.setAdapter(adapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
    } // cài đặt tab2
    private void setup() {
        context = getContext();
        setupTab();
    } // cài đặt chung
    private void setClick() {
        lv1.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            String[] parts = selectedItem.split("-");
            String firstPart = parts[1];
            sid = firstPart;
            openDetailFM();
        });

        lv2.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            String[] parts = selectedItem.split("-");
            String firstPart = parts[1];

            openDetail(firstPart);
        });
    }  // bấm vào
    private void openDetailFM() {
        diagnosiDetailFM = new DianosiDetail();
        diagnosiDetailFM.setId(sid);
        transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameMain, diagnosiDetailFM);
        transaction.addToBackStack(null);
        transaction.commit();
    } // mở chi tiết

    private void openDetail(String sid) {
        diagnosiDetail = new PrescriptionDetailFM();
        diagnosiDetail.setId(sid);
        transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameMain, diagnosiDetail);
        transaction.addToBackStack(null);
        transaction.commit();
    } // mở chi tiết
    private void mapping(View view) {
        tabHost = view.findViewById(R.id.tabhost);
        lv1 = view.findViewById(R.id.lv1);
        lv2 = view.findViewById(R.id.lv2);
        ed1 = view.findViewById(R.id.ed1);
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
    }  // ánh xạ dữ liệu
}
