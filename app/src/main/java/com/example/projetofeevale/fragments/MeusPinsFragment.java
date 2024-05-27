package com.example.projetofeevale.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetofeevale.R;
import com.example.projetofeevale.adapter.MeusPinsAdapter;
import com.example.projetofeevale.fragments.FormCreatePin.Pin;

import java.util.List;

public class MeusPinsFragment extends Fragment {

    private RecyclerView recyclerView;
    private MeusPinsAdapter adapter;
    private List<Pin> pinList;

    public MeusPinsFragment() {
        // Required empty public constructor
    }

    public static MeusPinsFragment newInstance(String param1, String param2) {
        MeusPinsFragment fragment = new MeusPinsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_pins, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_pins);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        DbHelper dbHelper = new DbHelper(getActivity());
        pinList = dbHelper.getData();
        adapter = new MeusPinsAdapter(pinList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
