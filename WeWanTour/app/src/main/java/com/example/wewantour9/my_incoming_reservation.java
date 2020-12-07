package com.example.wewantour9;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
    private My_reservation_customer_adapter cAdapter;
    private My_reservation_agency_adapter aAdapter;
    private ProgressBar mProgressCircle;
    private TextView noReservationsLabel;
    private DatabaseReference mDatabaseReferenceTour, db_customer_reservations;
    private List<Reservation> reservations;
    private LinearLayoutManager mLayoutManager;
    private FirebaseAuth fAuth;
    FirebaseUser current_user;
    boolean flagIsCustomer = false;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_my_incoming_reservation, container, false);

        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProgressCircle = view.findViewById(R.id.progress_circle);
        reservations = new ArrayList<Reservation>();

        noReservationsLabel=view.findViewById(R.id.txt_available_IncomingReservation);

        mDatabaseReferenceTour = FirebaseDatabase.getInstance().getReference("RESERVATION");
        db_customer_reservations= FirebaseDatabase.getInstance().getReference("USER/Customer");

        //get the user id
        db_customer_reservations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Customer customer_buffer = postSnapshot.getValue(Customer.class);
                    if(customer_buffer.getEmail().equals(current_user.getEmail())){
                        flagIsCustomer = true;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

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

                if(reservations.isEmpty()){
                    noReservationsLabel.setVisibility(View.VISIBLE);
                }else{
                    noReservationsLabel.setVisibility(View.GONE);
                }

                if(flagIsCustomer){
                    cAdapter = new My_reservation_customer_adapter(getContext(), reservations);
                    mRecyclerView.setAdapter(cAdapter);
                }else{
                    aAdapter = new My_reservation_agency_adapter(getContext(), reservations);
                    mRecyclerView.setAdapter(aAdapter);
                }
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