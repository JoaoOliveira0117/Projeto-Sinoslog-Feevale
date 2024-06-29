package com.example.projetofeevale.ui.meusPins.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetofeevale.R;
import com.example.projetofeevale.data.model.response.OccurrenceResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.OccurrenceRepository;
import com.example.projetofeevale.activities.DetailActivity;

import java.util.List;

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.ViewHolder> {

    private final List<OccurrenceResponse> occurrences;

    public PinAdapter(List<OccurrenceResponse> occurrences) {
        this.occurrences = occurrences;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OccurrenceResponse occurrence = occurrences.get(position);

        if (occurrence.getImageUrl() != null) {
            new OccurrenceRepository().getOccurrenceImage(occurrence.get_id(), new ApiCallback<Bitmap>() {

                @Override
                public void onSuccess(Bitmap data) {
                    holder.imageView.setImageBitmap(data);
                }

                @Override
                public void onFailure(String message, String cause, Throwable t) {

                }
            } );
        }
        holder.titleTextView.setText(occurrence.getTitle());
        holder.typeTextView.setText(occurrence.getType());
        holder.dataTextView.setText(occurrence.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);

                intent.putExtra("id", occurrence.get_id());
                intent.putExtra("endereco", occurrence.getAddress());
                intent.putExtra("latitude", occurrence.getLatitude());
                intent.putExtra("longitude", occurrence.getLongitude());
                intent.putExtra("tipo", occurrence.getType());
                intent.putExtra("dataHora", occurrence.getDate());
                intent.putExtra("titulo", occurrence.getTitle());
                intent.putExtra("descricao", occurrence.getDescription());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return occurrences.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleTextView;
        public TextView typeTextView;
        public TextView dataTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            typeTextView = itemView.findViewById(R.id.type_text_view);
            dataTextView = itemView.findViewById(R.id.data_text_view);
        }
    }
}