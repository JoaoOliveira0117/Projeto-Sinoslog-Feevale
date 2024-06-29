package com.example.projetofeevale.ui.createOccurrence.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.projetofeevale.BuildConfig;
import com.example.projetofeevale.R;
import com.example.projetofeevale.data.model.request.OccurrenceRequest;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OccurrenceInfo extends BaseOccurrence {
    private static final int HANDLER_DELAY = 500;
    private OccurrenceRequest occurrenceRequest;
    private boolean canShowDropdown = true;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable workRunnable;
    private PlacesClient placesClient;
    private TextView addressLabel;
    private TextView occurrenceTypeLabel;
    private TextView occurrenceDateLabel;
    private AutoCompleteTextView address;
    private EditText occurrenceDate;
    private Spinner occurrenceType;


    public OccurrenceInfo() {}

    public OccurrenceInfo(OccurrenceRequest occurrenceRequest) {
        this.occurrenceRequest = occurrenceRequest;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY);
        View view = inflater.inflate(R.layout.form_create_occurrence_info, container, false);

        addressLabel = view.findViewById(R.id.occurrenceAddressLabel);
        address = view.findViewById(R.id.occurrenceAddress);
        occurrenceTypeLabel = view.findViewById(R.id.occurrenceTypeLabel);
        occurrenceType = view.findViewById(R.id.occurrenceType);
        occurrenceDateLabel = view.findViewById(R.id.occurrenceDateLabel);
        occurrenceDate = view.findViewById(R.id.occurrenceDate);

        setupPlacesAutoComplete();

        occurrenceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        return view;
    }

    private void setupPlacesAutoComplete() {
        placesClient = Places.createClient(requireContext());
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        List<String> placeIdSuggestions = new ArrayList<>();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(workRunnable);
            }

            @Override
            public void afterTextChanged(final Editable s) {
                workRunnable = new Runnable() {
                    @Override
                    public void run() {
                        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                                .setSessionToken(token)
                                .setQuery(s.toString())
                                .build();

                        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
                            List<String> suggestions = new ArrayList<>();
                            placeIdSuggestions.clear();
                            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                placeIdSuggestions.add(prediction.getPlaceId());
                                suggestions.add(prediction.getFullText(null).toString());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions);
                            address.setAdapter(adapter);
                            if (canShowDropdown && address.isFocused()) {
                                address.showDropDown();
                            }
                        });
                    }
                };
                handler.postDelayed(workRunnable, HANDLER_DELAY);
            }
        };

        address.addTextChangedListener(textWatcher);

        address.setOnItemClickListener((parent, contextView, position, id) -> {
            canShowDropdown = false;
            findPlaceFromAddress(placeIdSuggestions.get(position));

            handler.postDelayed(() -> canShowDropdown = true, HANDLER_DELAY * 2);
        });
    }

    private void findPlaceFromAddress(String address) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(address, placeFields);

        placesClient.fetchPlace(request).addOnCompleteListener((response) -> {
            if (response.isSuccessful()) {
                FetchPlaceResponse fetchPlaceResponse = response.getResult();
                Place place = fetchPlaceResponse.getPlace();
                occurrenceRequest.setLatitude(place.getLatLng().latitude);
                occurrenceRequest.setLongitude(place.getLatLng().longitude);
            } else {
                Toast.makeText(getActivity(), "Não foi possível selecionar o endereço", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendar.set(Calendar.MINUTE, minute);

                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                        String selectedDateTime = sdf.format(calendar.getTime());

                                        occurrenceDate.setText(selectedDateTime);
                                    }
                                }, hourOfDay, minute, true);
                        timePickerDialog.show();
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    public int getLabelColor(boolean isError) {
        if(isError){
            return Color.RED;
        }

        return ContextCompat.getColor(requireContext(), R.color.colorPrimary);
    }

    @Override
    public boolean hasEmptyFields() {
        boolean emptyAddress = address.getText().toString().equals("");
        boolean emptyType = occurrenceType.getSelectedItem().toString().equals("");

        addressLabel.setTextColor(getLabelColor(emptyAddress));

        return emptyAddress || emptyType;
    }

    @Override
    public void fillForm() {
        occurrenceRequest.setAddress(address.getText().toString());
        occurrenceRequest.setType(occurrenceType.getSelectedItem().toString());
        occurrenceRequest.setDate(occurrenceDate.getText().toString());
    }
}