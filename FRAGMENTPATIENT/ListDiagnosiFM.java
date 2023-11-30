package com.example.quanlibenhvien.FRAGMENTPATIENT;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlibenhvien.ACTIVITIES.LoginAC;
import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.R;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class ListDiagnosiFM  extends Fragment {
    private ListView lv1;
    private EditText ed1;
    private ImageButton btn1,btn2;
    private Context context;
    private Connect conn;
    private FragmentTransaction transaction;
    private DiagnosiDetailFM diagnosiDetailFM;
    private  ArrayAdapter<String> adapter;
    private  ArrayList<String> data;
    private String sid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onelist_onetext_onebtn, container, false);
        mapping(view);
        setup();
        setClick();
        return  view;
    }
    private void setup() {
        context = getContext();
        btn2.setVisibility(View.GONE);
        fillData(LoginAC.username.toString().trim());
    } // cài đặt chung
    private void fillData(String sId){
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  [GetPrescriptionsById] '" + sId +"'"
            );
            data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    String value = rs.getString("id").toUpperCase() +" --- "+rs.getString("date");
                    data.add(value);
                }
                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, data);
                lv1.setAdapter(adapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
    } // đổ dữ liệu
    private void setClick() {
        lv1.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            String[] parts = selectedItem.split(" ");
            String firstPart = parts[0];
            sid = firstPart;
            openDetailFM();
        });

        ed1.setOnClickListener(v -> {
            Calendar currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
                String dateString = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                ed1.setText(dateString);
            }, year, month, day);
            datePickerDialog.show();
        });

        btn1.setOnClickListener(v -> getSearch());

    } // bấm vào
    private void openDetailFM() {
        diagnosiDetailFM = new DiagnosiDetailFM();
        diagnosiDetailFM.setId(sid);
        transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameMain, diagnosiDetailFM);
        transaction.addToBackStack(null);
        transaction.commit();
    } // mở chi tiết
    private void getSearch() {
        String keyword = ed1.getText().toString().toLowerCase();
        if(keyword.isEmpty()){
            fillData(LoginAC.username.toString().trim());
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
    } // tìm kiếm
    private void mapping(View view) {
        lv1 = view.findViewById(R.id.lv2);
        btn2 = view.findViewById(R.id.btn2);
        btn1 = view.findViewById(R.id.btn1);
        ed1 = view.findViewById(R.id.ed1);
    } // ánh xạ
}
