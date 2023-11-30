package com.example.quanlibenhvien.FRAGMENTDOCTOR;
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
import java.util.List;

public class SickFM  extends Fragment {
    private Connect conn;
    private Context context;
    private ListView   lv1;
    private ImageButton btn1,btn2;
    private EditText ed1;
    private SickDetailFM sickDetailFM;
    private FragmentTransaction transaction;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> data;
    private  ResultSet rs;
    private String sid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onelist_onetext_onebtn, container, false);
        mapping(view);
        fillData();
        setClick();
        return  view;
    }
    private void setClick(){
        btn1.setOnClickListener(v -> getSearch());
        btn2.setOnClickListener(v -> getDetail());
        lv1.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            String[] parts = selectedItem.split("-");
            String firstPart = parts[0];
            sid = firstPart;
            openDetailFM();
        });
    } // bấm vào
    private void getDetail() {
        sid = null;
        openDetailFM();
    } // mở chi tiết
    private void openDetailFM() {
        sickDetailFM = new SickDetailFM();
        sickDetailFM.setId(sid);
        transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameMain, sickDetailFM);
        transaction.addToBackStack(null);
        transaction.commit();
    } // đổi fragment chi tiết
    private void getSearch() {
        search();
    } // tìm kiếm
    private void search(){
        String text = ed1.getText().toString().toLowerCase();
        if(text.isEmpty()){
            fillData();
        }
        else {
            List<String> searchResults = new ArrayList<>();
            for (String item : data) {
                if (item.toLowerCase().contains(text)) {
                    searchResults.add(item);
                }
            }
            adapter.clear();
            adapter.addAll(searchResults);
            adapter.notifyDataSetChanged();
        }
    } // hàm tìm kiếm
    private void fillData(){
        context = getContext();
        try {
            conn = new Connect();
            rs = conn.read("EXEC  GetSickData"
            );
            data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    String value =rs.getString("id") +"---"+rs.getString("name");
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
    } // cài đặt dữ liệu
    private void mapping(View view){
        lv1 = view.findViewById(R.id.lv2);
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        ed1 = view.findViewById(R.id.ed1);
    } // ánh xạ
}
