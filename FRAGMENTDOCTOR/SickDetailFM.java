package com.example.quanlibenhvien.FRAGMENTDOCTOR;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.R;

import java.sql.ResultSet;
public class SickDetailFM extends Fragment {
    private Connect conn;
    private Context context;
    private ImageButton btn1;
    private Button btn2, btn3;
    private CheckBox cb1,cb2;
    private EditText ed1, ed2,ed3;
    private String sId;
    public void setId(String id) {
        sId = id;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sick_detail, container, false);
        mapping(view);
        setup();;
        init();
        setClick();
        return view;
    }
    private void init(){
        if (sId != null) {
            filldata(sId);
        }
    } // kiểm tra có id truyền vào không , có thì đổ dữ liệu vào
    private void filldata(String sId){
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  GetSickById '" + sId +"'"
            );
            if( rs!= null) {
                while (rs.next()) {
                    ed1.setText(rs.getString("id"));
                    ed2.setText(rs.getString("name"));
                    ed3.setText(rs.getString("symptom"));
                    int a = rs.getInt("status");

                    if(a != 1){
                        cb2.setChecked(true);
                        btn2.setVisibility(View.GONE);
                        btn3.setText(R.string.backup);
                    }
                    else {
                        cb1.setChecked(true);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
    } // đổ dữ liệu
    private void setup() {
        context = getContext();
    } // cài đặt
    private void setClick() {
        btn1.setOnClickListener(v ->  onBackPressed());
        btn2.setOnClickListener(v -> getDel());
        btn3.setOnClickListener(v -> getSave());
    } // bấm vào
    private void getDel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.confdel);
        builder.setNegativeButton(R.string.cancle, null);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            del();
        });
        builder.show();
    }  // thực hiện xóa
    private void del(){
        String id = ed1.getText().toString().trim();
        conn = new Connect();
        String sql= "UPDATE  Sick SET status = 0 where id = '" + id +"'";
        try {
            if (conn.exec(sql)) {
                Toast.makeText(context, getResources().getString(R.string.editok), Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(context, getResources().getString(R.string.editerr), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }finally {
            conn.closeConnection();
        }
    } // hàm xóa
    private void getSave() {
       if(sId != null){
            if(btn3.getText().toString().equals("Lưu")){
                edit();
            }
       }
       else {
           addnew();
       }
    } // thực hiện lưu/sửa
    public void reset() {
        sId = null;
    }
    private void addnew(){
        conn = new Connect();
        String id = ed1.getText().toString().trim();
        String name = ed2.getText().toString().trim();
        String symptom = ed3.getText().toString().trim();
        String sql = "Insert into Sick values ('" + id + "',N'" + name + "',N'" + symptom + "',1)";
        try {
            if (conn.exec(sql)) {
                Toast.makeText(context, getResources().getString(R.string.addok), Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(context, getResources().getString(R.string.adder), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            conn.closeConnection();
        }
    } // hàm thêm mới
    private void edit(){
        conn = new Connect();
        String id = ed1.getText().toString().trim();
        String name = ed2.getText().toString().trim();
        String symptom = ed3.getText().toString().trim();
        String sql = "UPDATE  Sick SET name = N'" + name +"',symptom = N'" + symptom +"' where id = '" + id +"'";
        try {
            if (conn.exec(sql)) {
                Toast.makeText(context, getResources().getString(R.string.editok), Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(context, getResources().getString(R.string.editerr), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }finally {
            conn.closeConnection();
        }
    } // hàm cập nhật
    private void onBackPressed() {
        reset();
        getParentFragmentManager().popBackStack();
    } // quay lại
    private void mapping(View view) {
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        ed1 = view.findViewById(R.id.ed1);
        ed2 = view.findViewById(R.id.ed2);
        ed3 = view.findViewById(R.id.ed3);
        cb1 = view.findViewById(R.id.cb1);
        cb2 = view.findViewById(R.id.cb2);
    } // ánh xạ dữ liệu
}
