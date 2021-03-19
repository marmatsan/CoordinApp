package com.elcazadordebaterias.coordinapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;

import com.elcazadordebaterias.coordinapp.adapters.MyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * Class to create the pop-up dialog to create a new chat group
 *
 * @author Martín Mateos Sánchez
 */

public class CreateGroupDialog extends DialogFragment {
    private Spinner courseList, subjectList, participantsList;

    private CreateGroupDialogListener listener;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        try {
            listener = (CreateGroupDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CreateGroupDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_creategroupdialog, null);

        // Group list spinner
        courseList = view.findViewById(R.id.courseNameSpinner);

        ArrayList<String> coursesNames = new ArrayList<String>();
        coursesNames.add("Selecciona el curso");

        ArrayAdapter<String> courseListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, coursesNames) {
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
        courseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseList.setAdapter(courseListAdapter);
        courseList.setSelection(0);

        // Subject list spinner
        subjectList = view.findViewById(R.id.subjectNameSpinner);

        ArrayList<String> subjectNames = new ArrayList<String>();
        subjectNames.add("Selecciona una asignatura");

        ArrayAdapter<String> subjectListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subjectNames) {
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
        subjectListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectList.setAdapter(subjectListAdapter);
        subjectList.setSelection(0);

        CollectionReference coursesCollection = fStore.collection("CoursesOrganization");

        coursesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    coursesNames.add(document.getId());
                }
            }
            courseListAdapter.notifyDataSetChanged();
            /*
            if(coursesNames.size() > 2){

            }
             */
        });

        // Participants List Spinner

        participantsList = view.findViewById(R.id.participantsSpinner);

        final String[] select_planets = {"AAAAAAA", "BBBBBBBB", "CCCCCCCCCC","DDDDDDDD", "EEEEEEEEE", "FFFFFFFFFF","GGGGGGGGG", "HHHHHHHH", "IIIIIIIII","JJJJJJJJJ", "KKKKKKKKK", "LLLLLLLLL","MMMMMMMM", "NNNNNNNNN", "OOOOOOOOO","PPPPPPPPPPP", "QQQQQQQQQQQQQQQ", "RRRRRRRRRR","SSSSSSSSSS", "TTTTTTTTTTT", "UUUUUUUUUU","VVVVVVVVVV", "WWWWWWWWWWW", "XXXXXXXXXXXX"};
        ArrayList<CreateGroupDialogSpinnerItem> categoryModelArrayList = new ArrayList<>();

        for (String s : select_planets) {
            CreateGroupDialogSpinnerItem categoryModel = new CreateGroupDialogSpinnerItem(s, false);
            categoryModelArrayList.add(categoryModel);
        }

        MyAdapter myAdapter = new MyAdapter(getContext(), categoryModelArrayList);
        participantsList.setAdapter(myAdapter);



        builder.setView(view).setTitle("Solicitud para crear un grupo")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
        })
                .setPositiveButton("Solicitar", (dialogInterface, i) -> {

                    String course = null;
                    String subject = null;
                    String[] participants = null;

                    listener.submitRequest(course, subject, participants);

        });

        return builder.create();
    }

    public interface CreateGroupDialogListener {
        void submitRequest(String course, String subject, String[] participants);
    }

}
