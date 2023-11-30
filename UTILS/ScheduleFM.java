package com.example.quanlibenhvien.UTILS;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlibenhvien.ACTIVITIES.LoginAC;
import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.R;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class ScheduleFM  extends Fragment {
    private Connect connect;
    private ListView lv1,lv2;
    private Spinner spinner;
    private Context context;
    private  int currentYear,currentWeek,defaultWeekPosition;
    private Calendar calendar;
    private  List<String> weekList;
    private ArrayAdapter<String> adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule, container, false);
        mapping(view);
        setupSpinner();
        setClick();
        return  view;
    }
    private void fillDataNoon(String id,String start, String end){
        context = getContext();
        try {
            connect = new Connect();
            ResultSet rs = connect.read("EXEC  GetScheduleByDateRangeNoon '" + id + "','" + start + "','" + end + "' "
            );
            ArrayList<String> data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    String value =rs.getString("day_name")+" "+rs.getString("room")+" " +rs.getString("from") +" "+rs.getString("description");
                    data.add(value);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, data);
                lv2.setAdapter(adapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            connect.closeConnection();
        }
    } // lấy dữ liệu buổi chiều
    private void fillDataMorning(String id,String start, String end){
        context = getContext();
        try {
            connect = new Connect();
            ResultSet rs = connect.read("EXEC  GetScheduleByDateRangeMorning '" + id + "','" + start + "','" + end + "' "
            );
            ArrayList<String> data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    String value =rs.getString("day_name")+" "+rs.getString("room")+" " +rs.getString("from") +" "+rs.getString("description");
                    data.add(value);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, data);
                lv1.setAdapter(adapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            connect.closeConnection();
        }
    } // lấy dữ liệu buổi sáng
    private void setClick(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String[] parts = selectedItem.split(":");
                String[] dateRange = parts[1].split("--");
                String startDate = Util.formatDate(dateRange[0].trim());
                String endDate = Util.formatDate(dateRange[1].trim());
                fillDataMorning(LoginAC.username,startDate,endDate);
                fillDataNoon(LoginAC.username,startDate,endDate);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    } // chọn tuần > đổ dữ liệu khi chọn
    private void setupSpinner(){
        context = getContext();
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        weekList = Util.getWeekList(currentYear);
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, weekList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        defaultWeekPosition = currentWeek - 1;
        spinner.setSelection(defaultWeekPosition);
    } // cài đặt spinner
    private void mapping(View view){
        lv1 = view.findViewById(R.id.lv1);
        lv2 = view.findViewById(R.id.lv2);
        spinner = view.findViewById(R.id.spinner1);
    } // ánh xạ
}
