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

                holder1.cardTitle.setText(inputTextCardParent.getCardTitle());

                // Set information of the card
                int totalNumberofStudents = inputTextCardParent.getInputTextCardDocument().getStudentsData().size();
                int nonAnsweredStudents = 0;

                for (InputTextCardDocument.InputTextCardStudentData studentData : inputTextCardParent.getInputTextCardDocument().getStudentsData()) {
                    if (studentData.getResponse() == null) {
                        nonAnsweredStudents++;
                    }
                }

                String inputCardinformativeText;

                if (nonAnsweredStudents == totalNumberofStudents) {
                    inputCardinformativeText = "A la espera de respuestas";
                } else if () {

                }

                holder1.informativeText.setText(inputCardinformativeText);
                holder1.notAnsweredStudents.setText(inputTextCardParent.getStudentsThatHaveNotAnswered());

                LinearLayoutManager layoutManager = new LinearLayoutManager(holder1.inputTextCardChildRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

                layoutManager.setInitialPrefetchItemCount(inputTextCardParent.getInputTextCardChildList().size());
                InputTextCardsChildAdapter inputTextCardsChildAdapter = new InputTextCardsChildAdapter(inputTextCardParent.getInputTextCardChildList(), context);

                RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
                SparseBooleanArray expandState = new SparseBooleanArray();

                for (int i = 0; i < cardsList.size(); i++) {
                    expandState.append(i, false);
                }

                holder1.inputTextCardChildRecyclerView.setLayoutManager(layoutManager);
                holder1.inputTextCardChildRecyclerView.setAdapter(inputTextCardsChildAdapter);
                holder1.inputTextCardChildRecyclerView.setRecycledViewPool(viewPool);

                // Button to expand responses
                holder1.expandView.setText(R.string.verRespuestas);
                holder1.expandView.setIconResource(R.drawable.ic_baseline_folder_open_24);

                final boolean isExpanded = expandState.get(position); //Check if the view is expanded
                holder1.expandableView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

                holder1.expandView.setOnClickListener(view -> {
                    if (holder1.expandableView.getVisibility() == View.VISIBLE) {
                        holder1.expandableView.setVisibility(View.GONE);
                        holder1.expandView.setText(R.string.verRespuestas);
                        holder1.expandView.setIconResource(R.drawable.ic_baseline_folder_open_24);
                        expandState.put(position, false);
                    } else {
                        holder1.expandableView.setVisibility(View.VISIBLE);
                        holder1.expandView.setText(R.string.ocultarRespuestas);
                        holder1.expandView.setIconResource(R.drawable.ic_baseline_folder_24);
                        expandState.put(position, true);
                    }
                });

                break;

            case InteractivityCardType.TYPE_CHOICES:

                MultichoiceCard multichoiceCard = (MultichoiceCard) card;
                MultiChoiceCardViewHolder holder2 = (MultiChoiceCardViewHolder) holder;

                MultichoiceCardDocument multichoiceCardDocument = multichoiceCard.getMultichoiceCardDocument();

                holder2.cardTitle.setText(multichoiceCard.getCardTitle());

                holder2.listOfAnswers.setVisibility(View.GONE);

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
                    holder2.correctAnswer.setText(R.string.noEvaluable);
                }

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

                String multiChoiceCardInformativeText;

                if (nonAnswered == multichoiceCardDocument.getStudentsData().size()) {
                    multiChoiceCardInformativeText = "A la espera de respuestas";
                    holder2.informativeText.setText(multiChoiceCardInformativeText);
                } else if (nonAnswered == 0) {
                    multiChoiceCardInformativeText = "Todos los estudiantes han contestado";
                    holder2.informativeText.setText(multiChoiceCardInformativeText);
                } else {
                    if (nonAnswered == 1) {
                        multiChoiceCardInformativeText = "Falta por contestar 1 estudiante";
                    } else {
                        multiChoiceCardInformativeText = "Faltan por contestar " + nonAnswered + " estudiantes";
                    }
                }

                holder2.informativeText.setText(multiChoiceCardInformativeText);

                for (Map.Entry<Integer, Integer> entry : counterMap.entrySet()) {
                    if (entry.getValue() != 0) {
                        holder2.listOfAnswers.setVisibility(View.VISIBLE);

                        int percentage = 0;

                        if (multichoiceCardDocument.getStudentsData().size() != 0) {
                            percentage = (entry.getValue() * 100) / multichoiceCardDocument.getStudentsData().size();
                        }

                        String questionTitle = identifierTitleMap.get(entry.getKey());
                        String text = questionTitle + ":  Contestada por " + entry.getValue() + " (" + percentage + "%)";

                        TextView textView = new TextView(holder2.questionsContainer.getContext());
                        textView.setText(text);
                        textView.setTextColor(Color.rgb(0, 0, 0));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 8, 8, 8);
                        textView.setLayoutParams(params);

                        holder2.questionsContainer.addView(textView);

                    }
                }

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

        TextView informativeText;
        TextView notAnsweredStudents;
        MaterialButton expandView;
        MaterialButton setVisibilityOff;
        ConstraintLayout expandableView;
        RecyclerView inputTextCardChildRecyclerView;

        public InputTextCardParentViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            informativeText = itemView.findViewById(R.id.informativeText);
            notAnsweredStudents = itemView.findViewById(R.id.notAnsweredStudents);
            expandView = itemView.findViewById(R.id.expandView);
            setVisibilityOff = itemView.findViewById(R.id.setVisibilityOff);
            expandableView = itemView.findViewById(R.id.expandableView);
            inputTextCardChildRecyclerView = itemView.findViewById(R.id.inputTextCardChildRecyclerView);
        }

    }

    public static class MultiChoiceCardViewHolder extends CardViewHolder {

        TextView correctAnswer;
        TextView informativeText;
        TextView listOfAnswers;
        LinearLayout questionsContainer;
        MaterialButton setVisibilityOff;

        public MultiChoiceCardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            correctAnswer = itemView.findViewById(R.id.correctAnswer);
            informativeText = itemView.findViewById(R.id.informativeText);
            listOfAnswers = itemView.findViewById(R.id.listOfAnswers);
            questionsContainer = itemView.findViewById(R.id.questionsContainer);
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

}
