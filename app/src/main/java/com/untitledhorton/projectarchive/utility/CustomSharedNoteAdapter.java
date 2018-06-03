package com.untitledhorton.projectarchive.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.untitledhorton.projectarchive.R;
import com.untitledhorton.projectarchive.model.Note;
import com.untitledhorton.projectarchive.model.SharedNote;

import java.util.ArrayList;

public class CustomSharedNoteAdapter extends BaseAdapter{

    Context mContext;
    ArrayList<SharedNote> sharedNotes;
    LayoutInflater inflater;

    public CustomSharedNoteAdapter(Context mContext, ArrayList<SharedNote> sharedNotes) {
      this.mContext = mContext;
      this.sharedNotes = sharedNotes;
    }

    @Override
    public int getCount() {
        return sharedNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return sharedNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(inflater==null)
        {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.custom_note_row,parent,false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        ImageView ivPriority = convertView.findViewById(R.id.ivPriority);
        ImageView ivStatus = convertView.findViewById(R.id.ivStatus);

        tvTitle.setText(sharedNotes.get(position).getTitle());
        switch(sharedNotes.get(position).getPriority()){
            case "High":
                ivPriority.setImageResource(R.drawable.square_red);
                break;
            case "Medium":
                ivPriority.setImageResource(R.drawable.square_orange);
                break;
            case "Low":
                ivPriority.setImageResource(R.drawable.square_yellow);
                break;
        }

        switch(sharedNotes.get(position).getStatus()){
            case "done":
                ivStatus.setImageResource(R.drawable.done);
                break;
            case "notDone":
                ivStatus.setImageResource(R.drawable.not_done);
                break;
        }
        notifyDataSetChanged();
        return convertView;
    }

}
