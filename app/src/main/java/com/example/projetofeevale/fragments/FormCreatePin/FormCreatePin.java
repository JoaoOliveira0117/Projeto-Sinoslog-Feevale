package com.example.projetofeevale.fragments.FormCreatePin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.projetofeevale.BuildConfig;
import com.example.projetofeevale.MainActivity;
import com.example.projetofeevale.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FormCreatePin extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int[] steps = new int[]{
            R.layout.form_create_pin_step_1,
            R.layout.form_create_pin_step_2,
            R.layout.form_create_pin_step_3
    };
    private int currentStep;
    private ActivityResultLauncher activityResultLauncher;

    private PlacesClient placesClient;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    // Variáveis para armazenar dados do formulário

    private String address;
    private String latitude;
    private String longitude;
    private String type;
    private String dateTime;
    private String title;
    private String description;
    private byte[] imagemBytes;

    public FormCreatePin() {
        // Required empty public constructor
    }

    public FormCreatePin(int step) {
        this.currentStep = step;
    }

    public static FormCreatePin newInstance(String param1, String param2) {
        FormCreatePin fragment = new FormCreatePin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(steps[currentStep], container, false);

        if (currentStep == 0) {
            handleStepOne(view);
        } else if (currentStep == 1) {
            handleStepTwo(view);
        } else if (currentStep == 2) {
            handleStepThree(view);
        }

        return view;
    }

    private void handleStepOne(View view) {
        ImageView imageView = view.findViewById(R.id.upload_image);
        Button button = view.findViewById(R.id.upload_image_button);
        final CharSequence[] items = {"Tirar Foto", "Escolher da biblioteca", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Adicionar Imagem");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Tirar Foto")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    } else {
                        activityResultLauncher.launch(intent);
                    }
                } else if (items[which].equals("Escolher da biblioteca")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intent);
                } else if (items[which].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                if (data.getExtras() != null && data.getExtras().get("data") != null) {
                                    // Imagem da camera
                                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    imageView.setImageBitmap(imageBitmap);
                                    imageView.setPadding(0,0,0,0);
                                    imagemBytes = stream.toByteArray();
                                } else if (data.getData() != null) {
                                    // Imagem da galeria
                                    try {
                                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                        imageView.setImageBitmap(imageBitmap);
                                        imageView.setPadding(0,0,0,0);
                                        imagemBytes = stream.toByteArray();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
        );
    }

    private void handleStepTwo(View view) {
        Button buttonDate = view.findViewById(R.id.buttonDateTimePicker);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleTimePicker(v);
            }
        });

        Button buttonNext = view.findViewById(R.id.bttConfirmar2);

        Places.initialize(view.getContext(), BuildConfig.MAPS_API_KEY);

        placesClient = Places.createClient(view.getContext());

        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.editTextAddress);

        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        List<String> placeIdSuggestions = new ArrayList<>();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_dropdown_item_1line, suggestions);
                    autoCompleteTextView.setAdapter(adapter);
                    autoCompleteTextView.showDropDown();
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        autoCompleteTextView.addTextChangedListener(textWatcher);

        autoCompleteTextView.setOnItemClickListener((parent, contextView, position, id) -> {
            findPlaceFromAddress(placeIdSuggestions.get(position));
            autoCompleteTextView.dismissDropDown();
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner typeField = view.findViewById(R.id.spinnerType);
                AutoCompleteTextView addressField = view.findViewById(R.id.editTextAddress);

                if (typeField.getSelectedItemPosition() != 0 && !addressField.getText().toString().isEmpty()) {
                    type = typeField.getSelectedItem().toString();
                } else {
                    Toast.makeText(getActivity(), "Por favor, preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void handleStepThree(View view) {
        Button buttonNext = view.findViewById(R.id.bttConfirmar3);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText titleField = view.findViewById(R.id.editTextName);
                EditText descriptionField = view.findViewById(R.id.editTextAdditionalInfo);

                if (!titleField.getText().toString().isEmpty() && !descriptionField.getText().toString().isEmpty()) {
                    title = titleField.getText().toString();
                    description = descriptionField.getText().toString();
                } else {
                    Toast.makeText(getActivity(), "Por favor, preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findPlaceFromAddress(String address) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(address, placeFields);

        placesClient.fetchPlace(request).addOnCompleteListener((response) -> {
            if (response.isSuccessful()) {
                FetchPlaceResponse fetchPlaceResponse = response.getResult();
                Place place = fetchPlaceResponse.getPlace();
                this.address = place.getAddress();
                this.latitude = String.valueOf(place.getLatLng().latitude);
                this.longitude = String.valueOf(place.getLatLng().longitude);
                System.out.println(this.address);
            } else {
                Toast.makeText(getActivity(), "Não foi possível selecionar o endereço", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int getFormStepsAmount() {
        return steps.length;
    }

    private int year, month, dayOfMonth, hour, minute;

    public void scheduleTimePicker(View view) {
        initialDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(year, month, dayOfMonth, hour, minute);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        datePickerDialog.show();
        timePickerDialog.show();
    }

    private void initialDateTimeData() {
        if (year == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        updateDateTimeString();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        updateDateTimeString();
    }

    private void updateDateTimeString() {
        this.dateTime = dayOfMonth + "/" + (month + 1) + "/" + year + " " + hour + ":" + minute;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        year = month = dayOfMonth = hour = minute = 0;
    }

    // Métodos para obter os dados do formulário


    public String getAddress() {
        return address;
    }

    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }

    public String getType() {
        return type;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImagemBytes() {
        return imagemBytes;
    }

}
