package com.project.cardiacconsult.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.cardiacconsult.R;
import com.project.cardiacconsult.activities.DiagnosisActivity;
import com.project.cardiacconsult.activities.ECGActivity;
import com.project.cardiacconsult.activities.FeedbackActivity;
import com.project.cardiacconsult.activities.HistoryActivity;
import com.project.cardiacconsult.activities.PhysicianSearchActivity;

public class MainMenuAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] menuLabels = {"ECG", /*"DIAGNOSIS", "HISTORY",*/"PHYSICIAN SEARCH", "FEEDBACK"};
    private final int[] menuIds = {R.drawable.ic_cardiogram,/* R.drawable.ic_body_points, R.drawable.ic_clinic_history,*/R.drawable.ic_doctor_search, R.drawable.ic_feedback_icon};

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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv= view.findViewById(R.id.itemLabel);
               // Toast.makeText(mContext, "Clicked: " + tv.getText(),Toast.LENGTH_LONG).show();
                switch (tv.getText().toString())
                {
                    case "ECG":
                        Intent i1=  new Intent(mContext.getApplicationContext(),ECGActivity.class);
                        mContext.startActivity(i1);
                        break;

                    case "DIAGNOSIS":
                        Intent i2=  new Intent(mContext.getApplicationContext(),DiagnosisActivity.class);
                        mContext.startActivity(i2);
                        break;

                    case "HISTORY":
                        Intent i3=  new Intent(mContext.getApplicationContext(),HistoryActivity.class);
                        mContext.startActivity(i3);
                        break;

                    case "PHYSICIAN SEARCH":
                        Intent i4=  new Intent(mContext.getApplicationContext(),PhysicianSearchActivity.class);
                        mContext.startActivity(i4);
                        break;

                    case "FEEDBACK":
                        Intent i5=  new Intent(mContext.getApplicationContext(),FeedbackActivity.class);
                        mContext.startActivity(i5);
                        break;

                }
                //Intent intent
            }
        });


        return itemView;


    }


}
