package com.elcazadordebaterias.coordinapp.utils;

import android.app.Dialog;
import android.content.Context;
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

/**
 * Class to create the pop-up dialog to request a new subject creation. The student
 * has to input the teacher fullname and the course number, and submit the request. This
 * request is opened from the home fragment of the student.
 *
 * @author Martín Mateos Sánchez
 */
public class RequestSubjectCreationDialog extends DialogFragment {

    private TextInputEditText teacherFullName, courseNumber;
    private RequestSubjectCreationDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (RequestSubjectCreationDialogListener) context;
        } catch (ClassCastException  e) {
            throw new ClassCastException(context.toString() + " must implement RequestSubjectCreationDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.requestsubjectcreation, null);

        builder.setView(view)
                .setTitle("Solicitud para crear una asignatura").setNegativeButton("Cancelar", (dialogInterface, i) -> {

                }).setPositiveButton("Solicitar", (dialogInterface, i) -> {
                    String teachername = teacherFullName.getText().toString();
                    String coursenumber = courseNumber.getText().toString();
                    listener.submitRequest(teachername, coursenumber);
                });

        teacherFullName = view.findViewById(R.id.creatsubject_teachername_text);
        courseNumber = view.findViewById(R.id.creatsubject_coursenumber_text);

        return builder.create();
    }

    public interface RequestSubjectCreationDialogListener {
        void submitRequest(String teachername, String coursenumber);
    }

}
