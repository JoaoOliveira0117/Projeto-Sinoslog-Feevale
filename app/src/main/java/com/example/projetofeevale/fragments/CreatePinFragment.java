package com.example.projetofeevale.fragments;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetofeevale.R;
import com.example.projetofeevale.fragments.FormCreatePin.FormCreatePin;
import com.google.android.material.snackbar.Snackbar;

public class CreatePinFragment extends Fragment {

    private byte[] imagemBytes;
    private String address;
    private String latitude;
    private String longitude;
    private String type;
    private String dateTime;
    private String title;
    private String description;

    private View.OnClickListener onBack;
    private Fragment currentFragment;
    private int currentFormStep = 0;

    public CreatePinFragment() {
        // Required empty public constructor
    }

    public CreatePinFragment(View.OnClickListener onBack) {
        this.onBack = onBack;
    }

    public static CreatePinFragment newInstance(String param1, String param2) {
        CreatePinFragment fragment = new CreatePinFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_pin, container, false);

        FormCreatePin formCreatePin = new FormCreatePin(currentFormStep);
        replaceFormFragment(formCreatePin, false, false);

        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(onBack);

        Button formNextButton = view.findViewById(R.id.form_btn_next);
        Button formPrevButton = view.findViewById(R.id.form_btn_prev);

        formNextButton.setOnClickListener(onNextButtonClick(formNextButton, formPrevButton));
        formPrevButton.setOnClickListener(onPrevButtonClick(formNextButton, formPrevButton));

        buttonManager(formNextButton, currentFormStep < 2);
        buttonManager(formPrevButton, currentFormStep > 0);

        return view;
    }

    private void buttonManager(Button button, boolean enabled) {
        button.setEnabled(enabled);
        button.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    private void replaceFormFragment(Fragment fragment, boolean animated, boolean toRight) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (animated && toRight) {
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left,
                    R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right
            );
        }

        if (animated && !toRight) {
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right,
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left
            );
        }

        fragmentTransaction.replace(R.id.fragment_form_steps, fragment);
        currentFragment = fragment;
        fragmentTransaction.commit();
    }

    private View.OnClickListener onNextButtonClick(Button nextButton, Button prevButton) {
        return v -> navigateForward(nextButton, prevButton);
    }

    private View.OnClickListener onPrevButtonClick(Button nextButton, Button prevButton) {
        return v -> navigateBackward(nextButton, prevButton);
    }

    private void navigateForward(Button nextButton, Button prevButton) {
        boolean hasNextSteps = currentFormStep < FormCreatePin.getFormStepsAmount() - 1;

        saveFormData((FormCreatePin) currentFragment);

        if (hasNextSteps) {
            currentFormStep++;
            replaceFormFragment(new FormCreatePin(currentFormStep), true, true);
        } else {
            // Inserir dados no banco de dados
            insertDataToDatabase(nextButton);
        }

        nextButton.setText(hasNextSteps ? "Avançar" : "Finalizar");
        buttonManager(prevButton, true);
    }

    private void navigateBackward(Button nextButton, Button prevButton) {
        boolean hasPrevSteps = currentFormStep > 0;

        if (hasPrevSteps) {
            currentFormStep--;
            replaceFormFragment(new FormCreatePin(currentFormStep), true, false);
        }

        nextButton.setText("Avançar");
        buttonManager(prevButton, currentFormStep > 0);
    }

    // Salva os dados do fragmento atual
    private void saveFormData(FormCreatePin fragment) {
        if (currentFormStep == 0) {
            imagemBytes = fragment.getImagemBytes();

        } else if (currentFormStep == 1) {
            address = fragment.getAddress();
            latitude = fragment.getLatitude();
            longitude = fragment.getLongitude();
            type = fragment.getType();
            dateTime = fragment.getDateTime();
        } else if (currentFormStep == 2) {
            title = fragment.getTitle();
            description = fragment.getDescription();
        }
    }

    // Insere os dados no banco de dados
    private void insertDataToDatabase(View view) {

        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imagem", imagemBytes);
        values.put("endereco", address);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("tipo", type);
        values.put("dataHora", dateTime);
        values.put("titulo", title);
        values.put("descricao", description);

        long newRowId = db.insert("Pins", null, values);
        if (newRowId != -1) {
            Snackbar snackbar = Snackbar.make(view, "Dados inseridos com sucesso!", Snackbar.LENGTH_LONG);
            snackbar.setAction("Fechar", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(view, "Erro ao inserir dados.", Snackbar.LENGTH_LONG);
            snackbar.setAction("Fechar", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }
}
