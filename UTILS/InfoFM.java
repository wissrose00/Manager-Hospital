package com.example.quanlibenhvien.UTILS;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.quanlibenhvien.ACTIVITIES.LoginAC;
import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.R;
import com.squareup.picasso.Picasso;
import java.sql.ResultSet;
public class InfoFM  extends Fragment {
    private Connect connect;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
    private ImageView img1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_employee, container, false);
        mapping(view);
        setup();
        return  view;
    }
    private void setup(){
        fillData(LoginAC.username.toString().toUpperCase());
    } // gắn dữ liệu
    private void fillData(String id){
        try {
            connect = new Connect();
            ResultSet rs = connect.read("Select * from v_info_emp where id = '"+id+"' "
            );
            if( rs!= null) {
                while (rs.next()) {
                    tv1.setText(rs.getString("id"));
                    tv2.setText(rs.getString("name"));
                    tv3.setText(rs.getString("cic"));
                    tv4.setText(rs.getString("gender"));
                    tv5.setText(rs.getString("born"));
                    tv6.setText(rs.getString("phone"));
                    tv7.setText(rs.getString("address"));
                    tv9.setText(rs.getString("department"));
                    tv8.setText(rs.getString("position"));
                    tv10.setText(rs.getString("start_date"));

                    String imageURL = rs.getString("image");
                    Picasso.get().load(imageURL).into(img1);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            connect.closeConnection();
        }
    }  // đổ dữ liệu
    private void mapping(View view){
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv6 = view.findViewById(R.id.tv6);
        tv7 = view.findViewById(R.id.tv7);
        tv8 = view.findViewById(R.id.tv8);
        tv9 = view.findViewById(R.id.tv9);
        tv10 = view.findViewById(R.id.tv10);
        img1 = view.findViewById(R.id.img1);
    } // ánh xạ

}
