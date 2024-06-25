package com.example.kannammajannakka;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MechanicAdapter extends FirebaseRecyclerAdapter<Model, MechanicAdapter.Viewholder> {

    // Constructor
    public MechanicAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {
        holder.nameTxt.setText(model.getMechanicName());
        holder.phoneNumberTxt.setText(model.getMechanicNumber());
        holder.cityTxt.setText(model.getMechanicCity());
        holder.stateTxt.setText(model.getMechanicState());
        holder.priceTxt.setText(model.getMechanicPrice());
        holder.vehicleNameTxt.setText(model.getVehicleName());
        holder.callMechanicBtn.setVisibility(View.VISIBLE);

        holder.callMechanicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Accessing users Phone Number
                String phone = model.getMechanicNumber();

                // Implementing phone call using Intent
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // The data objects are inflated into the XML file user_data_layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_data_layout, parent, false);
        return new Viewholder(view);
    }

    // We need ViewHolder to hold each object from RecyclerView and to show it in RecyclerView
    class Viewholder extends RecyclerView.ViewHolder {

        TextView nameTxt, phoneNumberTxt, vehicleNameTxt, cityTxt, stateTxt, priceTxt;
        Button callMechanicBtn;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            // Assigning the address of the Android materials to show mechanic details
            nameTxt = itemView.findViewById(R.id.NameTxt);
            phoneNumberTxt = itemView.findViewById(R.id.PhoneNumberTxt);
            vehicleNameTxt = itemView.findViewById(R.id.VehicleNameTxt);
            cityTxt = itemView.findViewById(R.id.CityTxt);
            stateTxt = itemView.findViewById(R.id.StateTxt);
            priceTxt = itemView.findViewById(R.id.PriceTxt);
            callMechanicBtn = itemView.findViewById(R.id.CallMechanicBtn);
        }
    }
}
