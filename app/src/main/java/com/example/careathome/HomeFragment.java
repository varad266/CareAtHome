package com.example.careathome;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private final String[] services = {
            "Electrician","Carpenter","Mechanic","Plumber","Maid","CareTaker",
            "Gardener","Beauty Parlor","Towing","Appliances Service","Photographer",
            "Welding","Barber","Transport","AC Service"
    };

    private final int[] cardIds = {
            R.id.card_electrician, R.id.card_carpenter, R.id.card_mechanic,
            R.id.card_plumber, R.id.card_maid, R.id.card_caretaker,
            R.id.card_gardener, R.id.card_beauty, R.id.card_towing,
            R.id.card_appliances, R.id.card_photographer, R.id.card_welding,
            R.id.card_barber, R.id.card_transport, R.id.card_ac
    };

    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // ✅ Realtime Database root reference
        dbRef = FirebaseDatabase.getInstance().getReference("appointments");

        // Attach click listeners for service cards
        for (int i = 0; i < cardIds.length; i++) {
            final int index = i;
            CardView card = view.findViewById(cardIds[i]);
            if (card != null) {
                card.setOnClickListener(v -> showAppointmentDialog(services[index]));
            }
        }
        return view;
    }

    private void showAppointmentDialog(String serviceName) {
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_schedule, null);

        TextView tvServiceName      = dialogView.findViewById(R.id.tvServiceName);
        TextInputEditText edtDate   = dialogView.findViewById(R.id.edtDate);
        TextInputEditText edtTime   = dialogView.findViewById(R.id.edtTime);
        TextInputEditText edtNotes  = dialogView.findViewById(R.id.edtNotes);
        TextInputEditText edtAddress= dialogView.findViewById(R.id.edtAddress);
        MaterialButton btnPayment   = dialogView.findViewById(R.id.btnPayment);

        tvServiceName.setText(serviceName);

        // Date & Time pickers
        edtDate.setOnClickListener(v -> pickDate(edtDate));
        edtTime.setOnClickListener(v -> pickTime(edtTime));

        // ✅ PAY button → save to Realtime Database
        btnPayment.setOnClickListener(v -> {
            String date    = getText(edtDate);
            String time    = getText(edtTime);
            String notes   = getText(edtNotes);
            String address = getText(edtAddress);

            if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(address)) {
                Toast.makeText(getContext(),
                        "Please select date, time and enter address",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            saveAppointmentToRealtimeDB(serviceName, date, time, notes, address);
        });

        new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setNegativeButton("Close", null)
                .show();
    }

    /**
     * Save appointment details to Firebase Realtime Database
     */
    private void saveAppointmentToRealtimeDB(String serviceName,
                                             String date,
                                             String time,
                                             String notes,
                                             String address) {

        String appointmentId = dbRef.push().getKey(); // Auto ID

        if (appointmentId == null) {
            Toast.makeText(getContext(),
                    "Failed to generate appointment ID",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> appointment = new HashMap<>();
        appointment.put("service", serviceName);
        appointment.put("date", date);
        appointment.put("time", time);
        appointment.put("notes", notes);
        appointment.put("address", address);
        appointment.put("timestamp", System.currentTimeMillis());

        // ✅ Write to /appointments/{appointmentId}
        dbRef.child(appointmentId)
                .setValue(appointment)
                .addOnSuccessListener(unused -> Toast.makeText(getContext(),
                        "✅ Appointment saved successfully!",
                        Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(),
                        "❌ Failed to save: " + e.getMessage(),
                        Toast.LENGTH_LONG).show());
    }

    private void pickDate(EditText target) {
        final Calendar c = Calendar.getInstance();
        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day   = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(requireContext(),
                (view, y, m, d) ->
                        target.setText(String.format("%02d/%02d/%04d", d, m + 1, y)),
                year, month, day);

        dpd.getDatePicker().setMinDate(System.currentTimeMillis()); // Prevent past dates
        dpd.show();
    }

    private void pickTime(EditText target) {
        final Calendar c = Calendar.getInstance();
        int hour   = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(requireContext(),
                (view, h, m) ->
                        target.setText(String.format("%02d:%02d", h, m)),
                hour, minute, false);
        tpd.show();
    }

    private String getText(EditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
