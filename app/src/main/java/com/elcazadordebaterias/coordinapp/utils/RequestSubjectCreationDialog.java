package com.elcazadordebaterias.coordinapp.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class RequestSubjectCreationDialog extends DialogFragment {

    TextInputEditText teacherFullName, courseNumber;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.requestsubjectcreation, null);

        builder.setView(view)
                .setTitle("Solicitud para crear una asignatura").setNegativeButton("Cancelar", (dialogInterface, i) -> {

                }).setPositiveButton("Solicitar", (dialogInterface, i) -> {

                });

        teacherFullName = view.findViewById(R.id.creatsubject_teachername_text);
        courseNumber = view.findViewById(R.id.creatsubject_coursenumber_text);

        return builder.create();
    }
}
