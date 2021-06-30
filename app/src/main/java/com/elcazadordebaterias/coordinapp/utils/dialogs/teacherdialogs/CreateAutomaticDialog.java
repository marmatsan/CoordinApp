package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import androidx.fragment.app.DialogFragment;


/**
 * Dialog accessible only by the teacher to let the app create a group based on some rules.
 *
 * @author Martín Mateos Sánchez
 */
public class CreateAutomaticDialog extends DialogFragment {
    /*
    private Spinner courseSpinner, subjectSpinner, modeSpinner;
    private EditText numberInput;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_createautomaticdialog, null);

        courseSpinner =  view.findViewById(R.id.courseSpinner);
        subjectSpinner = view.findViewById(R.id.subjectSpinner);
        modeSpinner = view.findViewById(R.id.modeSpinner);
        numberInput = view.findViewById(R.id.numberInput);

        ArrayList<String> coursesNames = new ArrayList<String>();
        ArrayList<String> subjectNames = new ArrayList<String>();

        // Course adapter
        ArrayAdapter<String> courseListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, coursesNames);

        courseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseListAdapter);
        courseSpinner.setSelection(0);

        // Course list spinner
        fStore.collection("CoursesOrganization").whereArrayContains("allParticipantsIDs", fAuth.getUid()).get().addOnCompleteListener(task -> { // Get group names
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    coursesNames.add(document.getId());
                }
            }
            courseListAdapter.notifyDataSetChanged();
        });

        // Subjects adapter
        ArrayAdapter<String> subjectListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subjectNames);

        subjectListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectListAdapter);
        subjectSpinner.setSelection(0);

        // Mode adapter
        String[] modes = new String[]{"Dividir los grupos en partes iguales"};
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, modes);

        subjectListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);
        modeSpinner.setSelection(0);

        // Listeners when we select the items
        // Course spinner listener
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectNames.clear();
                String selectedCourseName = parent.getItemAtPosition(position).toString();

                fStore.collection("CoursesOrganization").document(selectedCourseName).collection("Subjects").whereEqualTo("teacherID", fAuth.getUid()).get().addOnCompleteListener(task -> { // Get group names
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            subjectNames.add(document.getId());
                        }
                    }
                    subjectListAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        builder.setView(view).setTitle("Menú de creación de grupos")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                })
                .setPositiveButton("Solicitar", (dialogInterface, i) -> {

                    String selectedCourse = courseSpinner.getSelectedItem().toString();
                    String selectedSubject = subjectSpinner.getSelectedItem().toString();
                    String selectedMode = modeSpinner.getSelectedItem().toString();
                    String selectedSplitString = numberInput.getText().toString();

                    // TODO: Error checking in edittext
                    fStore.collection("CoursesOrganization").document(selectedCourse).collection("Subjects").document(selectedSubject).get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Subject subject = document.toObject(Subject.class);

                                ArrayList<String> studentIDs = subject.getStudentIDs();
                                Collections.shuffle(studentIDs);

                                String teacherID = subject.getTeacherID();
                                int splitNumber = Integer.parseInt(selectedSplitString);

                                int totalGroups = (int) Math.floor(studentIDs.size()/splitNumber);
                                int remainderStudents = studentIDs.size()%splitNumber;

                                if(remainderStudents == 1) {
                                    Toast.makeText(context, "Un estudiante se quedará sin grupo. Introduce otro número", Toast.LENGTH_SHORT).show();
                                }else{
                                    for (int j = 0; j < totalGroups; j++) {
                                        createGroup(selectedCourse, selectedSubject, studentIDs.subList(j * splitNumber, j * splitNumber + splitNumber), teacherID);
                                    }

                                    if (remainderStudents != 0) {
                                        createGroup(selectedCourse, selectedSubject, studentIDs.subList(studentIDs.size() - remainderStudents, studentIDs.size()), teacherID);
                                    }
                                }
                            }
                        }
                    });
                });

        return builder.create();
    }

    private void createGroup(String course, String subject, List<String> studentIDs, String teacherID){

        ArrayList<String> participantsIds = new ArrayList<String>(studentIDs);

        ArrayList<GroupParticipant> participants = new ArrayList<GroupParticipant>();

        fStore.collection("Students").whereIn(FieldPath.documentId(), participantsIds).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                   participants.add(new GroupParticipant((String) document.get("FullName"), false, document.getId()));
                }

                fStore.collection("Teachers").document(teacherID).get().addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        participants.add(new GroupParticipant((String) documentSnapshot.get("FullName"), true, documentSnapshot.getId()));
                        participantsIds.add(teacherID);

                        Group group = new Group(
                                fAuth.getUid(),
                                course,
                                subject,
                                participantsIds,
                                participants);

                        fStore.collection("CoursesOrganization")
                                .document(group.getGroupName())
                                .collection("Subjects")
                                .document(group.getSubjectName())
                                .collection("Groups").add(group);
                    }
                });
            }
        });
    }
*/
}
