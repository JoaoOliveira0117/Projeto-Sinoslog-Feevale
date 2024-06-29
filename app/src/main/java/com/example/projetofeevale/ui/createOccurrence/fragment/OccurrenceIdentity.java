package com.example.projetofeevale.ui.createOccurrence.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projetofeevale.R;
import com.example.projetofeevale.data.model.request.OccurrenceRequest;

public class OccurrenceIdentity extends BaseOccurrence {
    private OccurrenceRequest occurrenceRequest;
    private TextView occurrenceNameLabel;
    private EditText occurrenceName;
    private EditText occurrenceDescription;

    public OccurrenceIdentity() {}
    public OccurrenceIdentity(OccurrenceRequest occurrenceRequest) {
        this.occurrenceRequest = occurrenceRequest;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_create_occurrence_identity, container, false);

        occurrenceName = view.findViewById(R.id.editTextName);
        occurrenceDescription = view.findViewById(R.id.editTextAdditionalInfo);

        occurrenceNameLabel = view.findViewById(R.id.editTextNameLabel);

        return view;
    }

    private int getLabelColor(boolean isError) {
        if(isError){
            return Color.RED;
        }

        return ContextCompat.getColor(requireContext(), R.color.colorPrimary);
    }

    @Override
    public boolean hasEmptyFields() {
        boolean emptyName = occurrenceName.getText().toString().equals("");

        occurrenceNameLabel.setTextColor(getLabelColor(emptyName));

        return emptyName;
    }

    @Override
    public void fillForm() {
        occurrenceRequest.setTitle(occurrenceName.getText().toString());
        occurrenceRequest.setDescription(occurrenceDescription.getText().toString());
    }
}