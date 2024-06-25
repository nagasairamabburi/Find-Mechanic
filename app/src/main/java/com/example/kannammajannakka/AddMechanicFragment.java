package com.example.kannammajannakka;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AddMechanicFragment extends Fragment {

    private EditText mechanicNameEditTxt, mechanicNumberEditTxt, mechanicCityEditTxt, mechanicStateEditTxt, mechanicPriceEditTxt, vehicleNameEditTxt;
    private Button addMechanicDetailsBtn;

    public AddMechanicFragment() {
        // Required empty public constructor
    }

    public static AddMechanicFragment newInstance(String param1, String param2) {
        AddMechanicFragment fragment = new AddMechanicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_mechanic, container, false);

        mechanicNameEditTxt = view.findViewById(R.id.MechanicNameEditTxt);
        mechanicNumberEditTxt = view.findViewById(R.id.MechanicNumberEditTxt);
        mechanicCityEditTxt = view.findViewById(R.id.CityEditTxt);
        mechanicStateEditTxt = view.findViewById(R.id.StateEditTxt);
        mechanicPriceEditTxt = view.findViewById(R.id.PriceEditTxt);
        vehicleNameEditTxt = view.findViewById(R.id.VehicleNameEditTxt);
        addMechanicDetailsBtn = view.findViewById(R.id.AddMechanicDetailsBtn);

        addMechanicDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mechanicName = mechanicNameEditTxt.getText().toString().trim();
                String mechanicNumber = mechanicNumberEditTxt.getText().toString().trim();
                String mechanicCity = mechanicCityEditTxt.getText().toString().trim();
                String mechanicState = mechanicStateEditTxt.getText().toString().trim();
                String mechanicPrice = mechanicPriceEditTxt.getText().toString().trim();
                String vehicleName = vehicleNameEditTxt.getText().toString().trim();

                if (mechanicName.isEmpty() || mechanicNumber.isEmpty() || mechanicCity.isEmpty() || mechanicState.isEmpty() || mechanicPrice.isEmpty() || vehicleName.isEmpty()) {
                    Toast.makeText(view.getContext(), "Please enter all the details", Toast.LENGTH_SHORT).show();
                } else {
                    addMechanicToDatabase(mechanicName, mechanicNumber, mechanicCity, mechanicState, mechanicPrice, vehicleName);
                }
            }
        });

        return view;
    }

    private void addMechanicToDatabase(String mechanicName, String mechanicNumber, String mechanicCity, String mechanicState, String mechanicPrice, String vehicleName) {
        String key = FirebaseDatabase.getInstance().getReference().child("mechanics").push().getKey();
        String searchKey = vehicleName.toLowerCase() + mechanicCity.toLowerCase() + mechanicState.toLowerCase();

        HashMap<String, Object> mechanicDetails = new HashMap<>();
        mechanicDetails.put("mechanicName", mechanicName);
        mechanicDetails.put("mechanicNumber", mechanicNumber);
        mechanicDetails.put("mechanicCity", mechanicCity);
        mechanicDetails.put("mechanicState", mechanicState);
        mechanicDetails.put("mechanicPrice", mechanicPrice);
        mechanicDetails.put("vehicleName", vehicleName);
        mechanicDetails.put("searchKey", searchKey);

        FirebaseDatabase.getInstance().getReference().child("mechanics").child(key).setValue(mechanicDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference().child("allmechanics").child(key).setValue(mechanicDetails)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                clearEditTexts();
                                                Toast.makeText(getContext(), "Mechanic added successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Failed to add mechanic to allmechanics", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Failed to add mechanic", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearEditTexts() {
        mechanicNameEditTxt.setText("");
        mechanicNumberEditTxt.setText("");
        mechanicCityEditTxt.setText("");
        mechanicStateEditTxt.setText("");
        mechanicPriceEditTxt.setText("");
        vehicleNameEditTxt.setText("");
    }
}
