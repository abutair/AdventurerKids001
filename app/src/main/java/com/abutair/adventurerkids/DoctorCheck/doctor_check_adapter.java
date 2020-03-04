package com.abutair.adventurerkids.DoctorCheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abutair.adventurerkids.R;

import java.util.ArrayList;

public class doctor_check_adapter  extends  RecyclerView.Adapter<doctor_check_adapter.ViewHolder>{
    private ArrayList<String> Check = new ArrayList<>();
    private Context mContext;

    public doctor_check_adapter( Context mContext ,ArrayList<String> mActions) {
        this.Check = mActions;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.activity_doctorcheck_item,parent,false);

        ViewHolder holder = new ViewHolder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.check.setText(Check.get(position));
    }

    @Override
    public int getItemCount() {
        return Check.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{


        TextView check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check  = itemView.findViewById(R.id.doctor_check_report);


        }

    }
}
