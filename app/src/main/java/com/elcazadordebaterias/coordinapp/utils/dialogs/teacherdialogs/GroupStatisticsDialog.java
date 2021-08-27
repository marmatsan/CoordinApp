package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupStatisticsDialog extends DialogFragment {

    TextView averageGroupMarkInputText;
    TextView averageIndividualMarkInputText;

    TextView groupalPerc;
    TextView individualPerc;

    HashMap<String, Double> statistics;

    public GroupStatisticsDialog(HashMap<String, Double> statistics) {
        this.statistics = statistics;
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

        View view = getActivity().getLayoutInflater().inflate(R.layout.utils_dialogs_groupstatisticsdialog, null);

        averageGroupMarkInputText = view.findViewById(R.id.averageGroupMarkInputText);
        averageIndividualMarkInputText = view.findViewById(R.id.averageIndividualMarkInputText);
        groupalPerc = view.findViewById(R.id.groupalPerc);
        individualPerc = view.findViewById(R.id.individualPerc);

        for (String key : statistics.keySet()) {
            Log.d("DEBUGGING", key + ": " + statistics.get(key));
        }

        // InputText Groupal
        Double groupalMarkInputText = statistics.get("Groupal InputText Mark");
        Double evaluableGroupalInputCards = statistics.get("Groupal InputText Cards");

        if (groupalMarkInputText != null && evaluableGroupalInputCards != null) {
            if (evaluableGroupalInputCards != 0) {

                double average = groupalMarkInputText / evaluableGroupalInputCards;

                if (average < 5) {
                    averageGroupMarkInputText.setTextColor(Color.parseColor("#B00020")); // Red
                } else if (average >= 5 && average < 7) {
                    averageGroupMarkInputText.setTextColor(Color.parseColor("#C7CB85")); // Yellow
                } else if (average >= 7 && average < 9) {
                    averageGroupMarkInputText.setTextColor(Color.parseColor("#7FB800")); // Green 1
                } else {
                    averageGroupMarkInputText.setTextColor(Color.parseColor("#5CAB7D")); // Green 2
                }

                String averageMarkText = "" + average;

                if (averageMarkText.endsWith(".0")) {
                    averageMarkText = averageMarkText.replace(".0", "");
                }

                String avg = averageMarkText + "/10";

                averageGroupMarkInputText.setText(avg);
            } else {
                String text = "No se ha evaluado ninguna actividad grupal";
                averageGroupMarkInputText.setText(text);
            }
        }

        // InputText Individual

        Double individualMarkInputText = statistics.get("Individual InputText Mark");
        Double evaluableIndividualStudents = statistics.get("Individual Evaluable Students");

        if (individualMarkInputText != null && evaluableIndividualStudents != null) {
            if (evaluableIndividualStudents != 0) {

                double average = individualMarkInputText / evaluableIndividualStudents;

                if (average < 5) {
                    averageIndividualMarkInputText.setTextColor(Color.parseColor("#B00020")); // Red
                } else if (average >= 5 && average < 7) {
                    averageIndividualMarkInputText.setTextColor(Color.parseColor("#C7CB85")); // Yellow
                } else if (average >= 7 && average < 9) {
                    averageIndividualMarkInputText.setTextColor(Color.parseColor("#7FB800")); // Green 1
                } else {
                    averageIndividualMarkInputText.setTextColor(Color.parseColor("#5CAB7D")); // Green 2
                }

                String averageMarkText = "" + average;

                if (averageMarkText.endsWith(".0")) {
                    averageMarkText = averageMarkText.replace(".0", "");
                }

                String avg = averageMarkText + "/10";

                averageIndividualMarkInputText.setText(avg);
            } else {
                String text = "No se ha evaluado ninguna actividad individual";
                averageIndividualMarkInputText.setText(text);
            }
        }


        builder.setTitle("Estadísticas del actividades evaluadas")
                .setView(view)
                .setNegativeButton("Ocultar", (dialog, i) -> {
                    // Just closes the dialog
                });

        return builder.create();
    }

}
