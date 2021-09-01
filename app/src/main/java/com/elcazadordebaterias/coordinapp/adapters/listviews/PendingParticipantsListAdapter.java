package com.elcazadordebaterias.coordinapp.adapters.listviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.PetitionCard;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionGroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionUser;

import java.util.ArrayList;

/**
 * Adapter used to display the list of the participants that are going to make a group, and the status
 * of the petition, which is displayed as an image based on the status (pending, accepted or rejected).
 * The mentioned list can be displayed by clicking a button in {@link PetitionCard}
 *
 * @author Martín Mateos Sánchez
 */
public class PendingParticipantsListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PetitionGroupParticipant> items;

    public PendingParticipantsListAdapter(Context context, ArrayList<PetitionGroupParticipant> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            v = inflater.inflate(R.layout.utils_petitiongroupparticipant, null);
        } else {
            v = view;
        }

        PetitionGroupParticipant participant = items.get(position);

        TextView participantName = v.findViewById(R.id.participantName);
        ImageView participantStatus = v.findViewById(R.id.petitionStatusImage);

        participantName.setText(participant.getParticipantName());

        if (participant.getPetitionStatusImage() == PetitionUser.STATUS_PENDING) {
            participantStatus.setImageResource(R.drawable.ic_petitionpending);
        } else if (participant.getPetitionStatusImage() == PetitionUser.STATUS_ACCEPTED) {
            participantStatus.setImageResource(R.drawable.ic_petitionaccepted);
        } else if (participant.getPetitionStatusImage() == PetitionUser.STATUS_REJECTED) {
            participantStatus.setImageResource(R.drawable.ic_petitioncanceled);
        }
        return v;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
