package com.example.quanlibenhvien.FRAGMENTPATIENT;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlibenhvien.ACTIVITIES.LoginAC;
import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.R;

import java.sql.ResultSet;
import java.util.ArrayList;
public class HomePatientFM extends Fragment {
    private Connect conn;
    private Context context;
    private TextView tv2,tv3,tv4,tv5,tv6;
    private ListView lv1,lv2;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;
    private Button btn1,btn2,btn3,btn4;
    private  String b,sid,getDoctor,getRoom;
    private View view;
    private AlertDialog.Builder builder1,builder2;
    private AlertDialog alertDialog,alertDialog2;
    private Handler handler;
    private Runnable runnable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_patient, container, false);
        mapping1(view);
        init();
        setup();
        setClick();
        return view;
    }

    private void startDataLoading() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                fillData(LoginAC.username.toString().trim().toUpperCase());
                int rowCount =lv1.getChildCount();
                if(rowCount == 0){
                    btn1.setText(R.string.examination);
                }
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void stopDataLoading() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopDataLoading();
    }
    private void setClick(){
        btn1.setOnClickListener(v -> getShow());

        lv1.setOnItemClickListener((parent, view, position, id) -> {
            showDialogConfirm();
        });

    } // bấm vào
    private void getShow() {
        int rowCount =lv1.getChildCount();
        if(!btn1.getText().toString().trim().equals("Hủy")){
            if(rowCount == 0){
                showDialogChoose();
            }
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.cancle);
            builder.setMessage(R.string.confcancle);
            builder.setNegativeButton(R.string.cancle, null);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                cancle(sid);
                btn1.setText(R.string.examination);
            });
            builder.show();
        }
    } // hiện thị dialog lựa chọn
    private void showDialogChoose(){
        view = LayoutInflater.from(context).inflate(R.layout.dialog_examination, relativeLayout);
        mapping2(view);
        fillData();
        builder1.setView(view);
        alertDialog = builder1.create();
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        btn2.setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });
        lv2.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            String[] parts = selectedItem.split("-");
            getDoctor = getDoc(parts[0]);
            getReg(getDoctor,LoginAC.username.toString().trim().toUpperCase(),getRoom);
            showDialogConfirm();
            alertDialog.dismiss();
        });
        alertDialog.show();
    } // lựa chọn khám bệnh
    private void fillData(){
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC GetDepartmentsToShow "
            );
            ArrayList<String> data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    String value = rs.getString("id").toUpperCase()
                            +"--"+rs.getString("decription");
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
    }  // hiện thị các lựa chọn
    private void showDialogConfirm(){
        view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, linearLayout);
        mapping3(view);
        fillDataConfirm(sid);
        builder2 = new AlertDialog.Builder(context);
        builder2.setView(view);
        alertDialog2 = builder2.create();
        if(alertDialog2.getWindow() != null){
            alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        btn4.findViewById(R.id.btn4).setOnClickListener(view1 -> {
            fillData(LoginAC.username.toString().trim().toUpperCase());
            alertDialog2.dismiss();
        });
        btn3.findViewById(R.id.btn3).setOnClickListener(view1 -> {
            if(!sid.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.cancle);
            builder.setMessage(R.string.confcancle);
            builder.setNegativeButton(R.string.cancle, null);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                cancle(sid);
            });
            builder.show();
            }
            alertDialog2.dismiss();
        });
        alertDialog2.show();
    } // hiện thị xác nhận đăng kí
    private void fillData(String sId){
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC GetNofiPatient @patientId = '" + sId +"'"
            );
            ArrayList<String> data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    int stt = rs.getInt("status");
                    String st;
                    if(stt == 1 ){
                        st = "Chờ bên ngoài";
                    }else {
                        st = "Có thể vào khám";
                    }
                    String value ="MÃ PHIẾU: "+rs.getString("id").toUpperCase() +"\nSỐ: "+rs.getString("number")+"\n"+ rs.getString("room").toUpperCase()  +"\nTRẠNG THÁI: "+st;
                    sid =rs.getString("id");
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
    } // hiện thị thông báo
    private void fillDataConfirm(String sId){
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC GetDiagConfirmInfo  '" + sId +"'"
            );
            if( rs!= null) {
                while (rs.next()) {
                   tv3.setText(String.valueOf(rs.getInt("number")));
                    tv4.setText(rs.getString("room").toUpperCase());
                    tv5.setText(rs.getString("doctor").toUpperCase());
                    tv6.setText(rs.getString("department").toUpperCase());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
    } // hiện thị thông tin đăng kí
    private String getDoc(String a){
        context = getContext();
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  GetEmpRmByDep @department= '" + a +"' "
            );
            if( rs!= null) {
                while (rs.next()) {
                    getDoctor = rs.getString("employee");
                    getRoom = rs.getString("room");
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
        return getDoctor;
    }  // lấy bác sĩ khi bốc số
    private void getReg(String doctor , String id , String room){
        context = getContext();
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  InsertDiagnosi '" + doctor +"','" + id +"','" + room +"'"
            );
            if( rs!= null) {
                while (rs.next()) {
                    int a = rs.getInt("InsertionStatus");
                    if(a != 1){
                       System.out.println("err");
                    }
                    else {
                        System.out.println("oke");
                        sid  = rs.getString("id");
                        btn1.setText("Hủy");
                    }
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
    } // đăng kí khám bệnh
    private void cancle(String id){
        conn = new Connect();
        String sql= "delete  Diagnosi where id = '" + id +"'";
        try {
            if (conn.exec(sql)) {
                Toast.makeText(context, getResources().getString(R.string.editok), Toast.LENGTH_SHORT).show();
                fillData(LoginAC.username.toString().trim().toUpperCase());
                sid = null;
                btn1.setText(R.string.examination);
            } else {
                Toast.makeText(context, getResources().getString(R.string.editerr), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }finally {
            conn.closeConnection();
        }
    } // hủy đăng kí khám bệnh
    private void init(){
        b = getExpirationDate(LoginAC.username.toString().trim());
    } // gắn ngày hết hạn bảo hiểm
    private String getExpirationDate(String id){
        context = getContext();
        String a ="";
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC  getPatientExpirationDate '" + id +"'"
            );
            ArrayList<String> data = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    a =rs.getString("expiration_date") ;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
        return a;
    } // lấy ngày hết hạn bảo hiểm
    private  void setup(){
        tv2.setText(b);
        startDataLoading();
        builder1 = new AlertDialog.Builder(context);
    } // cài đặt chung
    private void mapping1(View view) {
        tv2 =  view.findViewById(R.id.tv2);
        btn1 = view.findViewById(R.id.btn1);
        lv1 = view.findViewById(R.id.lv1);
        relativeLayout = view.findViewById(R.id.relative);
        linearLayout = view.findViewById(R.id.line1);
    } // ánh xạ
    private void mapping2(View view) {
        lv2 =view.findViewById(R.id.lv2);
        btn2 = view.findViewById(R.id.btn2);
    } // ánh xạ dialog lựa chọn đăng kí
    private void mapping3(View view) {
        btn3 = view.findViewById(R.id.btn3);
        btn4 = view.findViewById(R.id.btn4);
        tv3 = view.findViewById(R.id.tv6);
        tv4 = view.findViewById(R.id.tv7);
        tv5 = view.findViewById(R.id.tv8);
        tv6 = view.findViewById(R.id.tv9);
    } // ánh xạ dialog xác nhận đăng ki
}
