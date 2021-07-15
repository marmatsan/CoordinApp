package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.fragments.teacher.interactivity.AskForCard;
import com.elcazadordebaterias.coordinapp.fragments.teacher.interactivity.CreateInputTextCard;

import org.jetbrains.annotations.NotNull;

public class CreateInteractivityCardDialog extends DialogFragment {

    ViewPager2 viewPager;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_createinteractivitycard, null);

        viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setUserInputEnabled(false);

        builder.setTitle("Crear una nueva tarjeta")
                .setView(view)
                .setNegativeButton("Cancelar", (dialog, i) -> {
                    // Just closes the dialog
                });

        return builder.create();
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull @NotNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            // Ask for card to be created
            if (position == 1) {
                return new CreateInputTextCard();
            } else {
                return new AskForCard();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
