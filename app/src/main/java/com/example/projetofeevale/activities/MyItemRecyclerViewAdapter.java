package com.example.projetofeevale.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projetofeevale.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.projetofeevale.databinding.FragmentItemBinding;

import java.util.List;

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> values;

    public MyItemRecyclerViewAdapter(List<PlaceholderItem> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentItemBinding binding = FragmentItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaceholderItem item = values.get(position);
        holder.idView.setText(item.getId());
        holder.contentView.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView idView;
        public TextView contentView;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            idView = binding.itemNumber;
            contentView = binding.content;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }

}


