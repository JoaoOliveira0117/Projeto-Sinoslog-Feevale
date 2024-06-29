package com.example.projetofeevale.ui.createOccurrence;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetofeevale.R;
import com.example.projetofeevale.data.model.request.OccurrenceRequest;
import com.example.projetofeevale.data.model.response.OccurrenceResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.OccurrenceRepository;
import com.example.projetofeevale.ui.createOccurrence.fragment.BaseOccurrence;
import com.example.projetofeevale.ui.createOccurrence.fragment.OccurrenceIdentity;
import com.example.projetofeevale.ui.createOccurrence.fragment.OccurrenceImageUpload;
import com.example.projetofeevale.ui.createOccurrence.fragment.OccurrenceInfo;

import java.util.ArrayList;
import java.util.List;

public class CreateOccurrence extends Fragment {
    private OccurrenceRequest occurrenceRequest;
    private int currentIndex = 0;
    private BaseOccurrence currentFragment;
    private final List<BaseOccurrence> fragmentList = new ArrayList<>();
    private Button prevButton;
    private Button nextButton;

    private DialogInterface.OnClickListener onSuccess;

    public CreateOccurrence() {
    }

    public CreateOccurrence(DialogInterface.OnClickListener onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_pin, container, false);

        prevButton = view.findViewById(R.id.form_btn_prev);
        nextButton = view.findViewById(R.id.form_btn_next);

        setupPrevButton();
        setupNextButton();

        occurrenceRequest = new OccurrenceRequest();

        fragmentList.add(new OccurrenceImageUpload(occurrenceRequest));
        fragmentList.add(new OccurrenceInfo(occurrenceRequest));
        fragmentList.add(new OccurrenceIdentity(occurrenceRequest));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        replaceFormFragment(fragmentList.get(0), false, false);
    }

    private int getPreviousFragmentIndex() {
        currentIndex = currentIndex - 1;
        currentIndex = Math.max(currentIndex, 0);
        return currentIndex;
    }

    private int getNextFragmentIndex() {
        currentIndex = currentIndex + 1;
        currentIndex = Math.min(currentIndex, fragmentList.toArray().length - 1);
        return currentIndex;
    }

    private void setupPrevButton() {
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int targetIndex = getPreviousFragmentIndex();
                replaceFormFragment(fragmentList.get(targetIndex), true, false);
            }
        });
    }

    private void setupNextButton() {
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!currentFragment.hasEmptyFields()) {
                    int targetIndex = getNextFragmentIndex();
                    replaceFormFragment(fragmentList.get(targetIndex), true, true);
                };
            }
        });
    }

    private void handleButtonVisibility() {
        if(currentIndex == 0) {
            prevButton.setVisibility(View.INVISIBLE);
        } else if (currentIndex == fragmentList.toArray().length - 1) {
            nextButton.setText("Finalizar");
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postOccurrence();
                }
            });
        } else {
            setupNextButton();
            prevButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setText("Avançar");
        }
    }

    private void postOccurrence() {
        if(currentFragment.hasEmptyFields()) {
            return;
        }

        for (BaseOccurrence fragment : fragmentList) {
            fragment.fillForm();
        }

        new OccurrenceRepository().createOccurrence(occurrenceRequest, new ApiCallback<OccurrenceResponse>() {
            @Override
            public void onSuccess(OccurrenceResponse data) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Ocorrência criada com sucesso!")
                        .setPositiveButton("OK", onSuccess)
                        .show();
            }

            @Override
            public void onFailure(String message, String cause, Throwable t) {
                Toast.makeText(requireActivity().getBaseContext(), message + cause, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void replaceFormFragment(BaseOccurrence fragment, boolean animated, boolean toRight) {
        FragmentManager fragmentManager = getChildFragmentManager();
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
        handleButtonVisibility();
        fragmentTransaction.commit();
    }
}
