package com.example.projetofeevale.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetofeevale.R;
import com.example.projetofeevale.fragments.DetailActivity;
import com.example.projetofeevale.fragments.FormCreatePin.Pin;

import java.util.List;

public class MeusPinsAdapter extends RecyclerView.Adapter<MeusPinsAdapter.ViewHolder> {

    private final List<Pin> pinList;

    public MeusPinsAdapter(List<Pin> pinList) {
        this.pinList = pinList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pin pin = pinList.get(position);
        holder.titleTextView.setText(pin.getTitulo());
        holder.typeTextView.setText(pin.getTipo());
        holder.dataTextView.setText(pin.getDataHora());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("id", pin.getId());
                intent.putExtra("imagem", pin.getImagemBytes()); // Send image bytes to detail activity
                intent.putExtra("endereco", pin.getEndereco());
                intent.putExtra("tipo", pin.getTipo());
                intent.putExtra("dataHora", pin.getDataHora());
                intent.putExtra("titulo", pin.getTitulo());
                intent.putExtra("descricao", pin.getDescricao());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pinList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView typeTextView;
        public TextView dataTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            typeTextView = itemView.findViewById(R.id.type_text_view);
            dataTextView = itemView.findViewById(R.id.data_text_view);
        }
    }
}
