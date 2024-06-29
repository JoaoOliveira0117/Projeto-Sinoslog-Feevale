package com.example.projetofeevale.ui.contatos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.projetofeevale.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Contatos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Contatos extends Fragment {

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Contatos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContatoFragment.
     */
    public static Contatos newInstance(String param1, String param2) {
        Contatos fragment = new Contatos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contato, container, false);

        // Lista de contatos fixa
        final String[] contacts = {"Bombeiros", "Brigada Militar", "Disque Denúncia", "Disque Saúde",
                "Canil Municipal", "COMUSA", "Defesa Civil", "Guarda Municipal", "RGE Sul"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, contacts);
        ListView listView = view.findViewById(R.id.listView);
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

        return view;
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
            case "Canil Municipal":
                return "996832117";
            case "COMUSA":
                return "08006000115";
            case "Defesa Civil":
                return "30979408";
            case "Disque Denúncia":
                return "32885100";
            case "Disque Saúde":
                return "0800611997";
            case "Guarda Municipal":
                return "35248737";
            case "RGE Sul":
                return "08007077272";
            default:
                return null;
 }
}
}