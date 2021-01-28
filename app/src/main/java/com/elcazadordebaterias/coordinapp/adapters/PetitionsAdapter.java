 package com.elcazadordebaterias.coordinapp.adapters;

 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.TextView;

 import androidx.annotation.NonNull;
 import androidx.recyclerview.widget.RecyclerView;

 import com.elcazadordebaterias.coordinapp.R;
 import com.elcazadordebaterias.coordinapp.utils.CardItemRequest;
 import com.elcazadordebaterias.coordinapp.utils.FirebaseRequestInfo;
 import com.elcazadordebaterias.coordinapp.utils.StudentInfo;

 import com.google.android.material.button.MaterialButton;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.firestore.FirebaseFirestore;
 import com.google.firebase.firestore.QueryDocumentSnapshot;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map;

 public class PetitionsAdapter extends RecyclerView.Adapter<PetitionsAdapter.PetitionsViewHolder> {

     FirebaseAuth fAuth = FirebaseAuth.getInstance();
     FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    private ArrayList<CardItemRequest> requestList;

    public static class PetitionsViewHolder extends RecyclerView.ViewHolder{

        MaterialButton cancelrequest;
        MaterialButton acceptrequest;

        TextView studentname;
        TextView coursenumber;

        public PetitionsViewHolder(View view){
            super(view);
            cancelrequest = view.findViewById(R.id.cancel_request);
            acceptrequest = view.findViewById(R.id.accept_request);
            studentname = view.findViewById(R.id.studentname);
            coursenumber = view.findViewById(R.id.requestedcourse);
        }

    }

    public PetitionsAdapter(ArrayList<CardItemRequest> requestList){
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public PetitionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_requestsubjectcard, parent, false);
        return new PetitionsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PetitionsViewHolder holder, int position) {
        CardItemRequest currentItem = requestList.get(position);

        holder.studentname.setText(currentItem.getStudentName());
        holder.coursenumber.setText(currentItem.getCourseNumber());
        holder.cancelrequest.setOnClickListener(view -> {

            Map<String, Object> firebaseRequests = new HashMap<String, Object>();

            // Delete the document from the collection
            fStore.collection("Requests")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) { // Get all the documents of the collection "Requests" and add them to firebaseRequests map being the studentId the key
                                firebaseRequests.put(document.getId(), document.toObject(FirebaseRequestInfo.class));
                            }

                            for (Map.Entry<String, Object> entry : firebaseRequests.entrySet()) {
                                FirebaseRequestInfo document = (FirebaseRequestInfo) entry.getValue();

                                if (fAuth.getCurrentUser().getUid().equals(document.getTeacherId())) { // The teacher id of the document has to match the id of the logged teacher. Remove the petition
                                    fStore.collection("Requests").document(document.getStudentId()).delete();

                                    requestList.remove(position);
                                    notifyDataSetChanged();
                                }
                            }
                        } // TODO: 28-01-2021 Check if task fails
                    });

        });
        holder.acceptrequest.setOnClickListener(view -> {

            Map<String, Object> firebaseRequests = new HashMap<String, Object>();

            // Add the information to groups collection, then delete the document from the collection
            fStore.collection("Requests")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) { // Get all the documents of the collection "Requests" and add them to firebaseRequests map being the studentId the key
                                firebaseRequests.put(document.getId(), document.toObject(FirebaseRequestInfo.class));
                            }

                            for (Map.Entry<String, Object> entry : firebaseRequests.entrySet()) {
                                FirebaseRequestInfo document = (FirebaseRequestInfo) entry.getValue();

                                if (fAuth.getCurrentUser().getUid().equals(document.getTeacherId())) { // The teacher id of the document has to match the id of the logged teacher. Remove the petition

                                    StudentInfo studeninfo = new StudentInfo(document.getStudentName(), document.getStudentId());

                                    fStore.collection("Groups").document(document.getCourseNumber() + " " + document.getCourseNumberLetter()).set(studeninfo);

                                    fStore.collection("Requests").document(document.getStudentId()).delete();

                                    requestList.remove(position);
                                    notifyDataSetChanged();
                                }
                            }
                        } // TODO: 28-01-2021 Check if task fails
                    });
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

}
