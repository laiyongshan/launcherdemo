package com.yeohe.mylauncherapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TypeAdapter extends BaseAdapter {
    private List<Drawable> types=new ArrayList<Drawable>();
    private LayoutInflater inflater;
    private List<String> strArr=new ArrayList<String>();
    private ViewHolder holder;

    public TypeAdapter(List<Drawable> types, Context context, List<String> strArr, int model) {
        this.types = types;
        inflater = LayoutInflater.from(context);
        this.strArr=strArr;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return types.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.type_gridview_item, null);

            holder = new ViewHolder(convertView);

            holder.iv_type_icon = (ImageView) convertView.findViewById(R.id.iv_type_icon);
            holder.iv_type_tv = (TextView) convertView.findViewById(R.id.iv_type_tv);

//            Integer resId = (Integer) getItem(position);
            holder.iv_type_icon.setImageDrawable(types.get(position));

            holder.iv_type_tv.setText(strArr.get(position));


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.update(position);

        return convertView;
    }

    private class ViewHolder {
        ImageView iv_type_icon;
        TextView iv_type_tv;
        private LinearLayout layout;
        View view;

        public ViewHolder(View convertView) {
            view=convertView.findViewById(R.id.line);
            layout=(LinearLayout) convertView.findViewById(R.id.layout);
        }

        private void update(int position) {
            int i=0;
            i=position%3;
            switch (i) {
                case 0:
                    //每列第一个item不包括分割线的layout右内边距20
//                    layout.setPadding(0, 0, 5, 0);
//                    view.setPadding(0, 0, 5, 0);
                    break;
                case 1:
                    //每列第二个item不包括分割线的layout左、右内边距各10
//                    layout.setPadding(5, 0, 5, 0);
                    break;

//                case 2:
//                    //每列第二个item不包括分割线的layout左、右内边距各10
//                    layout.setPadding(5, 0, 5, 0);
//                    break;

                case 2:
                    //每列第三个item不包括分割线的layout左内边距20；必须使三个item的宽度一致
//                    layout.setPadding(5, 0, 0, 0);
//                    view.setPadding(5, 0, 0, 0);
                    break;
                default:
                    break;
            }

            setLine(position, view);

        }

        private void setLine(int position, View view) {
            int i = 0;
            i = types.size() %3;
            if (position + i + 1 > types.size()) {
                //最后一行分割线隐藏
                view.setVisibility(View.VISIBLE);
            }

        }
    }
}
