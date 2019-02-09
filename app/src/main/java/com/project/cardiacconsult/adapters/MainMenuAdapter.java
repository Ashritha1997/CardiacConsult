package com.project.cardiacconsult.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.cardiacconsult.R;

public class MainMenuAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] menuLabels = {"ECG", "DETAILS", "HISTORY","ECG", "DETAILS", "HISTORY","ECG", "DETAILS", "HISTORY"};
    private final int[] menuIds = {R.drawable.ic_hospital, R.drawable.ic_body_points, R.drawable.ic_ecg_history,R.drawable.ic_hospital, R.drawable.ic_body_points, R.drawable.ic_ecg_history,R.drawable.ic_hospital, R.drawable.ic_body_points, R.drawable.ic_ecg_history};

    //Constructor
    public MainMenuAdapter(Context c){
        mContext = c;
    }
    @Override
    public int getCount() {
        return menuLabels.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            itemView = inflater.inflate(R.layout.grid_menu_item_view, null);
            TextView itemLabel = itemView.findViewById(R.id.itemLabel);
            ImageView itemImage = itemView.findViewById(R.id.itemImage);
            itemLabel.setText(menuLabels[position]);
            itemImage.setImageResource(menuIds[position]);
        } else {
            itemView = convertView;
        }
        return itemView;
    }

}
