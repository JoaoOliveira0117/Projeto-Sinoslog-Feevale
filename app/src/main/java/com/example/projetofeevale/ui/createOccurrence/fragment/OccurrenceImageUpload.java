package com.example.projetofeevale.ui.createOccurrence.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.projetofeevale.R;
import com.example.projetofeevale.data.model.request.OccurrenceRequest;

import java.io.ByteArrayOutputStream;

public class OccurrenceImageUpload extends BaseOccurrence {
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_LIBRARY_PERMISSION = 100;
    private OccurrenceRequest occurrenceRequest;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ImageView imageView;
    private Button selecionarImagemButton;

    public OccurrenceImageUpload() {}

    public OccurrenceImageUpload(OccurrenceRequest occurrenceRequest) {
        this.occurrenceRequest = occurrenceRequest;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activityResultLauncher = buildActivityResultLauncher();

        View view = inflater.inflate(R.layout.form_create_occurrence_upload_image, container, false);

        imageView = view.findViewById(R.id.upload_image);

        selecionarImagemButton = view.findViewById(R.id.upload_image_button);
        selecionarImagemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialog().show();
            }
        });

        return view;
    }

    private boolean checkCameraPermissions() {
        boolean hasPermission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermission) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }

        return hasPermission;
    }

    private boolean checkLibraryPermissions() {
        boolean hasPermission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermission) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_LIBRARY_PERMISSION);
        }

        return hasPermission;
    }

    private ActivityResultLauncher<Intent> buildActivityResultLauncher() {
        return registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data == null) {
                            return;
                        }

                        if (data.getData() != null) {
                            imageView.setImageURI(data.getData());
                        }

                        if (data.getExtras() != null && data.getExtras().get("data") != null) {
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            imageView.setImageBitmap(imageBitmap);
                        }
                    }
                }
            }
        );
    }

    private AlertDialog.Builder buildAlertDialog() {
        final CharSequence[] items = {"Tirar Foto", "Escolher da biblioteca", "Cancelar"};
        boolean hasCameraPermissions = checkCameraPermissions();
        boolean hasLibraryPermissions = checkLibraryPermissions();

        return new AlertDialog.Builder(getActivity())
                .setTitle("Selecionar Imagem")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (hasCameraPermissions && items[which].equals("Tirar Foto")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            activityResultLauncher.launch(intent);
                        } else if (hasLibraryPermissions && items[which].equals("Escolher da biblioteca")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activityResultLauncher.launch(intent);
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void fillForm() {
        if(imageView.getDrawable() instanceof BitmapDrawable ){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);

            occurrenceRequest.setOccurrenceImage(byteArrayOutputStream);
        }
    }
}