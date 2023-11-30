package com.example.quanlibenhvien.FRAGMENTPATIENT;
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
public class InfoPatientFM  extends Fragment {
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11;
    private ImageView img1;
    private Connect connect;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_patient, container, false);
        mapping(view);
        setup();
        return view;
    }
    private void setup(){
        fillData(LoginAC.username.toString().trim());
    } // cài dặt chung
    private void fillData(String id){
        try {
            connect = new Connect();
            ResultSet rs = connect.read("Select * from v_info_patient where id = '"+id+"' "
            );
            if( rs!= null) {
                while (rs.next()) {
                    tv1.setText(rs.getString("id"));
                    tv3.setText(rs.getString("cic"));
                    tv2.setText(rs.getString("name"));
                    tv4.setText(rs.getString("gender"));
                    tv5.setText(rs.getString("age"));
                    tv6.setText(rs.getString("birth"));
                    tv7.setText(rs.getString("phone"));
                    tv8.setText(rs.getString("address"));
                    tv9.setText(rs.getString("insurance_id"));
                    tv10.setText(rs.getString("effective_date"));
                    tv11.setText(rs.getString("expiration_date"));
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
    } // đổ dữ liệu
    private void mapping(View view) {
        tv1 =  view.findViewById(R.id.tv1);
        tv2 =  view.findViewById(R.id.tv2);
        tv3 =  view.findViewById(R.id.tv3);
        tv4 =  view.findViewById(R.id.tv4);
        tv5 =  view.findViewById(R.id.tv5);
        tv6 =  view.findViewById(R.id.tv6);
        tv7 =  view.findViewById(R.id.tv7);
        tv8 =  view.findViewById(R.id.tv8);
        tv9 =  view.findViewById(R.id.tv9);
        tv10 =  view.findViewById(R.id.tv10);
        tv11 =  view.findViewById(R.id.tv11);
        img1 =  view.findViewById(R.id.img1);
    } // ánh xạ
}
