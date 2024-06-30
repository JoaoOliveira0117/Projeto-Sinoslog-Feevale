package com.example.projetofeevale.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetofeevale.R;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.OccurrenceRepository;
import com.example.projetofeevale.services.Auth;

public class DetailActivity extends SislogActivity {
    private Auth authService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        authService = new Auth(this);

        ImageView imageView = findViewById(R.id.image_view);
        TextView enderecoTextView = findViewById(R.id.endereco_text_view);
        TextView tipoTextView = findViewById(R.id.tipo_text_view);
        TextView dataHoraTextView = findViewById(R.id.data_hora_text_view);
        TextView tituloTextView = findViewById(R.id.titulo_text_view);
        TextView descricaoTextView = findViewById(R.id.descricao_text_view);

        ImageButton imageButton = findViewById(R.id.back_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("id");
            String endereco = extras.getString("endereco");
            String tipo = extras.getString("tipo");
            String dataHora = extras.getString("dataHora");
            String titulo = extras.getString("titulo");
            String descricao = extras.getString("descricao");

            new OccurrenceRepository(this).getOccurrenceImage(id, new ApiCallback<Bitmap>() {

                @Override
                public void onSuccess(Bitmap data) {
                    imageView.setImageBitmap(data);
                }

                @Override
                public void onFailure(String message, String cause, Throwable t) {

                }
            });

            enderecoTextView.setText(endereco);
            tipoTextView.setText(tipo);
            dataHoraTextView.setText(dataHora);
            tituloTextView.setText(titulo);
            descricaoTextView.setText(descricao);
        }
    }

    public Auth getAuthService() {
        return authService;
    }
}