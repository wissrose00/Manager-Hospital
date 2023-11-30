package com.example.quanlibenhvien.FRAGMENTPHARMA;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.quanlibenhvien.R;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class ListPrescriptionFM extends Fragment {
    private TabHost tabHost;
    private ListView lv1,lv2;
    private EditText ed1;
    private ImageButton btn1,btn2;
    private Context context;
    private Connect conn;
    private FragmentTransaction transaction;
    private PrescriptionDetailFM prescriptionDetailFM;
    private  ArrayList<String> data;
    private  ArrayAdapter<String> adapter;
    private String sid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_patient_fm, container, false);
        mapping(view);
        setup();
        setClick();
        return  view;
    }
    private void setup() {
        context = getContext();
        setupTab();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
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
        setuptab1();
        setuptab2();
    }
    private void setuptab1() {
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  GetPrescriptionsToday"
            );
            ArrayList<String> data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    String value =rs.getString("id").toUpperCase() +"    "+rs.getString("name") +" "+rs.getString("birth")  ;
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
    }
    private void setuptab2() {
        btn2.setVisibility(View.GONE);
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  GetPrescriptionsHistory"
            );
            data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    String value =rs.getString("id").toUpperCase() +"    "+rs.getString("name") +" "+rs.getString("birth")  ;
                    data.add(value);
                }
                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, data);
                lv2.setAdapter(adapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
    }
    private void setClick() {
        lv1.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            String[] parts = selectedItem.split(" ");
            String firstPart = parts[0];
            sid = firstPart;
            openDetailFM();
        });

        lv2.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            String[] parts = selectedItem.split(" ");
            String firstPart = parts[0];
            sid = firstPart;
            openDetailFM();
        });

        btn1.setOnClickListener(v -> getSearch());
    }
    private void openDetailFM() {
        prescriptionDetailFM = new PrescriptionDetailFM();
        prescriptionDetailFM.setId(sid);
        transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameMain, prescriptionDetailFM);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void getSearch() {
        String keyword = ed1.getText().toString().toLowerCase();
        if(keyword.isEmpty()){
            setuptab2();
        }
        else {
            List<String> searchResults = new ArrayList<>();

            for (String item : data) {
                if (item.toLowerCase().contains(keyword)) {
                    searchResults.add(item);
                }
            }
            adapter.clear();
            adapter.addAll(searchResults);
            adapter.notifyDataSetChanged();
        }
    }

    private void mapping(View view) {
        tabHost = view.findViewById(R.id.tabhost);
        lv1 = view.findViewById(R.id.lv1);
        lv2 = view.findViewById(R.id.lv2);
        ed1 = view.findViewById(R.id.ed1);
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
    }  // ánh xạ
}
