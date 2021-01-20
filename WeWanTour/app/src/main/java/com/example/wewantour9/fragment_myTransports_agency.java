package com.example.wewantour9;

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
public class fragment_myTransports_agency extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private List_my_transport_inAgency_adapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseReferenceTour;
    private List<Transport> transports;
    private LinearLayoutManager mLayoutManager;
    private FirebaseAuth fAuth;
    FirebaseUser current_user;
    private TextView noTransportLabel;


    public fragment_myTransports_agency() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=  inflater.inflate(R.layout.fragment_my_transports_agency, container, false);


        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProgressCircle = view.findViewById(R.id.progress_circle);
        transports = new ArrayList<Transport>();

        noTransportLabel=view.findViewById(R.id.txt_available_MyTransport);


        mDatabaseReferenceTour = FirebaseDatabase.getInstance().getReference("TRANSPORT");
        mDatabaseReferenceTour.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                transports.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Transport transport = postSnapshot.getValue(Transport.class);
                    if(transport.getAgency().equals(current_user.getEmail())) {
                        transports.add(transport);
                    }
                }
                mAdapter = new List_my_transport_inAgency_adapter(getContext(), transports);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mProgressCircle.setVisibility(View.INVISIBLE);
                if(transports.isEmpty()){
                    noTransportLabel.setVisibility(View.VISIBLE);
                }else{
                    noTransportLabel.setVisibility(View.GONE);
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
