package com.elcazadordebaterias.coordinapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * Class to create the pop-up dialog to request a new subject creation. The student
 * has to input the teacher fullname and the course number, and submit the request. This
 * request is opened from the home fragment of the student.
 *
 * @author Martín Mateos Sánchez
 */
public class RequestSubjectCreationDialog extends DialogFragment {

    private Spinner teachersNamesList, coursesNamesList;
    private RequestSubjectCreationDialogListener listener;
    private TextInputEditText coursenumberinput;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        try {
            listener = (RequestSubjectCreationDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RequestSubjectCreationDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.requestsubjectcreation, null);

        // Teacher names spinner
        teachersNamesList = view.findViewById(R.id.teacherfullname_spinner);

        ArrayList<String> teachersNames = new ArrayList<String>();
        teachersNames.add("Nombre del/la profesor/a");

        ArrayAdapter<String> teachersNameAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, teachersNames) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        teachersNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teachersNamesList.setAdapter(teachersNameAdapter);
        teachersNamesList.setSelection(0);

        // Courses names spinner
        coursesNamesList = view.findViewById(R.id.coursenumber_spinner);

        ArrayList<String> coursesNames = new ArrayList<String>();
        coursesNames.add("Selecciona un curso");
        coursesNames.add("1º ESO");
        coursesNames.add("2º ESO");
        coursesNames.add("3º ESO");
        coursesNames.add("4º ESO");
        coursesNames.add("1º Bachillerato");
        coursesNames.add("2º Bachillerato");

        ArrayAdapter<String> coursesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, coursesNames) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        coursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coursesNamesList.setAdapter(coursesAdapter);
        coursesNamesList.setSelection(0);

        CollectionReference teachersNamesCollection = fStore.collection("Teachers");

        teachersNamesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    teachersNames.add(document.getData().get("FullName").toString());
                }
            } // TODO: 23-01-2021 Not checking if the task fails
            teachersNameAdapter.notifyDataSetChanged(); // TODO: 23-01-2021 Since the get() method is asynchronous, first get the data, then build the view, then show it
        });

        // Course number input
        coursenumberinput = view.findViewById(R.id.coursenumber); // TODO: 26-01-2021 Improve this (check that the uses enters only one letter)

        builder.setView(view)
                .setTitle("Solicitud para crear una asignatura").setNegativeButton("Cancelar", (dialogInterface, i) -> {
            // Just closes the dialog
        }).setPositiveButton("Solicitar", (dialogInterface, i) -> {

            String teachername = teachersNamesList.getSelectedItem().toString();
            String coursenumber = coursesNamesList.getSelectedItem().toString();
            String coursenumberletter = coursenumberinput.getText().toString();

            listener.submitRequest(teachername, coursenumber, coursenumberletter);

        });

        return builder.create();
    }

    public interface RequestSubjectCreationDialogListener {
        void submitRequest(String teachername, String coursenumber, String coursenumberletter);
    }

}
