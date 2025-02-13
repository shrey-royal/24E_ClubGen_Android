package com.company.proapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ItemModel> items;

    public CustomAdapter(Context context, ArrayList<ItemModel> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.imageView = view.findViewById(R.id.iv_item);
            holder.titleView = view.findViewById(R.id.tv_title);
            holder.descriptionView = view.findViewById(R.id.tv_desc);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ItemModel item = items.get(i);
        holder.imageView.setImageResource(item.getImageResId());
        holder.titleView.setText(item.getTitle());
        holder.descriptionView.setText(item.getDescription());

        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descriptionView;
    }
}
