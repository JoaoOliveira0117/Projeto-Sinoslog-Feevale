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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.projetofeevale.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

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
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    // Variáveis para armazenar dados do formulário

    private String address;
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
        // Inflate the layout for this fragment
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

    // Método para gerenciar o primeiro passo do formulário
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

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner typeField = view.findViewById(R.id.spinnerType);
                EditText addressField = view.findViewById(R.id.editTextAddress);

                // Verificar se os campos obrigatórios foram preenchidos
                if (typeField.getSelectedItemPosition() != 0 && !addressField.getText().toString().isEmpty()) {
                    // Se os campos estiverem preenchidos, vá para o próximo passo
                    type = typeField.getSelectedItem().toString();
                    address = addressField.getText().toString();
                    // Avançar para o próximo passo
                    // Você pode implementar a lógica aqui para avançar para o próximo passo
                } else {
                    // Se os campos não estiverem preenchidos, exiba uma mensagem de erro ou faça algo apropriado
                    // Exemplo: Mostrar uma mensagem de erro
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

                // Verificar se os campos obrigatórios foram preenchidos
                if (!titleField.getText().toString().isEmpty() && !descriptionField.getText().toString().isEmpty()) {
                    // Se os campos estiverem preenchidos, vá para o próximo passo
                    title = titleField.getText().toString();
                    description = descriptionField.getText().toString();
                    // Avançar para o próximo passo
                    // Você pode implementar a lógica aqui para avançar para o próximo passo
                } else {
                    // Se os campos não estiverem preenchidos, exiba uma mensagem de erro ou faça algo apropriado
                    // Exemplo: Mostrar uma mensagem de erro
                    Toast.makeText(getActivity(), "Por favor, preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                }
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
