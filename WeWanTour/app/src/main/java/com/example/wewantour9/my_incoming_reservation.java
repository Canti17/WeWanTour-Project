package com.example.wewantour9;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class my_incoming_reservation extends Fragment {

    private RecyclerView mRecyclerView;
    private My_reservation_customer_adapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseReferenceTour;
    private List<Reservation> reservations;
    private LinearLayoutManager mLayoutManager;
    private FirebaseAuth fAuth;
    FirebaseUser current_user;

    private View view;

    public boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        boolean sameYear = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
        boolean sameMonth = calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
        boolean sameDay = calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
        return (sameDay && sameMonth && sameYear);
    }

    public my_incoming_reservation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_my_future_reservation_customer, container, false);

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_my_past_reservation_customer, container, false);

        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProgressCircle = view.findViewById(R.id.progress_circle);
        reservations = new ArrayList<Reservation>();




        mDatabaseReferenceTour = FirebaseDatabase.getInstance().getReference("RESERVATION");
        mDatabaseReferenceTour.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reservations.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Reservation reserv = postSnapshot.getValue(Reservation.class);
                    if (current_user.getEmail().equals(reserv.getCustomer())) {
                        String start_date = reserv.getTour().getStartDate();
                        Date date1 = null;
                        try {
                            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(start_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date current_date = new Date();

                        if (isSameDay(date1,current_date) || date1.after(current_date)) {
                            reservations.add(reserv);
                        }
                    }
                }
                mAdapter = new My_reservation_customer_adapter(getContext(), reservations);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mProgressCircle.setVisibility(View.INVISIBLE);
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