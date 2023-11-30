package com.example.quanlibenhvien.ADAPTERS;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlibenhvien.R;

import java.util.HashMap;
import java.util.List;
public class SelectedSickAdapter extends ArrayAdapter<HashMap<String, String>> {
    public SelectedSickAdapter(Context context, List<HashMap<String, String>> selectedDrugs) {
        super(context, 0, selectedDrugs);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_selected_sick, parent, false);
        }
        HashMap<String, String> currentDrug = getItem(position);
        final ImageButton imageButtonDel = itemView.findViewById(R.id.btndel);
        final TextView tvId = itemView.findViewById(R.id.tv1);
        final TextView tvName = itemView.findViewById(R.id.tv2);
        tvId.setText(currentDrug.get("id"));
        tvName.setText(currentDrug.get("name"));
        imageButtonDel.setOnClickListener(v -> {
            String drugId = currentDrug.get("id");
            int itemPosition = getPosition(currentDrug);
            if (itemPosition >= 0) {
                remove(currentDrug);
            }
            notifyDataSetChanged();
        });
        return itemView;
    }
}