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
    private final String[] textview;
    private final int[] Imageid;



/*    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.logo, R.drawable.logo,
            R.drawable.logo
    };*/

    //Constructor
    public MainMenuAdapter(Context c,String[] textview, int[] Imageid){
        mContext = c;
        this.Imageid=Imageid;
        this.textview=textview;
    }
    @Override
    public int getCount() {
        return textview.length;
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
        //ImageView imageView = new ImageView(mContext);
        //imageView.setImageResource(mThumbIds[position]);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
        //return imageView;

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.row_data, null);
            TextView textView = (TextView) grid.findViewById(R.id.tv1);
            ImageView imageView = (ImageView)grid.findViewById(R.id.images);
            textView.setText(textview[position]);
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }

}
