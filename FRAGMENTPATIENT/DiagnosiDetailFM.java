package com.example.quanlibenhvien.FRAGMENTPATIENT;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.R;

import java.sql.ResultSet;
import java.util.ArrayList;
public class DiagnosiDetailFM extends Fragment {
    private Connect conn;
    private Context context;
    private ImageButton btn1;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
    private ListView lv1;
    private String sid;
    public void setId(String id) {
        sid = id;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diagnosi_patient_detail, container, false);
        mapping(view);
        setup();;
        init();
        setClick();
        return view;
    }
    private void init(){
        if (sid != null) {
            filldata(sid);
        }
    }
    private void filldata(String sId){
        context = getContext();
        try {
            conn = new Connect();
            ResultSet rs1 = conn.read("EXEC  GetInfoPressById10 '" + sId +"'"
            );
            ResultSet rs2 = conn.read("EXEC  [GetInfoByDiagnosi] '" + sId +"'"
            );
            ResultSet rs3 = conn.read("EXEC  GetExaminateInfo '" + sId +"'"
            );
            StringBuilder result = new StringBuilder();
            if( rs3!= null) {
                while (rs3.next()) {
                    String id = rs3.getString("id");
                    String name = rs3.getString("name");
                    result.append(id.toUpperCase()).append("-").append(name).append(";");
                }
                String combinedRows = result.toString();
                tv6.setText(combinedRows);
            }

            if( rs1!= null) {
                while (rs1.next()) {
                    tv1.setText(rs1.getString("id").toUpperCase());
                    tv2.setText(rs1.getString("room"));
                    tv3.setText(rs1.getString("date"));
                    tv4.setText(rs1.getString("doctor").toUpperCase());
                    tv5.setText(rs1.getString("department").toUpperCase());
                    tv7.setText(rs1.getString("note"));

                }
            }

            ArrayList<String> data = new ArrayList<>();
            if( rs2!= null) {
                while (rs2.next()) {
                    String value =rs2.getString("medicine").toUpperCase()+"  "+rs2.getString("name").toUpperCase() +"\nSL: "+rs2.getString("quantity")+"  "+rs2.getString("used").toUpperCase();
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
    private void setup() {
        context = getContext();
    }
    private void  setClick(){
        btn1.setOnClickListener(v ->  onBackPressed());
    }
    private void onBackPressed() {
        getParentFragmentManager().popBackStack();
    }
    private void mapping(View view) {
        btn1 = view.findViewById(R.id.btn1);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv6 = view.findViewById(R.id.tv6);
        tv7 = view.findViewById(R.id.tv7);
        tv8 = view.findViewById(R.id.tv8);
        lv1 =  view.findViewById(R.id.lv1);
    }
}
