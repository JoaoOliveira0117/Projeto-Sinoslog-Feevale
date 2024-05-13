package com.example.projetofeevale.fragments.FormCreatePin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projetofeevale.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormCreatePin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormCreatePin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int[] steps = new int[]{
            R.layout.form_create_pin_step_1,
            R.layout.form_create_pin_step_2,
            R.layout.form_create_pin_step_3
    };
    private int currentStep;

    private ActivityResultLauncher activityResultLauncher;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    public FormCreatePin() {
        // Required empty public constructor
    }

    public FormCreatePin(int step) {
        currentStep = step;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormCreatePinStepOne.
     */
    // TODO: Rename and change types and number of parameters
    public static FormCreatePin newInstance(String param1, String param2) {
        FormCreatePin fragment = new FormCreatePin();
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

        // Registra o ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Lógica para lidar com o resultado
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(steps[currentStep], container, false);

        handleStepOne(view);

        return view;
    }

    private void handleStepOne(View view) {
        Button button = view.findViewById(R.id.upload_image_button);

        final CharSequence[] items = { "Tirar Foto", "Escolher da biblioteca", "Cancelar"};
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
                    startActivity(intent);
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
    }

    public static int getFormStepsAmount() {
        return steps.length;
    }
}