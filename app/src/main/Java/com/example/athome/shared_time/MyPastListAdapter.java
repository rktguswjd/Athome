package com.example.athome.shared_time;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.athome.R;
import com.example.athome.reservation_list.ItemNowReservData;

import java.util.ArrayList;

public class MyPastListAdapter  extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<ItemMyPastListData> data = null;
    private int layout;

    public MyPastListAdapter(Context context, int layout, ArrayList<ItemMyPastListData> data)
    {
        this.inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout=layout;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public String getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    //리턴할때 각 아이템레이아웃을 넘겨주면 화면에 표시
    //포지션으로 현재 몇번째 아이템이 표시해야되는지 알 수 있음
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.my_past_list_item, parent, false);
        }

        ItemMyPastListData itemMyPastListData=data.get(position);

        TextView textStartDate = (TextView) convertView.findViewById(R.id.my_past_reserv_start_date);
        TextView textEndDate = (TextView) convertView.findViewById(R.id.my_past_reserv_end_date);
        TextView textStartTime = (TextView) convertView.findViewById(R.id.my_past_reserv_start_time);
        TextView textEndTime = (TextView) convertView.findViewById(R.id.my_past_reserv_end_time);
        TextView textCarNumber = (TextView) convertView.findViewById(R.id.user_past_reserv_car_number);
        TextView textPhoneNumber = (TextView) convertView.findViewById(R.id.user_past_reserv_phone_number);



        textStartDate.setText(itemMyPastListData.getPastReservStartDate());
        textEndDate.setText(itemMyPastListData.getPastReservEndDate());
        textStartTime.setText(itemMyPastListData.getPastReservStartTime());
        textEndTime.setText(itemMyPastListData.getPastReservStartTime());
        textCarNumber.setText(itemMyPastListData.getPastReservCarNumber());
        textPhoneNumber.setText(itemMyPastListData.getPastReservPhoneNumber());


        return convertView;
    }

}
