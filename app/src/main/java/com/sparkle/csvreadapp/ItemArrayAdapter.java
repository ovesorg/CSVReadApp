package com.sparkle.csvreadapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.util.ArrayList;
import java.util.List;

public class ItemArrayAdapter extends ArrayAdapter<String[]> {

    private List<String[]> scoreList = new ArrayList<>();
    private List<String[]> allscoreList = new ArrayList<>();

    static class ItemViewHolder{
        TextView name;
        TextView score;
    }
    public ItemArrayAdapter(@NonNull Context context, int resource, onItemClickListener listener) {
        super(context, resource);
        allscoreList = new ArrayList<>();
        allscoreList.addAll(scoreList);
        this.listener = listener;
    }

    public interface onItemClickListener{
        void changevalue(String string);
    }
    private onItemClickListener listener;

    public void add(String[] object) {
        allscoreList.add(object);
        super.add(object);
    }

    public void notifyList(List<String[]> getPayAccountLists) {
        this.allscoreList = getPayAccountLists;
        notifyDataSetChanged();
    }

    public void filter(String charText) {

        scoreList = new ArrayList<>();
        if (charText.length() == 0) {
            allscoreList.addAll(scoreList);
        } else {
            for (String[] wp : allscoreList) {
                if (wp[0].equals(charText)) {
                    scoreList.add(wp);
                    listener.changevalue(wp[1]);
                }
            }
            if (scoreList.size()==0){
                listener.changevalue("PAYG_ID not found");
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return this.allscoreList.size();
    }

    @Nullable
    @Override
    public String[] getItem(int position) {
        return this.allscoreList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ItemViewHolder viewHolder;
        if (row == null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_list_item,parent,false);
            viewHolder = new ItemViewHolder();
            viewHolder.name = (TextView) row.findViewById(R.id.name);
            viewHolder.score = (TextView) row.findViewById(R.id.score);
            row.setTag(viewHolder);
        }else {
            viewHolder = (ItemViewHolder) row.getTag();
        }

        String[] start = getItem(position);
        viewHolder.name.setText(start[0]);
        viewHolder.score.setText(start[1]);

        return row;
    }

}
