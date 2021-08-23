package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.student;

import android.content.Context;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
                MultichoiceCard choicesCard = (MultichoiceCard) card;
                MultiChoiceCardViewHolder holder2 = (MultiChoiceCardViewHolder) holder;

                holder2.cardTitle.setText(choicesCard.getCardTitle());

                for (String question : choicesCard.getQuestions()) {
                    RadioButton button = new RadioButton(holder2.radioGroup.getContext());
                    button.setText(question);
                    holder2.radioGroup.addView(button);
                }
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
        MaterialButton sendAnswer;

        public MultiChoiceCardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            sendAnswer = itemView.findViewById(R.id.sendAnswer);
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
