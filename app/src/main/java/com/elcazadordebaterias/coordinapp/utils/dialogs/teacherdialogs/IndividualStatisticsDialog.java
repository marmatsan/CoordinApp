package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
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

    Context context;

    LinearLayout container;

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
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.utils_dialogs_individualuserstatistics, null);
        container = view.findViewById(R.id.container);

        for (String key : statisticsMap.keySet()) {
            addTextView(key, 24, 24, 8, 2, Typeface.BOLD, 16, R.color.black);

            View v = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
            setMarginsDp(params, 24, 0, 24, 8);
            v.setLayoutParams(params);
            v.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.black));
            container.addView(v);

            HashMap<String, Double> groupStatistics = statisticsMap.get(key);

            Double evaluableInputTextDocuments = groupStatistics.get("Evaluable InputTextDocuments");
            Double cumulativeInputTextMark = groupStatistics.get("Cumulative InputTextMark");

            addTextView("De tipo entrada de texto", 24, 24, 8, 0, Typeface.BOLD, 16, R.color.black);
            addTextView("Media de las actividades", 24, 24, 8, 0, Typeface.NORMAL, 14, R.color.defaultColor);

            String inputActivitiesText;
            int color;

            if (evaluableInputTextDocuments != null && cumulativeInputTextMark != null) {
                if (evaluableInputTextDocuments != 0) {
                    double average = cumulativeInputTextMark / evaluableInputTextDocuments;

                    if (average < 5) {
                        color = R.color.red;
                    } else if (average >= 5 && average < 7) {
                        color = R.color.yellow;
                    } else if (average >= 7 && average < 9) {
                        color = R.color.green1;
                    } else {
                        color = R.color.green2;
                    }

                    String averageMarkText = "" + average;

                    if (averageMarkText.endsWith(".0")) {
                        averageMarkText = averageMarkText.replace(".0", "");
                    } else if (averageMarkText.length() > 4) {
                        averageMarkText = averageMarkText.substring(0, 4);
                    }

                    String text = averageMarkText + "/10";
                    addTextView(text, 24, 24, 8, 8, Typeface.NORMAL, 14, color);

                } else {
                    inputActivitiesText = "No se ha evaluado ninguna actividad individual";
                    color = R.color.orange;
                    addTextView(inputActivitiesText, 24, 24, 8, 8, Typeface.NORMAL, 14, color);
                }
            }

            Double evaluableMultichoiceDocuments = groupStatistics.get("Total points");
            Double totalPoints = groupStatistics.get("Evaluable MultichoiceDocuments");

            addTextView("De tipo multirespuesta", 24, 24, 8, 0, Typeface.BOLD, 16, R.color.black);
            addTextView("Tasa de acierto de las actividades", 24, 24, 8, 0, Typeface.NORMAL, 14, R.color.defaultColor);

            if (evaluableMultichoiceDocuments != null && totalPoints != null) {
                if (evaluableMultichoiceDocuments != 0) {
                    double rate = totalPoints / evaluableMultichoiceDocuments;

                    if (rate < 0.5) {
                        color = R.color.red;
                    } else if (rate >= 0.5 && rate < 0.7) {
                        color = R.color.yellow;
                    } else if (rate >= 0.7 && rate < 0.9) {
                        color = R.color.green1;
                    } else {
                        color = R.color.green2;
                    }

                    double ratePerc = rate * 100;
                    String ratePercText = "" + ratePerc;

                    if (ratePercText.endsWith(".0")) {
                        ratePercText = ratePercText.replace(".0", "");
                    } else if (ratePercText.length() > 4) {
                        ratePercText = ratePercText.substring(0, 4);
                    }

                    String text = ratePercText + "%";
                    addTextView(text, 24, 24, 8, 8, Typeface.NORMAL, 14, color);
                } else {
                    inputActivitiesText = "No se ha evaluado ninguna actividad individual";
                    color = R.color.orange;
                    addTextView(inputActivitiesText, 24, 24, 8, 8, Typeface.NORMAL, 14, color);
                }
            }

            // Sent messages
            addTextView("Contribución al grupo", 24, 24, 8, 0, Typeface.BOLD, 16, R.color.black);
            addTextView("Mensajes enviados por el chat entre alumnos", 24, 24, 8, 0, Typeface.NORMAL, 14, R.color.defaultColor);


            Double totalChatMessages = groupStatistics.get("Total Chat Messages");
            Double sentByUser = groupStatistics.get("Messages By User");

            if (totalChatMessages != null && sentByUser != null) {
                if (totalChatMessages != 0) {

                    double rate = sentByUser / totalChatMessages;

                    if (rate < 0.5) {
                        color = R.color.red;
                    } else if (rate >= 0.5 && rate < 0.7) {
                        color = R.color.yellow;
                    } else if (rate >= 0.7 && rate < 0.9) {
                        color = R.color.green1;
                    } else {
                        color = R.color.green2;
                    }

                    int sentByUserInt = (int) Math.round(sentByUser);
                    int totalChatMessagesInt = (int) Math.round(totalChatMessages);

                    double percentage = (sentByUser / totalChatMessages) * 100;
                    String ratePercText = "" + percentage;

                    if (ratePercText.endsWith(".0")) {
                        ratePercText = ratePercText.replace(".0", "");
                    } else if (ratePercText.length() > 4) {
                        ratePercText = ratePercText.substring(0, 4);
                    }

                    String text = sentByUserInt + "/" + totalChatMessagesInt + " (" +(ratePercText) + "%)";

                    addTextView(text, 24, 24, 8, 8, Typeface.NORMAL, 14, color);
                } else {
                    inputActivitiesText = "Ningún alumno del grupo ha enviado mensajes";
                    color = R.color.orange;
                    addTextView(inputActivitiesText, 24, 24, 8, 8, Typeface.NORMAL, 14, color);
                }
            }

        }

        builder.setTitle("Estadísticas individuales de " + studentName)
                .setView(view)
                .setNegativeButton("Ocultar", (dialog, i) -> {
                    // Just closes the dialog
                });

        return builder.create();
    }

    private int dpToPx(int dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    private void setMarginsDp(LinearLayout.LayoutParams params, int leftDp, int topDp, int rightDp, int bottomDp) {
        int leftPx = dpToPx(leftDp);
        int topPx = dpToPx(topDp);
        int rightPx = dpToPx(rightDp);
        int bottomPx = dpToPx(bottomDp);

        params.setMargins(leftPx, topPx, rightPx, bottomPx);
    }

    private void addTextView(String text, int leftDp, int rightDp, int topDp, int bottomDp, int typeFace, int textSizeSp, int color) {
        TextView textView = new TextView(container.getContext());
        textView.setText(text);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setMarginsDp(params, leftDp, topDp, rightDp, bottomDp);
        textView.setLayoutParams(params);
        textView.setTypeface(textView.getTypeface(), typeFace);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSp);
        textView.setTextColor(ContextCompat.getColor(container.getContext(), color));

        container.addView(textView);
    }

}
