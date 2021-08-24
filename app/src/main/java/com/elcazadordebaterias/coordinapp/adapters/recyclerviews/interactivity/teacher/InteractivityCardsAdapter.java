package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.teacher;


import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.InputTextCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InputTextCardParent;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InteractivityCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.MultichoiceCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.ReminderCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
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

                holder1.averageMark.setText(inputTextCardParent.getAverageGrade());
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

                final boolean isExpanded = expandState.get(position); //Check if the view is expanded
                holder1.expandableView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

                holder1.expandView.setOnClickListener(view -> {
                    if (holder1.expandableView.getVisibility() == View.VISIBLE) {
                        holder1.expandableView.setVisibility(View.GONE);
                        holder1.expandView.setText(R.string.verRespuestas);
                        expandState.put(position, false);
                    } else {
                        holder1.expandableView.setVisibility(View.VISIBLE);
                        holder1.expandView.setText(R.string.ocultar);
                        expandState.put(position, true);
                    }
                });

                break;

            case InteractivityCardType.TYPE_CHOICES:
                /*
                MultichoiceCard choicesCard = (MultichoiceCard) card;
                MultiChoiceCardViewHolder holder2 = (MultiChoiceCardViewHolder) holder;

                holder2.cardTitle.setText(choicesCard.getCardTitle());

                for (String question : choicesCard.getQuestions()) {
                    RadioButton button = new RadioButton(holder2.radioGroup.getContext());
                    button.setText(question);
                    holder2.radioGroup.addView(button);
                }
                 */
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

        TextView averageMark;
        TextView notAnsweredStudents;
        MaterialButton expandView;
        MaterialButton showResponses;
        ConstraintLayout expandableView;
        RecyclerView inputTextCardChildRecyclerView;

        public InputTextCardParentViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            averageMark = itemView.findViewById(R.id.averageMark);
            notAnsweredStudents = itemView.findViewById(R.id.notAnsweredStudents);
            expandView = itemView.findViewById(R.id.expandView);
            showResponses = itemView.findViewById(R.id.showResponses);
            expandableView = itemView.findViewById(R.id.expandableView);
            inputTextCardChildRecyclerView = itemView.findViewById(R.id.inputTextCardChildRecyclerView);
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
