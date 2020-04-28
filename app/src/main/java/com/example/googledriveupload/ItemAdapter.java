package com.example.googledriveupload;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>  {

    private Context context;
    private List<ItemData> mItemcardData = new ArrayList<>();
    boolean isCheck = false;


    public ItemAdapter(Context context,List<ItemData> item) {
        this.context = context;
        mItemcardData.addAll(item);
    }

    public void addItem(List<ItemData> list){
        mItemcardData.clear();
        mItemcardData.addAll(list);
        notifyDataSetChanged();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public RadioButton mRadioButton;
        public TextView tvName;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mRadioButton = itemView.findViewById(R.id.rbtn);
            tvName = itemView.findViewById(R.id.tvFilename);


        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,null);
        ItemViewHolder ivh = new ItemViewHolder(view);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemData itemcardData = mItemcardData.get(position);

        holder.tvName.setText(itemcardData.getTvItemname());

        holder.mRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheck = !isCheck;
                holder.mRadioButton.setChecked(isCheck);
                if (isCheck){
                    Log.d("qweqwe","Hiiii");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemcardData.size();
    }

}
