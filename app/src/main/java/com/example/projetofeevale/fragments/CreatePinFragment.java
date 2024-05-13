package com.example.projetofeevale.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetofeevale.MainActivity;
import com.example.projetofeevale.R;
import com.example.projetofeevale.fragments.FormCreatePin.FormCreatePin;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePinFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View.OnClickListener onBack;
    private Fragment currentFragment;
    private int currentFormStep = 0;

    public CreatePinFragment() {
        // Required empty public constructor
    }

    public CreatePinFragment(View.OnClickListener onBack) {
        this.onBack = onBack;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePinFragment newInstance(String param1, String param2) {
        CreatePinFragment fragment = new CreatePinFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_pin, container, false);

        FormCreatePin formCreatePin = new FormCreatePin(currentFormStep);
        replaceFormFragment(formCreatePin, false, false);

        ImageButton backButton = (ImageButton) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(onBack);

        Button formNextButton = (Button) view.findViewById(R.id.form_btn_next);
        Button formPrevButton = (Button) view.findViewById(R.id.form_btn_prev);

        formNextButton.setOnClickListener(onFormNavigate(formNextButton, formPrevButton, true));
        formPrevButton.setOnClickListener(onFormNavigate(formNextButton, formPrevButton, false));

        buttonManager(formNextButton, currentFormStep < 2);
        buttonManager(formPrevButton, currentFormStep > 0);

        return view;
    }

    private void buttonManager(Button button, boolean enabled) {
        System.out.println(enabled);
        button.setEnabled(enabled);
        button.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    private void replaceFormFragment(Fragment fragment, boolean animated, boolean to_right) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (animated && to_right) {
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left,
                    R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right
            );
        }

        if (animated && !to_right) {
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

    private View.OnClickListener onFormNavigate(Button nextButton, Button prevButton, boolean next) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasPrevSteps = currentFormStep > 0;
                boolean hasNextSteps = currentFormStep < FormCreatePin.getFormStepsAmount() - 1;
                boolean canNavigate = next ? hasNextSteps : hasPrevSteps;
                int multiplier = next ? 1 : -1;

                if (canNavigate) {
                    currentFormStep = currentFormStep + multiplier;
                    replaceFormFragment(new FormCreatePin(currentFormStep), true, next);
                    hasPrevSteps = currentFormStep > 0;
                    hasNextSteps = currentFormStep < FormCreatePin.getFormStepsAmount() - 1;
                }

                nextButton.setText(hasNextSteps ? "Avançar" : "Finalizar");
                buttonManager(prevButton, hasPrevSteps);
            }
        };
    }
}