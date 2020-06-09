package com.abutair.adventurerkids.activity;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.abutair.adventurerkids.R;
import java.util.ArrayList;

public class activity_adapter  extends  RecyclerView.Adapter<activity_adapter.ViewHolder> {
    private ArrayList<String> mHour = new ArrayList<>();
    private  ArrayList<String>mActions = new ArrayList<>();
    private Context mContext;
    public activity_adapter(  Context context  ,ArrayList<String> mHour, ArrayList<String> mActions ) {
        this.mHour = mHour;
        this.mActions = mActions;
        this.mContext= context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext() ).inflate(R.layout.activity_recyclerview_item,viewGroup,false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.hour.setText(mHour.get(i));
        viewHolder.action.setText(mActions.get(i));
    }

    @Override
    public int getItemCount() {
        return mHour.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        TextView hour , action;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hour  = itemView.findViewById(R.id.activity_hour);
            action = itemView.findViewById(R.id.Action);

        }

    }
}
