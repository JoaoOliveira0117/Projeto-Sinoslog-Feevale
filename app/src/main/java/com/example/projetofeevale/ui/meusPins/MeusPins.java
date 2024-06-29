package com.example.projetofeevale.ui.meusPins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetofeevale.R;
import com.example.projetofeevale.ui.meusPins.adapter.PinAdapter;
import com.example.projetofeevale.data.model.response.OccurrenceResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.OccurrenceRepository;

import java.util.List;

public class MeusPins extends Fragment {
    private RecyclerView recyclerView;
    private PinAdapter adapter;

    public MeusPins() {
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

        new OccurrenceRepository().getAllOccurrences(new ApiCallback<List<OccurrenceResponse>>() {
            @Override
            public void onSuccess(List<OccurrenceResponse> data) {
                adapter = new PinAdapter(data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String message, String cause, Throwable t) {

            }
        });

        return view;
    }
}
