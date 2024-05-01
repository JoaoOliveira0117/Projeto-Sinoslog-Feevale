package com.example.projetofeevale.helpers;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.projetofeevale.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NumerosDeContato extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numerosde_contato);

        // Lista de contatos fixa
        final String[] contacts = {"Bombeiros","Brigada Militar", "Disque Denúncia","Disque Saúde",
                "Canil Municipal",
                "COMUSA", "Defesa Civil", "Guarda Municipal","RGE Sul"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contactName = contacts[position];
                String contactNumber = getContactNumber(contactName); // Obtenha o número de contato correspondente ao nome do contato
                if (contactNumber != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + contactNumber));
                    startActivity(intent);
                }
            }
        });
    }

    // Método para obter o número de contato correspondente ao nome do contato
    private String getContactNumber(String contactName) {
        // Aqui você pode implementar lógica para mapear o nome do contato para o número de contato desejado
        // Por simplicidade, este exemplo apenas retorna um número fixo para cada contato
        switch (contactName) {
            case "Bombeiros":
                return "35951123";
            case "Brigada Militar":
                return "190";
            case "Canil Municipal 2":
                return "996832117";
            case "COMUSA":
                return "0800 6000 115";
            case "Defesa Civil":
                return "30979408";
            case "Disque Denúncia":
                return "32885100";
            case "Disque Saúde":
                return "0800 611997";
            case "Guarda Municipal":
                return "35248737";
            case "RGE Sul":
                return "08007077272";
            default:
                return null;
        }
    }
}
