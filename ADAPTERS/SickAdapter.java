package com.example.quanlibenhvien.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SickAdapter extends ArrayAdapter<HashMap<String, String>> {
    private DianosiDetail dianosiDetail;
    public void setDianosiDetail(DianosiDetail dianosiDetail) {
        this.dianosiDetail = dianosiDetail;
    }
    public SickAdapter(Context context, List<HashMap<String, String>> drugs) {
        super(context, 0, drugs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_sick, parent, false);
        }

        HashMap<String, String> currentDrug = getItem(position);

        final ImageButton imageButtonAdd = itemView.findViewById(R.id.btnadd);
        final TextView tvId = itemView.findViewById(R.id.tv1);
        final TextView tvName = itemView.findViewById(R.id.tv2);

        tvId.setText(currentDrug.get("id"));
        tvName.setText(currentDrug.get("name"));

        imageButtonAdd.setOnClickListener(v -> {
            String drugId = currentDrug.get("id");
            String drugName = currentDrug.get("name");
            boolean isExistingDrug = false;
            for (HashMap<String, String> selectedDrug : dianosiDetail.selectedSicks) {
                if (selectedDrug.get("id").equals(drugId)) {
                    isExistingDrug = true;
                    break;
                }
            }
            if (!isExistingDrug) {
                HashMap<String, String> selectedDrug = new HashMap<>();
                selectedDrug.put("id", drugId);
                selectedDrug.put("name", drugName);

                dianosiDetail.selectedSicks.add(selectedDrug);
            }

            ListView listViewSelectedDrugs = dianosiDetail.getView().findViewById(R.id.lv1);
            SelectedSickAdapter adapter = new SelectedSickAdapter(dianosiDetail.getContext(), dianosiDetail.selectedSicks);
            listViewSelectedDrugs.setAdapter(adapter);
        });


        return itemView;
    }
}