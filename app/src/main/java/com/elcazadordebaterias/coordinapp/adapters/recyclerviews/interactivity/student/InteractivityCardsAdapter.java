package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.student;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.InputTextCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.InteractivityCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.MultichoiceCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.ReminderCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.MultichoiceCardDocument;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class InteractivityCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private ArrayList<InteractivityCard> cardsList;
    private final Context context;

    public InteractivityCardsAdapter(ArrayList<InteractivityCard> cardsList, Context context) {
        this.cardsList = cardsList;
        this.context = context;

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case InteractivityCardType.TYPE_INPUTTEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_interactivity_studentcards_inputextcard, parent, false);
                return new InputTextCardViewHolder(view);
            case InteractivityCardType.TYPE_CHOICES:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_interactivity_studentcards_multichoicescard, parent, false);
                return new MultiChoiceCardViewHolder(view);
            default: // ReminderCard
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_interactivity_studentcards_remindercard, parent, false);
                return new ReminderCardViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        InteractivityCard card = cardsList.get(position);

        switch (holder.getItemViewType()) {
            case InteractivityCardType.TYPE_INPUTTEXT:
                InputTextCard inputTextCard = (InputTextCard) card;
                InputTextCardViewHolder holder1 = (InputTextCardViewHolder) holder;

                holder1.cardTitle.setText(inputTextCard.getCardTitle());
                holder1.sendResponse.setOnClickListener(v -> {
                    String response = holder1.inputText.getText().toString();

                    if (!response.isEmpty()) {

                        DocumentSnapshot interactivityCardDocument = inputTextCard.getDocumentSnapshot();

                        InputTextCardDocument inputTextCardDocument = interactivityCardDocument.toObject(InputTextCardDocument.class);
                        DocumentReference documentReference = interactivityCardDocument.getReference();

                        ArrayList<InputTextCardDocument.InputTextCardStudentData> studentsData = new ArrayList<InputTextCardDocument.InputTextCardStudentData>();
                        for (InputTextCardDocument.InputTextCardStudentData studentData : inputTextCardDocument.getStudentsData()) {

                            if (studentData.getStudentID().equals(inputTextCard.getStudentID())) {
                                studentData.setResponse(response);
                            }
                            studentsData.add(studentData);

                        }

                        documentReference
                                .update("studentsData", studentsData)
                                .addOnSuccessListener(unused -> {
                                    documentReference
                                            .update("hasTeacherVisibility", true)
                                            .addOnSuccessListener(unused1 -> {
                                                Toast.makeText(context, "Respuesta enviada correctamente", Toast.LENGTH_SHORT).show();
                                            });
                                });
                    }
                });
                break;

            case InteractivityCardType.TYPE_CHOICES:
                MultichoiceCard multiChoiceCard = (MultichoiceCard) card;
                MultiChoiceCardViewHolder holder2 = (MultiChoiceCardViewHolder) holder;

                holder2.cardTitle.setText(multiChoiceCard.getCardTitle());
                holder2.errorMessage.setVisibility(View.GONE);

                holder2.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                    holder2.errorMessage.setVisibility(View.GONE);
                });

                HashMap<Integer, MultichoiceCardDocument.Question> questionsMap = new HashMap<Integer, MultichoiceCardDocument.Question>();

                int buttonID = 0;

                // Add questions to card
                for (MultichoiceCardDocument.Question question : multiChoiceCard.getQuestionsList()) {
                    RadioButton button = new RadioButton(holder2.radioGroup.getContext());
                    button.setText(question.getQuestionTitle());

                    int textColor = Color.parseColor("#1976d2");
                    button.setButtonTintList(ColorStateList.valueOf(textColor));

                    button.setId(buttonID);
                    questionsMap.put(buttonID, question);
                    holder2.radioGroup.addView(button);

                    buttonID++;
                }

                DocumentSnapshot interactivityCardDocument = multiChoiceCard.getDocumentSnapshot();

                MultichoiceCardDocument multiChoiceCardDocument = interactivityCardDocument.toObject(MultichoiceCardDocument.class);
                DocumentReference documentReference = interactivityCardDocument.getReference();

                holder2.sendAnswer.setOnClickListener(view -> {
                    int checkedButtonID = holder2.radioGroup.getCheckedRadioButtonId();

                    if (checkedButtonID == -1) {
                        holder2.errorMessage.setVisibility(View.VISIBLE);
                    } else {
                        holder2.errorMessage.setVisibility(View.GONE);

                        MultichoiceCardDocument.Question selectedQuestion = questionsMap.get(checkedButtonID);

                        if (selectedQuestion != null) {

                            ArrayList<MultichoiceCardDocument.MultichoiceCardStudentData> studentsData = new ArrayList<MultichoiceCardDocument.MultichoiceCardStudentData>();

                            for (MultichoiceCardDocument.MultichoiceCardStudentData studentData : multiChoiceCardDocument.getStudentsData()) {
                                if (studentData.getStudentID().equals(fAuth.getUid())) {
                                    studentData.setQuestionRespondedIdentifier(selectedQuestion.getQuestionIdentifier());
                                    if (multiChoiceCardDocument.getHasToBeEvaluated()) {
                                        if (selectedQuestion.getHasCorrectAnswer()) {
                                            studentData.setMark(1);
                                        } else {
                                            studentData.setMark(0);
                                        }
                                    }
                                }
                                studentsData.add(studentData);
                            }

                            documentReference.update("studentsData", studentsData);

                        }

                    }
                });

                break;

            default: // ReminderCard
                ReminderCard reminderCard = (ReminderCard) card;
                ReminderCardViewHolder holder3 = (ReminderCardViewHolder) holder;

                holder3.cardTitle.setText(reminderCard.getCardTitle());
                holder3.reminderContainer.setText(reminderCard.getReminderText());
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

        if (card instanceof InputTextCard) {
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

    public static class InputTextCardViewHolder extends CardViewHolder {

        TextInputEditText inputText;
        FloatingActionButton sendResponse;

        public InputTextCardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            inputText = itemView.findViewById(R.id.inputText);
            sendResponse = itemView.findViewById(R.id.sendResponse);
        }

    }

    public static class MultiChoiceCardViewHolder extends CardViewHolder {

        RadioGroup radioGroup;
        FloatingActionButton sendAnswer;
        TextView errorMessage;

        public MultiChoiceCardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            sendAnswer = itemView.findViewById(R.id.sendAnswer);
            errorMessage = itemView.findViewById(R.id.errorMessage);
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