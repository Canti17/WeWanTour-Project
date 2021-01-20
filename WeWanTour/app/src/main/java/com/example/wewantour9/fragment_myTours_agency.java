package com.example.wewantour9;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_myTours_agency extends Fragment {


    public fragment_myTours_agency() {
        // Required empty public constructor
    }


    private RecyclerView mRecyclerView;
    private List_tour_inAgency_adapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseReferenceTour;
    private List<Tour> mUploads;
    private LinearLayoutManager mLayoutManager;
    private FirebaseAuth fAuth;
    FirebaseUser current_user;
    private TextView noTourLabel;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_my_tours_agency, container, false);

        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProgressCircle = view.findViewById(R.id.progress_circle);
        mUploads = new ArrayList<Tour>();

        noTourLabel=view.findViewById(R.id.txt_available_MyTour);



        mDatabaseReferenceTour = FirebaseDatabase.getInstance().getReference("TOUR");

        mDatabaseReferenceTour.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Tour upload = postSnapshot.getValue(Tour.class);
                    if(upload.getAgency().equals(current_user.getEmail())) {
                        mUploads.add(upload);
                    }
                }

                mAdapter = new List_tour_inAgency_adapter(getContext(), mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mProgressCircle.setVisibility(View.INVISIBLE);
                if(mUploads.isEmpty()){
                    noTourLabel.setVisibility(View.VISIBLE);
                }else{
                    noTourLabel.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


        return view;
    }
}
