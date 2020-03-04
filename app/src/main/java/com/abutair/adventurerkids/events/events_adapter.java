package com.abutair.adventurerkids.events;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abutair.adventurerkids.R;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class events_adapter  extends  RecyclerView.Adapter<events_adapter.ViewHolder> {
    private Context mContext ;
    private  ArrayList<String> Title;
    private ArrayList<String> Images;

    private  ArrayList<String> Desc;

    public events_adapter(Context mContext, ArrayList<String> title, ArrayList<String> images, ArrayList<String> desc) {
        this.mContext = mContext;
        Title = title;
        Images = images;
        Desc = desc;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_events_item,viewGroup,false);
        ViewHolder viewHolder =  new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        StorageReference pathReference= FirebaseStorage.getInstance().getReference().child(Images.get(i));
        Glide.with(mContext).load(pathReference).placeholder(R.drawable.blank).into(viewHolder.img);
        viewHolder.desc.setText(Desc.get(i));
        viewHolder.title.setText(Title.get(i));
    }

    @Override
    public int getItemCount() {
        return Desc.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img ;
        TextView title,desc ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           img = itemView.findViewById(R.id.events_img);
           title = itemView.findViewById(R.id.events_title);
           desc = itemView.findViewById(R.id.events_desc);
        }
    }
}
