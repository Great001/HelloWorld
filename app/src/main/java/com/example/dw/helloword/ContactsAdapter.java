package com.example.dw.helloword;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by DW on 2016/7/22.
 */
public class ContactsAdapter extends BaseAdapter {


    private List<Name>  listName;
    private List<String>  listNumber;
    private Context context;

    private static final int TYPE_ITEM=0;
    private static final int TYPE_DIVIDER=1;

    public ContactsAdapter(Context context,List<Name> listName,List<String> listNumber){
        this.context=context;
        this.listName=listName;
        this.listNumber=listNumber;

    }

    @Override
    public int getCount() {
        return listName.size();
    }

    @Override
    public Object getItem(int position) {
        return listName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder=null;
        int type = getItemViewType(position);
        if (type == TYPE_ITEM) {
            {
            if (convertView == null) {
                        convertView = LayoutInflater.from(context).inflate(R.layout.contacts_item, null);
                        holder = new MyViewHolder();
                        holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                        holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_phone_number);
                        convertView.setTag(holder);
                    } else {
                        holder = (MyViewHolder) convertView.getTag();
                    }
                    holder.tvName.setText(listName.get(position).name);
                    holder.tvNumber.setText(listNumber.get(position));
                }
            }else{
                    convertView=LayoutInflater.from(context).inflate(R.layout.gray_view_divider,null);
            TextView tvSortOrder=(TextView)convertView.findViewById(R.id.tv_letter_sort_order);
            String  str=listName.get(position).order+"";
            tvSortOrder.setText(str);
        }

        return convertView;
    }


    @Override
    public int getItemViewType(int position) {
        if(listName.get(position).name!="#")
        return TYPE_ITEM;
        else
            return TYPE_DIVIDER;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class MyViewHolder{
        TextView tvName,tvNumber;
    }

}
