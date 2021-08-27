package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InputTextCardParent;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.MultichoiceCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.MultichoiceCardDocument;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.HashMap;

public class IndividualStatisticsDialog extends DialogFragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private String selectedCourse;
    private String selectedSubject;
    private String studentID;
    private String studentName;

    private HashMap<String, HashMap<String, Double>> statisticsMap;
    
    public IndividualStatisticsDialog(String selectedCourse, String selectedSubject, String studentName, String studentID, HashMap<String, HashMap<String, Double>> statisticsMap) {
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
        this.studentID = studentID;
        this.studentName = studentName;
        this.statisticsMap = statisticsMap;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.utils_dialogs_individualuserstatistics, null);

        LinearLayout container = view.findViewById(R.id.container);

        for (int i = 0; i < 10 ; i++) {
            TextView valueTV = new TextView(getContext());
            String t = "Actividades de tipo entrada de texto";
            valueTV.setText(t);
            valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            container.addView(valueTV);
        }

        /*
        for (String key : statisticsMap.keySet()) {
            Log.d("DEBUGGING", key);

            HashMap<String, Double> groupStatistics = statisticsMap.get(key);

            Double evaluableInputTextDocuments = groupStatistics.get("Evaluable InputTextDocuments");
            Double cumulativeInputTextMark = groupStatistics.get("Cumulative InputTextMark");

            Double totalPoints =  groupStatistics.get("Evaluable MultichoiceDocuments");
            Double evaluableMultichoiceDocuments = groupStatistics.get("Total points");

            TextView valueTV = new TextView(getContext());
            String t = "Actividades de tipo entrada de texto";
            valueTV.setText(t);
            valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            container.addView(valueTV);

            if (evaluableInputTextDocuments != null &&  cumulativeInputTextMark != null) {
                if (evaluableInputTextDocuments != 0){
                    double averageMark = cumulativeInputTextMark / evaluableInputTextDocuments;
                    String av = "" +averageMark;
                    valueTV.setText(av);
                } else {
                    String te = "No hay actividades tipo entrada de texto evaluables";
                    valueTV.setText(te);
                }

                valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                if(valueTV.getParent() != null) {
                    ((ViewGroup)valueTV.getParent()).removeView(valueTV); // <- fix
                }

                container.addView(valueTV);
            }

            if (evaluableMultichoiceDocuments != null &&  totalPoints != null) {
                if (evaluableMultichoiceDocuments != 0){
                    double rate = totalPoints /evaluableMultichoiceDocuments;
                    double ratePerc = rate * 100;
                    String av = "" +ratePerc + "%";
                    valueTV.setText(av);
                } else {
                    String te = "No hay actividades tipo multirespuesta evaluables";
                    valueTV.setText(te);
                }

                valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                if(valueTV.getParent() != null) {
                    ((ViewGroup)valueTV.getParent()).removeView(valueTV); // <- fix
                }
                container.addView(valueTV);
            }


        }

        */

        builder.setTitle("Estadísticas de " + studentName)
                .setView(view)
                .setNegativeButton("Ocultar", (dialog, i) -> {
                    // Just closes the dialog
                });

        return builder.create();
    }

}
