package com.example.quanlibenhvien.FRAGMENTDOCTOR;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlibenhvien.ACTIVITIES.LoginAC;
import com.example.quanlibenhvien.ADAPTERS.DrugAdapter;
import com.example.quanlibenhvien.ADAPTERS.SickAdapter;
import com.example.quanlibenhvien.DATABASE.Connect;
import com.example.quanlibenhvien.R;
import com.squareup.picasso.Picasso;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DianosiDetail extends Fragment {
    private Connect conn;
    private Context context;
    private ImageButton btn1,btn3,btn4,btn00,btnsearch;
    private Button btn2,btn5,btn6,btn;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;
    private ImageView img1;
    private EditText ed1,ed2,ed3,ed0;
    private ListView lv1,lv2,lv3,lv4;
    private LinearLayout ln1,ln2,ln3;
    private RelativeLayout relativeLayout;
    private View view,view1;
    private AlertDialog.Builder builder1,builder2;
    private AlertDialog alertDialog,alertDialog2;
    private List<HashMap<String, String>> drugs,sicks;
    public List<HashMap<String, String>> selectedDrugs;
    public List<HashMap<String, String>> selectedSicks;
    private DrugAdapter adapter;
    private SickAdapter sickAdapter;
    private String sid;
    public void setId(String id) {
        sid = id;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diagnosi_doctor_detail, container, false);
        mapping1(view);
        init();
        setup();
        setClick();
        return view;
    }
    private void init(){
        if (sid != null) {
            filldata(sid);
        }
    } // kiểm tra có id truyền vào không , có thì đổ dữ liệu vào
    private void filldata(String sId){
        context = getContext();
        try {
            conn = new Connect();
            ResultSet rs1 = conn.read("EXEC  GetInfoByID '" + sId +"'"
            );
            ArrayList<String> data = new ArrayList<>();
            if( rs1!= null) {
                while (rs1.next()) {
                    tv1.setText(rs1.getString("id").toUpperCase());
                    tv2.setText(rs1.getString("idpatient"));
                    tv3.setText(rs1.getString("name"));
                    tv4.setText(rs1.getString("cic"));
                    tv5.setText(rs1.getString("gender"));
                    tv6.setText(rs1.getString("age"));
                    tv7.setText(rs1.getString("birth"));
                    tv8.setText(rs1.getString("phone"));
                    tv9.setText(rs1.getString("address"));

                    String img = rs1.getString("image");
                    Picasso.get().load(img).into(img1);

                    ln1.setVisibility(view.GONE);
                    ln2.setVisibility(view.GONE);
                    ln3.setVisibility(view.GONE);

                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
    }

    private  void setup(){
        context = getContext();
        selectedDrugs = new ArrayList<>();
        selectedSicks = new ArrayList<>();
        builder1 = new AlertDialog.Builder(context);
        builder2 = new AlertDialog.Builder(context);
        getCancle();
    } // cài đặt chung
    private void setClick(){
        btn1.setOnClickListener(v ->  onBackPressed());
        btn2.setOnClickListener(v -> getShowHistorySick());

        btn.setOnClickListener(v ->getCall());

        btn3.setOnClickListener(v -> getShowSick());
        btn4.setOnClickListener(v -> getShowMedicine());
        btn5.setOnClickListener(v -> getBtn5()
        );
        btn6.setOnClickListener(v -> getSave());

    } // bấm vào
    private void getCall() {
        ln1.setVisibility(view.VISIBLE);
        ln2.setVisibility(view.VISIBLE);
        ln3.setVisibility(view.VISIBLE);
        btn.setVisibility(view.GONE);

        conn = new Connect();
        String sql= "UPDATE  Diagnosi SET status = 2  where id = '" + sid +"'";
        try {
            if (conn.exec(sql)) {
                Toast.makeText(context, getResources().getString(R.string.editok), Toast.LENGTH_SHORT).show();
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
        for (HashMap<String, String> item : selectedSicks) {
            String id = item.get("id");
            addSick(sid, id);
        }

        int itemCount = lv2.getCount();
        for (int i = 0; i < itemCount; i++) {
            View childView = lv2.getChildAt(i);
            if (childView != null) {
                String quantity = ((EditText) childView.findViewById(R.id.ed1)).getText().toString();
                String id = ((TextView) childView.findViewById(R.id.tv1)).getText().toString();
                String used = ((TextView) childView.findViewById(R.id.ed2)).getText().toString();
                addDrug(id, Integer.parseInt(quantity), used);
            }
        }
        updateDone();
    }
    private void updateDone(){
        conn = new Connect();
        String sql= "UPDATE  Diagnosi SET status = 3, note = N'" + ed0.getText().toString().trim()+"' where id = '" + sid +"'";
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
    private void addSick(String id , String sick){
        conn = new Connect();
        String sql = "Insert into Examinate values ('" + id + "','" + sick + "')";
        try {
            if (conn.exec(sql)) {
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            conn.closeConnection();
        }
    }
    private void addDrug( String medicine , int quantity , String used){
        conn = new Connect();
        String sql = "Insert into Presciption values ('" + sid + "','" + medicine + "','" + quantity + "',N'" + used + "')";
        try {
            if (conn.exec(sql)) {
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            conn.closeConnection();
        }

    }
    private void getBtn5(){
        getCancle();
    }
    private void getCancle() {
        ln1.setVisibility(view.GONE);
        ln2.setVisibility(view.GONE);
        ln3.setVisibility(view.GONE);
        btn.setVisibility(view.VISIBLE);

    }
    private void getShowHistorySick() {

    } // xem bệnh án
    private void onBackPressed() {
        getParentFragmentManager().popBackStack();
    } // quay lại
    private void getShowMedicine() {
        showDialogChooseMedicine();
    } // lập toa thuốc
    private void getShowSick() {
        showDialogChooseSick();
    } // chẩn đoán
    private void showDialogChooseMedicine(){
        view = LayoutInflater.from(context).inflate(R.layout.dialog_add_medicine, relativeLayout);
        mapping2(view);
        fillDataMedicine();
        builder1.setView(view);
        alertDialog = builder1.create();
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        btnsearch.setOnClickListener(view1 -> {getSearchMedicine();});
        btn00.setOnClickListener(view1 -> alertDialog.dismiss());
        alertDialog.show();
    } // hiện dialog chọn thuốc
    private void fillDataMedicine() {
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC GetMedicineData "
            );
            drugs = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    HashMap<String, String> drug = new HashMap<>();
                    drug.put("id", rs.getString("id").toUpperCase());
                    drug.put("name", rs.getString("name").toUpperCase());
                    drugs.add(drug);
                }
                adapter = new DrugAdapter(context, drugs);
                adapter.setDianosiDetail(this);
                lv3.setAdapter(adapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }
    } // hiện ds thuốc
    private void getSearchMedicine() {
        if(ed1.getText().toString().isEmpty()){
            lv3.setAdapter(adapter);
        }
        else {
        searchMedicine(ed1.getText().toString().trim());
        }
    } // thực hiện tìm kiếm thuốc
    private void searchMedicine(String text){
        List<HashMap<String, String>> searchResults = new ArrayList<>();
        for (HashMap<String, String> drug : drugs) {
            String drugName = drug.get("name");
            String drugCode = drug.get("id");
            if (drugName.toLowerCase().contains(text.toLowerCase()) || drugCode.toLowerCase().contains(text.toLowerCase())) {
                searchResults.add(drug);
            }
        }
        DrugAdapter searchAdapter = new DrugAdapter(context, searchResults);
        lv3.setAdapter(searchAdapter);
    } // hàm tìm thuốc
    private void showDialogChooseSick(){
        view1 = LayoutInflater.from(context).inflate(R.layout.dialog_add_medicine, relativeLayout);
        mapping3(view1);
        fillDataSick();
        builder2.setView(view1);
        alertDialog2 = builder2.create();
        if(alertDialog2.getWindow() != null){
            alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        btnsearch.setOnClickListener(view1 -> {getSearchSick();});
        btn00.setOnClickListener(view1 -> alertDialog2.dismiss());
        alertDialog2.show();
    } // hiện dialog chọn bệnh
    private void fillDataSick() {
        try {
            conn = new Connect();
            ResultSet rs = conn.read("EXEC GetSickData "
            );
            sicks = new ArrayList<>();
            if( rs!= null) {
                while (rs.next()) {
                    HashMap<String, String> sick = new HashMap<>();
                    sick.put("id", rs.getString("id").toUpperCase());
                    sick.put("name", rs.getString("name").toUpperCase());
                    sicks.add(sick);
                }
                sickAdapter = new SickAdapter(context, sicks);
                sickAdapter.setDianosiDetail(this);
                lv4.setAdapter(sickAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            conn.closeConnection();
        }

    } // hiện ds bệnh
    private void getSearchSick(){
        if(ed1.getText().toString().isEmpty()){
            lv4.setAdapter(sickAdapter);
        }
        else {
            searchSick(ed1.getText().toString().trim());
        }
    } // tìm bệnh
    private void searchSick(String text){
        List<HashMap<String, String>> searchResults = new ArrayList<>();
        for (HashMap<String, String> item : sicks) {
            String Name = item.get("name");
            String Code = item.get("id");
            if (Name.toLowerCase().contains(text.toLowerCase()) || Code.toLowerCase().contains(text.toLowerCase())) {
                searchResults.add(item);
            }
        }
        SickAdapter searchAdapter = new SickAdapter(context, searchResults);
        lv4.setAdapter(searchAdapter);
    } // hàm tìm bệnh
    private void mapping1(View view) {
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv6 = view.findViewById(R.id.tv6);
        tv7 = view.findViewById(R.id.tv7);
        tv8 = view.findViewById(R.id.tv8);
        tv9 = view.findViewById(R.id.tv9);

        img1 = view.findViewById(R.id.img1);

        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        btn4 = view.findViewById(R.id.btn4);
        btn5 = view.findViewById(R.id.btn5);
        btn6 = view.findViewById(R.id.btn6);
        btn = view.findViewById(R.id.btn);

        ed0 =  view.findViewById(R.id.ed1);

        lv1 = view.findViewById(R.id.lv1);
        lv2 = view.findViewById(R.id.lv2);

        ln1 = view.findViewById(R.id.ln1);
        ln2 = view.findViewById(R.id.ln2);
        ln3 = view.findViewById(R.id.ln3);

        relativeLayout = view.findViewById(R.id.relative);
    } // ánh xạ
    private void mapping2(View view) {
        lv3 = view.findViewById(R.id.lv3);
        ed1 = view.findViewById(R.id.ed1);
        btn00 =view.findViewById(R.id.btn99);
        btnsearch=view.findViewById(R.id.btn1);
    } // ánh xạ dialog chọn thuốc
    private void mapping3(View view) {
        lv4 = view.findViewById(R.id.lv3);
        ed1 = view.findViewById(R.id.ed1);
        ed3 = view.findViewById(R.id.ed2);
        btn00 =view.findViewById(R.id.btn99);
        btnsearch=view.findViewById(R.id.btn1);
    } // ánh xạ dialog chẩn đoán
}
