package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.teacher;


import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InputTextCardParent;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InteractivityCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.MultichoiceCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.MultichoiceCardDocument;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InteractivityCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<InteractivityCard> cardsList;
    private final Context context;

    public InteractivityCardsAdapter(ArrayList<InteractivityCard> cardsList, Context context) {
        this.cardsList = cardsList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case InteractivityCardType.TYPE_INPUTTEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_interactivity_teachercards_inputextcardparent, parent, false);
                return new InputTextCardParentViewHolder(view);
            case InteractivityCardType.TYPE_CHOICES:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_interactivity_teachercards_multichoicescard, parent, false);
                return new MultiChoiceCardViewHolder(view);
            default: // ReminderCard
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_interactivity_teachercards_remindercard, parent, false);
                return new ReminderCardViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        InteractivityCard card = cardsList.get(position);

        switch (holder.getItemViewType()) {

            case InteractivityCardType.TYPE_INPUTTEXT:
                InputTextCardParent inputTextCardParent = (InputTextCardParent) card;
                InputTextCardParentViewHolder holder1 = (InputTextCardParentViewHolder) holder;

                InputTextCardDocument inputTextCardDocument = inputTextCardParent.getInputTextCardDocument();

                holder1.cardTitle.setText(inputTextCardParent.getCardTitle());

                holder1.evaluableAndGroupal.setText(getEvaluableAndGroupalText(inputTextCardDocument.getHasToBeEvaluated(), inputTextCardDocument.getHasGroupalActivity()));

                // Set information of the card
                int totalNumberofStudents = inputTextCardDocument.getStudentsData().size();
                int nonAnsweredStudents = 0;
                int studentsWithMarkSet = 0;
                int studentsWithNoMarkSet = 0;
                float sumOfMarks = 0;

                for (InputTextCardDocument.InputTextCardStudentData studentData : inputTextCardDocument.getStudentsData()) {
                    if (studentData.getResponse() == null) {
                        nonAnsweredStudents++;
                    } else {
                        if (studentData.getHasMarkSet()) {
                            studentsWithMarkSet++;
                            sumOfMarks = sumOfMarks + studentData.getMark();
                        } else {
                            studentsWithNoMarkSet++;
                        }
                    }
                }

                holder1.informativeText.setText(getInformativeText(inputTextCardDocument.getHasGroupalActivity(), nonAnsweredStudents, totalNumberofStudents));

                float averageMark = 0;

                if (studentsWithMarkSet != 0) {
                    holder1.averageMark.setVisibility(View.VISIBLE);
                    averageMark = sumOfMarks / studentsWithMarkSet;
                } else {
                    holder1.averageMark.setVisibility(View.GONE);
                }

                if (!inputTextCardDocument.getHasToBeEvaluated()) {
                    holder1.nonEvaluated.setVisibility(View.GONE);
                } else {
                    if (studentsWithNoMarkSet != 0) {
                        holder1.nonEvaluated.setVisibility(View.VISIBLE);
                        String nonEvaluatedStudents;
                        if (studentsWithNoMarkSet == 1) {
                            if (inputTextCardDocument.getHasGroupalActivity()) {
                                nonEvaluatedStudents = "Evalúa la respuesta del grupo";
                            } else {
                                nonEvaluatedStudents = "Falta 1 respuesta por evaluar";
                            }
                        } else {
                            nonEvaluatedStudents = "Faltan " + studentsWithNoMarkSet + " respuestas por evaluar";
                        }
                        holder1.nonEvaluated.setText(nonEvaluatedStudents);
                    } else {
                        holder1.nonEvaluated.setVisibility(View.GONE);
                    }
                }

                String averageMarkString;
                String header;

                String averageMarkText = "" + averageMark;
                if (averageMarkText.endsWith(".0")) {
                    averageMarkText = averageMarkText.replace(".0", "");
                }

                if (inputTextCardDocument.getHasGroupalActivity()) {
                    header = "Nota de la respuesta: ";
                    averageMarkString = header + averageMarkText + "/10";
                } else {
                    header = "Nota media de las respuestas: ";
                    averageMarkString = "Nota media de las respuestas: " + averageMarkText + " / 10";
                }

                SpannableStringBuilder strb = new SpannableStringBuilder(averageMarkString);
                strb.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, header.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder1.averageMark.setText(strb);

                holder1.deleteActivity.setOnClickListener(view -> {
                    inputTextCardParent.getDocumentSnapshot().getReference().delete();
                });

                // Recyclerview con la lista de respuestas
                if (inputTextCardParent.getInputTextCardChildList().size() == 0) {
                    holder1.expandView.setVisibility(View.GONE);
                } else {
                    holder1.expandView.setVisibility(View.VISIBLE);
                    if (inputTextCardDocument.getHasOpenedResponses()) {
                        holder1.expandableView.setVisibility(View.VISIBLE);
                        if (inputTextCardDocument.getHasGroupalActivity()) {
                            holder1.expandView.setText(R.string.ocultarRespuesta);
                        } else {
                            holder1.expandView.setText(R.string.ocultarRespuestas);
                        }
                        holder1.expandView.setIconResource(R.drawable.ic_baseline_folder_24);
                    } else {
                        holder1.expandableView.setVisibility(View.GONE);
                        if (inputTextCardDocument.getHasGroupalActivity()) {
                            holder1.expandView.setText(R.string.verRespuesta);
                        } else {
                            holder1.expandView.setText(R.string.verRespuestas);
                        }
                        holder1.expandView.setIconResource(R.drawable.ic_baseline_folder_open_24);
                    }
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(holder1.inputTextCardChildRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

                layoutManager.setInitialPrefetchItemCount(inputTextCardParent.getInputTextCardChildList().size());
                InputTextCardsChildAdapter inputTextCardsChildAdapter = new InputTextCardsChildAdapter(inputTextCardParent.getInputTextCardChildList(), inputTextCardDocument.getHasToBeEvaluated(), context);

                RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

                holder1.inputTextCardChildRecyclerView.setLayoutManager(layoutManager);
                holder1.inputTextCardChildRecyclerView.setAdapter(inputTextCardsChildAdapter);
                holder1.inputTextCardChildRecyclerView.setRecycledViewPool(viewPool);

                // Button to expand responses
                holder1.expandView.setOnClickListener(view -> {
                    DocumentReference documentReference = inputTextCardParent.getDocumentSnapshot().getReference();
                    if (inputTextCardDocument.getHasOpenedResponses()) {
                        documentReference.update("hasOpenedResponses", false);
                    } else {
                        documentReference.update("hasOpenedResponses", true);
                    }
                });

                holder1.setVisibilityOff.setOnClickListener(view -> {
                    DocumentReference documentReference = inputTextCardParent.getDocumentSnapshot().getReference();
                    documentReference.update("hasTeacherVisibility", false);
                });

                break;

            case InteractivityCardType.TYPE_CHOICES:

                MultichoiceCard multichoiceCard = (MultichoiceCard) card;
                MultiChoiceCardViewHolder holder2 = (MultiChoiceCardViewHolder) holder;

                MultichoiceCardDocument multichoiceCardDocument = multichoiceCard.getMultichoiceCardDocument();

                holder2.cardTitle.setText(multichoiceCard.getCardTitle());

                holder2.listOfAnswers.setVisibility(View.GONE);

                holder2.evaluableAndGroupal.setText(getEvaluableAndGroupalText(multichoiceCardDocument.getHasToBeEvaluated(), multichoiceCardDocument.getHasGroupalActivity()));


                // Initialize hashMaps
                HashMap<Integer, String> identifierTitleMap = new HashMap<Integer, String>();
                HashMap<Integer, Integer> counterMap = new HashMap<Integer, Integer>();

                for (MultichoiceCardDocument.Question question : multichoiceCardDocument.getQuestionsList()) {
                    identifierTitleMap.put(question.getQuestionIdentifier(), question.getQuestionTitle());
                    counterMap.put(question.getQuestionIdentifier(), 0);
                }

                // Subtitle
                if (multichoiceCardDocument.getHasToBeEvaluated()) {

                    for (MultichoiceCardDocument.Question question : multichoiceCardDocument.getQuestionsList()) {
                        if (question.getHasCorrectAnswer()) {
                            String correctAnswer = "Respuesta correcta: " + question.getQuestionTitle();
                            SpannableStringBuilder str = new SpannableStringBuilder(correctAnswer);
                            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, "Respuesta correcta: ".length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            holder2.correctAnswer.setText(str);
                            break;
                        }
                    }

                } else {
                    holder2.correctAnswer.setVisibility(View.GONE);
                }

                int totalStudents = multichoiceCardDocument.getStudentsData().size();
                int nonAnswered = 0;

                for (MultichoiceCardDocument.MultichoiceCardStudentData studentData : multichoiceCardDocument.getStudentsData()) {
                    int questionIdentifier = studentData.getQuestionRespondedIdentifier();

                    if (questionIdentifier != -1) {
                        Integer counter = counterMap.get(questionIdentifier);
                        if (counter != null) {
                            counter++;
                            counterMap.put(questionIdentifier, counter);
                        }
                    } else {
                        nonAnswered++;
                    }
                }

                holder2.informativeText.setText(getInformativeText(multichoiceCardDocument.getHasGroupalActivity(), nonAnswered, totalStudents));

                for (Map.Entry<Integer, Integer> entry : counterMap.entrySet()) {
                    if (entry.getValue() != 0) {
                        holder2.listOfAnswers.setVisibility(View.VISIBLE);

                        if (!multichoiceCardDocument.getHasGroupalActivity()) {
                            String title = "Listado de respuestas";
                            SpannableStringBuilder str = new SpannableStringBuilder(title);
                            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, "Listado de respuestas".length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            holder2.listOfAnswers.setText(str);
                        }

                        int percentage = 0;

                        if (multichoiceCardDocument.getStudentsData().size() != 0) {
                            percentage = (entry.getValue() * 100) / multichoiceCardDocument.getStudentsData().size();
                        }

                        String questionTitle = identifierTitleMap.get(entry.getKey());
                        String studentOrStudents;

                        if (entry.getValue() > 1) {
                            studentOrStudents = "estudiantes";
                        } else {
                            studentOrStudents = "estudiante";
                        }

                        if (multichoiceCardDocument.getHasGroupalActivity()) {
                            String response = "Respuesta del grupo: " + questionTitle;
                            SpannableStringBuilder str = new SpannableStringBuilder(response);
                            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, "Respuesta del grupo: ".length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            holder2.listOfAnswers.setText(str);
                        } else {
                            String text = questionTitle + ":  Contestada por " + entry.getValue() + " " + studentOrStudents + " (" + percentage + "%)";
                            TextView textView = new TextView(holder2.questionsContainer.getContext());
                            textView.setText(text);
                            textView.setTextColor(Color.rgb(0, 0, 0));
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0, 8, 8, 8);
                            textView.setLayoutParams(params);

                            holder2.questionsContainer.addView(textView);
                        }
                    }
                }

                holder2.deleteActivity.setOnClickListener(view -> {
                    multichoiceCard.getMultichoiceCardDocumentSnapshot().getReference().delete();
                });

                holder2.setVisibilityOff.setOnClickListener(view -> {
                    multichoiceCard.getMultichoiceCardDocumentSnapshot().getReference().update("hasTeacherVisibility", false);
                });


                break;

            default: // ReminderCard
                /*
                ReminderCard reminderCard = (ReminderCard) card;
                ReminderCardViewHolder holder3 = (ReminderCardViewHolder) holder;

                holder3.cardTitle.setText(reminderCard.getCardTitle());
                holder3.reminderContainer.setText(reminderCard.getReminderText());
                 */
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        InteractivityCard card = cardsList.get(position);

        int viewType;

        if (card instanceof InputTextCardParent) {
            viewType = InteractivityCardType.TYPE_INPUTTEXT;
        } else if (card instanceof MultichoiceCard) {
            viewType = InteractivityCardType.TYPE_CHOICES;
        } else { // ReminderCard
            viewType = InteractivityCardType.TYPE_REMINDER;
        }

        return viewType;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView cardTitle;

        public CardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
        }

    }

    public static class InputTextCardParentViewHolder extends CardViewHolder {

        TextView evaluableAndGroupal;
        TextView informativeText;
        TextView nonEvaluated;
        TextView averageMark;
        MaterialButton expandView;
        MaterialButton setVisibilityOff;
        MaterialButton deleteActivity;
        ConstraintLayout expandableView;
        RecyclerView inputTextCardChildRecyclerView;

        public InputTextCardParentViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            evaluableAndGroupal = itemView.findViewById(R.id.evaluableAndGroupal);
            informativeText = itemView.findViewById(R.id.informativeText);
            nonEvaluated = itemView.findViewById(R.id.nonEvaluated);
            averageMark = itemView.findViewById(R.id.averageMark);
            expandView = itemView.findViewById(R.id.expandView);
            setVisibilityOff = itemView.findViewById(R.id.setVisibilityOff);
            deleteActivity = itemView.findViewById(R.id.deleteActivity);
            expandableView = itemView.findViewById(R.id.expandableView);
            inputTextCardChildRecyclerView = itemView.findViewById(R.id.inputTextCardChildRecyclerView);
        }

    }

    public static class MultiChoiceCardViewHolder extends CardViewHolder {

        TextView evaluableAndGroupal;
        TextView correctAnswer;
        TextView informativeText;
        TextView listOfAnswers;
        LinearLayout questionsContainer;
        MaterialButton setVisibilityOff;
        MaterialButton deleteActivity;

        public MultiChoiceCardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            evaluableAndGroupal = itemView.findViewById(R.id.evaluableAndGroupal);
            correctAnswer = itemView.findViewById(R.id.correctAnswer);
            informativeText = itemView.findViewById(R.id.informativeText);
            listOfAnswers = itemView.findViewById(R.id.listOfAnswers);
            questionsContainer = itemView.findViewById(R.id.questionsContainer);
            deleteActivity = itemView.findViewById(R.id.deleteActivity);
            setVisibilityOff = itemView.findViewById(R.id.setVisibilityOff);
        }

    }

    public static class ReminderCardViewHolder extends CardViewHolder {

        TextView reminderContainer;

        public ReminderCardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            reminderContainer = itemView.findViewById(R.id.reminderContainer);
        }
    }

    private String getEvaluableAndGroupalText(boolean hasToBeEvaluated, boolean hasGroupalActivity) {
        String evaluableText;
        String groupalText;

        if (hasToBeEvaluated) {
            evaluableText = "Actividad evaluable";
        } else {
            evaluableText = "Actividad no evaluable";
        }

        if (hasGroupalActivity) {
            groupalText = " grupal";
        } else {
            groupalText = " no grupal";
        }

        return evaluableText + groupalText;
    }

    private String getInformativeText(boolean isGroupal, int nonAnsweredStudents, int totalNumberofStudents) {
        String informativeText;
        if (nonAnsweredStudents == totalNumberofStudents) {
            if (isGroupal) {
                informativeText = "Esperando a que el portavoz conteste";
            } else {
                informativeText = "Ningún estudiante ha contestado todavía";
            }
        } else if (nonAnsweredStudents == 0) {
            if (isGroupal) {
                informativeText = "El grupo ha contestado";
            } else {
                informativeText = "Todos los estudiantes han contestado";
            }
        } else {
            if (nonAnsweredStudents == 1) {
                informativeText = "Falta 1 estudiante por contestar ";
            } else {
                informativeText = "Faltan " + nonAnsweredStudents + " estudiantes por contestar ";
            }
        }
        return informativeText;
    }

}
