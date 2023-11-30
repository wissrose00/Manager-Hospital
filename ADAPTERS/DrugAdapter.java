package com.example.quanlibenhvien.ADAPTERS;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlibenhvien.FRAGMENTDOCTOR.DianosiDetail;
import com.example.quanlibenhvien.R;

import java.util.HashMap;
import java.util.List;

public class DrugAdapter extends ArrayAdapter<HashMap<String, String>> {
    private DianosiDetail dianosiDetail;
    public void setDianosiDetail(DianosiDetail dianosiDetail) {
        this.dianosiDetail = dianosiDetail;
    }
    public DrugAdapter(Context context, List<HashMap<String, String>> drugs) {
        super(context, 0, drugs);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_medicine, parent, false);
        }

        HashMap<String, String> currentDrug = getItem(position);

        final ImageButton imgButtonSub = itemView.findViewById(R.id.btnsub);
        final ImageButton imgButtonPlus = itemView.findViewById(R.id.btnplus);
        final ImageButton imageButtonAdd = itemView.findViewById(R.id.btnadd);
        final EditText editText = itemView.findViewById(R.id.ed1);

        final TextView tvId = itemView.findViewById(R.id.tv1);
        final TextView tvName = itemView.findViewById(R.id.tv2);


        tvId.setText(currentDrug.get("id"));
        tvName.setText(currentDrug.get("name"));

        imgButtonSub.setOnClickListener(v -> {
            int currentValue = Integer.parseInt(editText.getText().toString());
            if (currentValue > 1) {
                currentValue--;
                editText.setText(String.valueOf(currentValue));
            }
        });
        imgButtonPlus.setOnClickListener(v -> {
            int currentValue = Integer.parseInt(editText.getText().toString());
            currentValue++;
            editText.setText(String.valueOf(currentValue));
        });
        imageButtonAdd.setOnClickListener(v -> {
            // Lấy thông tin thuốc được chọn
            String drugId = currentDrug.get("id");
            String drugName = currentDrug.get("name");
            String quantity = editText.getText().toString();

            // Kiểm tra xem thuốc đã tồn tại trong danh sách selectedDrugs chưa
            boolean isExistingDrug = false;
            for (HashMap<String, String> selectedDrug : dianosiDetail.selectedDrugs) {
                if (selectedDrug.get("id").equals(drugId)) {
                    // Nếu thuốc đã tồn tại, tăng số lượng
                    int currentQuantity = Integer.parseInt(selectedDrug.get("quantity"));
                    int newQuantity = currentQuantity + Integer.parseInt(quantity);
                    selectedDrug.put("quantity", String.valueOf(newQuantity));
                    isExistingDrug = true;
                    break;
                }
            }

            // Nếu thuốc không tồn tại, thêm mới vào danh sách
            if (!isExistingDrug) {
                HashMap<String, String> selectedDrug = new HashMap<>();
                selectedDrug.put("id", drugId);
                selectedDrug.put("name", drugName); // Thêm tên thuốc vào danh sách
                selectedDrug.put("quantity", quantity);
                dianosiDetail.selectedDrugs.add(selectedDrug);
            }

            ListView listViewSelectedDrugs = dianosiDetail.getView().findViewById(R.id.lv2);
            SelectedDrugAdapter adapter = new SelectedDrugAdapter(dianosiDetail.getContext(), dianosiDetail.selectedDrugs);
            listViewSelectedDrugs.setAdapter(adapter);
        });


        return itemView;
    }
}