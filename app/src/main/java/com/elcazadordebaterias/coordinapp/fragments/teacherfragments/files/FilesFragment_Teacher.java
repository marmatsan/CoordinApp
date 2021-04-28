package com.elcazadordebaterias.coordinapp.fragments.teacherfragments.files;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.elcazadordebaterias.coordinapp.R;

/**
 * The fragment representing the Files Tab of the teacher.
 * @author Martín Mateos Sánchez
 */
public class FilesFragment_Teacher extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_files_teacher, container, false);
    }
}