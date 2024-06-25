package com.example.kannammajannakka;

import android.content.Context;
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

public class HomeAdapter extends FirebaseRecyclerAdapter<Model, HomeAdapter.Viewholder> {

    public HomeAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {
        // Storing Context of the data item
        Context context = holder.itemView.getContext();

        // Adding data to xml file
        holder.nameTxt.setText(model.getMechanicName());
        holder.phoneNumberTxt.setText(model.getMechanicNumber());
        holder.cityTxt.setText(model.getMechanicCity());
        holder.stateTxt.setText(model.getMechanicState());
        holder.priceTxt.setText(model.getMechanicPrice());
        holder.vehicleNameTxt.setText(model.getVehicleName());

        // Implementing onClickListener to make a Phone Call
        holder.mCallMechanicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Accessing users Phone Number
                String phone = model.getMechanicNumber();

                // Implementing phone call using Intent
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // The data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_data_layout, parent, false);
        return new Viewholder(view);
    }

    // We need view holder to hold each object from RecyclerView and to show it in RecyclerView
    class Viewholder extends RecyclerView.ViewHolder {
        TextView nameTxt, phoneNumberTxt, vehicleNameTxt, cityTxt, stateTxt, priceTxt;
        Button mCallMechanicBtn;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            // Assigning the address of the Android materials
            nameTxt = itemView.findViewById(R.id.NameTxt);
            phoneNumberTxt = itemView.findViewById(R.id.PhoneNumberTxt);
            vehicleNameTxt = itemView.findViewById(R.id.VehicleNameTxt);
            cityTxt = itemView.findViewById(R.id.CityTxt);
            stateTxt = itemView.findViewById(R.id.StateTxt);
            priceTxt = itemView.findViewById(R.id.PriceTxt);
            mCallMechanicBtn = itemView.findViewById(R.id.CallMechanicBtn);
        }
    }
}
