package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;

import org.jetbrains.annotations.NotNull;

public class CreateMultichoiceCardDialog extends DialogFragment {

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.utils_dialogs_createinputtextcarddialog, null);


        builder.setTitle("Crear una nueva tarjeta")
                .setView(view)
                .setNegativeButton("Cancelar", (dialog, i) -> {
                    // Just closes the dialog
                });

        return builder.create();
    }

}