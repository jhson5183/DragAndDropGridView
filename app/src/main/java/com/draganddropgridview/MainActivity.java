package com.draganddropgridview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lib.draganddropgridview.DropAdapter;
import com.lib.draganddropgridview.DropGridView;
import com.lib.draganddropgridview.DropHolder;
import com.lib.draganddropgridview.DropModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private DropGridView mDropGridView;
    private DropAdapter mDropAdapter;
    private List<DropModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDropGridView = (DropGridView) findViewById(R.id.grid_view);

        getData();
        mDropAdapter = new NumberAdapter(this, mList);
        mDropGridView.setAdapter(mDropAdapter);
    }

    private void getData(){

        for (int i = 0; i < 300; i++){
            mList.add(new NumberModel(String.valueOf(i)));
        }

    }

    class NumberModel extends DropModel{

        private String mNumber;
        NumberModel(String number){
            mNumber = number;
        }

    }

    class NumberAdapter extends DropAdapter{

        NumberAdapter(Context context, List<DropModel> list){
            super(context, list);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            DropHolder holder = null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.adapter_number, null);
                holder = new DropHolder();
                convertView.setTag(holder);
            }else{
                holder = (DropHolder)convertView.getTag();
            }

            NumberModel model = (NumberModel)getItem(position);

            TextView textView = (TextView)convertView.findViewById(R.id.text1);
            textView.setText(model.mNumber);
            holder.item = model;

            return convertView;
        }
    }


}
