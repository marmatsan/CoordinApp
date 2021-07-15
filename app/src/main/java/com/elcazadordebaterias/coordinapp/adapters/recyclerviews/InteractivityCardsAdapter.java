package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.InputTextCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.InteractivityCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.MultichoiceCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.ReminderCard;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InteractivityCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<InteractivityCard> cardsList;

    private final int TYPE_INPUTTEXT = 0;
    private final int TYPE_CHOICES = 1;
    private final int TYPE_REMINDER = 2;

    public InteractivityCardsAdapter(ArrayList<InteractivityCard> cardsList){
        this.cardsList = cardsList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType){
            case TYPE_INPUTTEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_inputextcard, parent, false);
                return new InputTextCardViewHolder(view);
            case TYPE_CHOICES:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_multichoicescard, parent, false);
                return new MultiChoiceCardViewHolder(view);
            default: // ReminderCard
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_remindercard, parent, false);
                return new ReminderCardViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        InteractivityCard card = cardsList.get(position);

        switch(holder.getItemViewType()){
            case TYPE_INPUTTEXT:
                InputTextCard textCard = (InputTextCard) card;
                InputTextCardViewHolder holder1 = (InputTextCardViewHolder) holder;

                holder1.cardTitle.setText(textCard.getCardTitle());
                textCard.setInputText(holder1.inputText.getText().toString());
                holder1.sendResponse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Implement send response
                    }
                });
                break;

            case TYPE_CHOICES:
                MultichoiceCard choicesCard = (MultichoiceCard) card;
                MultiChoiceCardViewHolder holder2 = (MultiChoiceCardViewHolder) holder;

                holder2.cardTitle.setText(choicesCard.getCardTitle());

                for(String question : choicesCard.getQuestions()){
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

        if (card instanceof InputTextCard){
            viewType = TYPE_INPUTTEXT;
        } else if (card instanceof MultichoiceCard){
            viewType = TYPE_CHOICES;
        } else { // ReminderCard
            viewType = TYPE_REMINDER;
        }

        return viewType;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{

        TextView cardTitle;

        public CardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
        }

    }

    public static class InputTextCardViewHolder extends CardViewHolder{

        TextInputEditText inputText;
        MaterialButton sendResponse;

        public InputTextCardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            inputText = itemView.findViewById(R.id.inputText);
            sendResponse = itemView.findViewById(R.id.sendResponse);
        }

    }

    public static class MultiChoiceCardViewHolder extends CardViewHolder{

        RadioGroup radioGroup;

        public MultiChoiceCardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            radioGroup = itemView.findViewById(R.id.radioGroup);
        }

    }

    public static class ReminderCardViewHolder extends CardViewHolder{

        TextView reminderContainer;

        public ReminderCardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            reminderContainer = itemView.findViewById(R.id.reminderContainer);
        }
    }

}
