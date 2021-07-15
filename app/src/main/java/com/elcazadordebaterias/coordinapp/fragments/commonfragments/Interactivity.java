package com.elcazadordebaterias.coordinapp.fragments.commonfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.InteractivityCardsAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.InputTextCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.InteractivityCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.MultichoiceCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.ReminderCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Interactivity extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private final int userType;

    public Interactivity(int userType){
        this.userType = userType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interactivity, container, false);

        // newInteractivityCard button
        FloatingActionButton newInteractivityCard = view.findViewById(R.id.newInteractivityCard);

        if(userType == UserType.TYPE_STUDENT){
            newInteractivityCard.setVisibility(View.GONE);
        }

        // Container for the interactivity cards
        RecyclerView interactivityCardsContainer = view.findViewById(R.id.interactivityCardsContainer);

        ArrayList<InteractivityCard> cardsList = new ArrayList<InteractivityCard>();
        cardsList.add(new InputTextCard("InputTextCard"));

        ArrayList<String> questions = new ArrayList<String>();
        questions.add("Napoleón");
        questions.add("Sócrates");
        questions.add("Benedicto IV");

        cardsList.add(new MultichoiceCard("MulitchoiceCard", questions));
        cardsList.add(new ReminderCard("ReminderCard", "Recordad que mañana hay examen de geografía e historia a las 11:00 PM"));

        InteractivityCardsAdapter adapter = new InteractivityCardsAdapter(cardsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        interactivityCardsContainer.setAdapter(adapter);
        interactivityCardsContainer.setLayoutManager(layoutManager);

        adapter.notifyDataSetChanged();


        return view;
    }
}