package com.example.kannammajannakka;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private Button searchBtn;
    private EditText vehicleNameEditTxt, cityEditTxt, stateEditTxt;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.HomeRecyclerVeiw);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vehicleNameEditTxt = view.findViewById(R.id.VehicleEditTxtHome);
        cityEditTxt = view.findViewById(R.id.CityEditTxtHome);
        stateEditTxt = view.findViewById(R.id.StateEditTxtHome);
        searchBtn = view.findViewById(R.id.SearchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });

        // Initialize the adapter with the default query
        setupRecyclerViewAdapter(FirebaseDatabase.getInstance().getReference().child("allmechanics"));

        return view;
    }

    private void performSearch() {
        String vehicle = vehicleNameEditTxt.getText().toString().toLowerCase().trim();
        String city = cityEditTxt.getText().toString().toLowerCase().trim();
        String state = stateEditTxt.getText().toString().toLowerCase().trim();

        if (vehicle.isEmpty() || city.isEmpty() || state.isEmpty()) {
            Toast.makeText(getContext(), "Please, fill in all details", Toast.LENGTH_SHORT).show();
        } else {
            String searchKey = vehicle + city + state;
            Log.d(TAG, "Constructed search key: " + searchKey);

            Query searchQuery = FirebaseDatabase.getInstance().getReference().child("mechanics")
                    .orderByChild("searchKey").equalTo(searchKey);

            Log.d(TAG, "Search Query: " + searchQuery.toString());
            setupRecyclerViewAdapter(searchQuery);
        }
    }

    private void setupRecyclerViewAdapter(Query query) {
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, Model.class)
                        .build();

        if (adapter != null) {
            adapter.stopListening();
        }

        adapter = new HomeAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Stop listening to the adapter to avoid memory leaks
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
