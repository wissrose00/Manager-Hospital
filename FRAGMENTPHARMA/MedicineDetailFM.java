package com.example.quanlibenhvien.FRAGMENTPHARMA;

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

public class MedicineDetailFM extends Fragment {
    private Connect conn;
    private Context context;
    private ImageButton btn1;
    private Button btn2, btn3;
    private EditText ed1, ed2,ed3,ed4,ed5,ed6,ed7;
    private CheckBox cb1,cb2;
    private String sId;

    public void setId(String id) {
        sId = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medicine_detail, container, false);
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
    }

    private void filldata(String sId){
        context = getContext();
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  GetMedecineById '" + sId +"'"
            );
            if( rs!= null) {
                while (rs.next()) {
                    ed1.setText(rs.getString("id"));
                    ed2.setText(rs.getString("name"));
                    ed3.setText(rs.getString("production_date"));
                    ed4.setText(rs.getString("expriry_date"));
                    ed5.setText(rs.getString("quantity"));
                    ed6.setText(rs.getString("medicine_use"));
                    ed7.setText(rs.getString("composition"));

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
    }

    private void setup() {
        context = getContext();
    }

    private void setClick() {
        btn1.setOnClickListener(v ->  onBackPressed());
        btn2.setOnClickListener(v -> getDel());
        btn3.setOnClickListener(v -> getSave());
    }

    private void getDel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.confdel);
        builder.setNegativeButton(R.string.cancle, null);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            del();
        });
        builder.show();
    }

    private void del(){
        String id = ed1.getText().toString().trim();
        conn = new Connect();
        String sql= "UPDATE  Medecine SET status = 0 where id = '" + id +"'";
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
    }
    private void getSave() {
        if(sId != null){
            if(btn3.getText().toString().equals("Lưu")){
                edit();
            }
        }
        else {
            addnew();
        }
    }
    public void reset() {
        sId = null;
    }

    private void addnew(){
        conn = new Connect();
        String id = ed1.getText().toString().trim();
        String name = ed2.getText().toString().trim();
        String production_date = ed3.getText().toString().trim();
        String expriry_date = ed4.getText().toString().trim();
        String quantity = ed5.getText().toString().trim();
        String medicine_use = ed6.getText().toString().trim();
        String composition = ed7.getText().toString().trim();
        String status = "1";
        String image = "";

        String sql = "INSERT INTO [dbo].[Medecine]" +
                " ([id]," +
                " [name]," +
                " [production_date]," +
                " [expriry_date]," +
                " [quantity]," +
                " [medicine_use]," +
                " [composition]," +
                " [status]," +
                " [image])" +
                " VALUES" +
                " ('" + id + "'," +
                " N'" + name + "'," +
                " '" + production_date + "'," +
                " '" + expriry_date + "'," +
                " '" + quantity + "'," +
                " N'" + medicine_use + "'," +
                " N'" + composition + "'," +
                " '" + status + "'," +
                " '" + image + "')";

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
    }

    private void edit(){
        conn = new Connect();
        String id = ed1.getText().toString().trim();
        String name = ed2.getText().toString().trim();
        String production_date = ed3.getText().toString().trim();
        String expriry_date = ed4.getText().toString().trim();
        String quantity = ed5.getText().toString().trim();
        String medicine_use = ed6.getText().toString().trim();
        String composition = ed7.getText().toString().trim();
        String status = "1";
        String image = null;

        String sql = "UPDATE [dbo].[Medecine] SET" +
                " [name] = N'" + name + "'," +
                " [production_date] = '" + production_date + "'," +
                " [expriry_date] = '" + expriry_date + "'," +
                " [quantity] = '" + quantity + "'," +
                " [medicine_use] = N'" + medicine_use + "'," +
                " [composition] = N'" + composition + "'," +
                " [status] = '" + status + "'," +
                " [image] = '" + image + "'" +
                " WHERE [id] = '" + id + "'";
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
    }


    private void onBackPressed() {
        reset();
        getParentFragmentManager().popBackStack();
    }

    private void mapping(View view) {

        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);

        cb1 = view.findViewById(R.id.cb1);
        cb2 = view.findViewById(R.id.cb2);

        ed1 = view.findViewById(R.id.ed1);
        ed2 = view.findViewById(R.id.ed2);
        ed3 = view.findViewById(R.id.ed3);
        ed4 = view.findViewById(R.id.ed4);
        ed5 = view.findViewById(R.id.ed5);
        ed6 = view.findViewById(R.id.ed6);
        ed7 = view.findViewById(R.id.ed7);



    }

}
