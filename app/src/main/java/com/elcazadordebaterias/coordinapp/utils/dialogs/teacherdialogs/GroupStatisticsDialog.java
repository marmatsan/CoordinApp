package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
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

public class GroupStatisticsDialog extends DialogFragment {

    TextView averageGroupMarkInputText;
    TextView averageIndividualMarkInputText;

    TextView groupalPerc;
    TextView individualPerc;

    ArrayList<Double> statistics;

    public GroupStatisticsDialog(ArrayList<Double>statistics){
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

        for (Double doub : statistics) {
            Log.d("DEBUGGING", ""+doub);
        }

        BigDecimal groupalMarkInputText = BigDecimal.valueOf(statistics.get(0));
        groupalMarkInputText = groupalMarkInputText.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        BigDecimal evaluableGroupalInputCards = BigDecimal.valueOf(statistics.get(1));
        evaluableGroupalInputCards = evaluableGroupalInputCards.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        if (statistics.get(1) != 0) {

            double average = statistics.get(0)/statistics.get(1);

            String avText = ""+ average;

            averageGroupMarkInputText.setText(avText);
        } else {
            String text = "No se ha evaluado ninguna actividad";
            averageGroupMarkInputText.setText(text);
        }


        builder.setTitle("Crear una nueva tarjeta")
                .setView(view)
                .setNegativeButton("Cancelar", (dialog, i) -> {
                    // Just closes the dialog
                });

        return builder.create();
    }

}
