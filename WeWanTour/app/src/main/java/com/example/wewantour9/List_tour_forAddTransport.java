package com.example.wewantour9;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;
        import java.util.Objects;

public class List_tour_forAddTransport  extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List_tour_forAddTransport_adapter mAdapter;
    private ProgressBar mProgressCircle;
    private TextView noToursLabel;

    private DatabaseReference mDatabaseReferenceTour;
    private List<Tour> tours;
    private LinearLayoutManager mLayoutManager;
    private Activity activity;
    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour_for_add_transport);

        noToursLabel = findViewById(R.id.txt_available_tours);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProgressCircle = findViewById(R.id.progress_circle);
        tours = new ArrayList<Tour>();

        activity=this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        mDatabaseReferenceTour = FirebaseDatabase.getInstance().getReference("TOUR");
        mDatabaseReferenceTour.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tours.clear();

                //check if the current date is after the tour starting date
                final Calendar c = Calendar.getInstance();
                int current_year = c.get(Calendar.YEAR);
                int current_month = c.get(Calendar.MONTH)+1;
                int current_day = c.get(Calendar.DAY_OF_MONTH);

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Tour upload = postSnapshot.getValue(Tour.class);
                    String[] dateSplit = upload.getStartDate().split("/");
                    if( !((current_year>Integer.parseInt(dateSplit[2])) ||
                            ((current_year == Integer.parseInt(dateSplit[2])) && current_month > Integer.parseInt(dateSplit[1])) ||
                            ((current_year == Integer.parseInt(dateSplit[2])) && current_month == Integer.parseInt(dateSplit[1]) && current_day > Integer.parseInt(dateSplit[0]))) ){
                        tours.add(upload);
                    }
                }
                if(tours.isEmpty()){
                    noToursLabel.setVisibility(View.VISIBLE);
                }else{
                    noToursLabel.setVisibility(View.GONE);
                }
                mAdapter = new List_tour_forAddTransport_adapter(List_tour_forAddTransport.this, tours);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(List_tour_forAddTransport.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}
