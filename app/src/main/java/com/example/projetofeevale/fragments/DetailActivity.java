package com.example.projetofeevale.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.projetofeevale.R;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView enderecoTextView;
    private TextView tipoTextView;
    private TextView dataHoraTextView;
    private TextView tituloTextView;
    private TextView descricaoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.image_view);
        enderecoTextView = findViewById(R.id.endereco_text_view);
        tipoTextView = findViewById(R.id.tipo_text_view);
        dataHoraTextView = findViewById(R.id.data_hora_text_view);
        tituloTextView = findViewById(R.id.titulo_text_view);
        descricaoTextView = findViewById(R.id.descricao_text_view);

        ImageButton imageButton = findViewById(R.id.back_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String endereco = extras.getString("endereco");
            String tipo = extras.getString("tipo");
            String dataHora = extras.getString("dataHora");
            String titulo = extras.getString("titulo");
            String descricao = extras.getString("descricao");
            byte[] imagemBytes = extras.getByteArray("imagem");

            // Converter bytes da imagem em um Bitmap
            //Bitmap bitmap = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.length);

            // Definir o Bitmap no ImageView
            //imageView.setImageBitmap(bitmap);

            enderecoTextView.setText(endereco);
            tipoTextView.setText(tipo);
            dataHoraTextView.setText(dataHora);
            tituloTextView.setText(titulo);
            descricaoTextView.setText(descricao);
        }
    }
}