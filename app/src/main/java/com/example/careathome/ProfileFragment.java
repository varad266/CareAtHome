//package com.example.careathome;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.Objects;
//
//public class ProfileFragment extends Fragment {
//
//    private static final String TAG = "ProfileFragment";
//
//    // UI components
//    private TextView txtFullName, txtFirstName, txtLastName, txtEmail, txtAddress, txtPhone;
//    private ProgressBar progressBar;
//    private Button buttonRefresh;
//    private ImageButton buttonLogout;
//
//    // Firebase instances
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
//    private FirebaseUser currentUser;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//
//        // Initialize UI components
//        txtFullName = view.findViewById(R.id.txtFullName);
//        txtFirstName = view.findViewById(R.id.txtFirstName);
//        txtLastName = view.findViewById(R.id.txtLastName);
//        txtEmail = view.findViewById(R.id.txtEmail);
//        txtAddress = view.findViewById(R.id.txtAddress);
//        txtPhone = view.findViewById(R.id.txtPhone);
//        progressBar = view.findViewById(R.id.progressBar);
//        buttonRefresh = view.findViewById(R.id.buttonRefresh);
//        buttonLogout = view.findViewById(R.id.buttonLogout);
//
//        // Initialize Firebase instances
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//        mDatabase = FirebaseDatabase.getInstance().getReference("Users"); // Corrected path to match your signup code
//
//        // Set up click listeners
//        buttonRefresh.setOnClickListener(v -> fetchUserProfile());
//        buttonLogout.setOnClickListener(v -> logoutUser());
//
//        // Fetch user data when the fragment is created
//        fetchUserProfile();
//
//        return view;
//    }
//
//    private void fetchUserProfile() {
//        // Show loading progress bar and hide refresh button
//        progressBar.setVisibility(View.VISIBLE);
//        buttonRefresh.setVisibility(View.GONE);
//
//        if (currentUser == null) {
//            // User not logged in
//            Toast.makeText(getContext(), "Please log in to view your profile.", Toast.LENGTH_SHORT).show();
//            progressBar.setVisibility(View.GONE);
//            buttonRefresh.setVisibility(View.VISIBLE);
//            return;
//        }
//
//        String userId = currentUser.getUid();
//        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Data exists, retrieve it as a Pojo object
//                    // *** THIS IS THE KEY CHANGE ***
//                    Pojo user = dataSnapshot.getValue(Pojo.class);
//                    if (user != null) {
//                        updateUI(user);
//                    } else {
//                        Log.e(TAG, "Pojo object is null after dataSnapshot.getValue()");
//                        Toast.makeText(getContext(), "Failed to parse user data.", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    // Data does not exist for the current user
//                    Log.d(TAG, "No user data found for ID: " + userId);
//                    Toast.makeText(getContext(), "Profile data not found.", Toast.LENGTH_SHORT).show();
//                }
//
//                // Hide progress bar and show refresh button
//                progressBar.setVisibility(View.GONE);
//                buttonRefresh.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Failed to read value
//                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
//                Toast.makeText(getContext(), "Failed to load profile data.", Toast.LENGTH_SHORT).show();
//
//                // Hide progress bar and show refresh button
//                progressBar.setVisibility(View.GONE);
//                buttonRefresh.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//
//    private void updateUI(Pojo user) {
//        // Set the UI elements with the retrieved user data
//        String fullName = user.getFirstName() + " " + user.getLastName();
//        txtFullName.setText(fullName);
//        txtFirstName.setText(user.getFirstName());
//        txtLastName.setText(user.getLastName());
//        txtEmail.setText(user.getEmail());
//        txtAddress.setText(user.getAddress());
//        txtPhone.setText(user.getPhoneNumber()); // Note the getPhoneNumber() method call
//    }
//
//    private void logoutUser() {
//        mAuth.signOut();
//        Toast.makeText(getContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
//        // You would typically navigate to the login/splash screen here
//    }
//}



package com.example.careathome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    // UI components
    private TextView txtFullName, txtFirstName, txtLastName, txtEmail, txtAddress, txtPhone;
    private ProgressBar progressBar;
    private Button buttonRefresh;
    private ImageButton buttonLogout;

    // Firebase instances
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize UI components
        txtFullName = view.findViewById(R.id.txtFullName);
        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.txtLastName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtPhone = view.findViewById(R.id.txtPhone);
        progressBar = view.findViewById(R.id.progressBar);
        buttonRefresh = view.findViewById(R.id.buttonRefresh);
        buttonLogout = view.findViewById(R.id.buttonLogout);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users"); // Corrected path to match your signup code

        // Set up click listeners
        buttonRefresh.setOnClickListener(v -> fetchUserProfile());
        buttonLogout.setOnClickListener(v -> logoutUser());

        // Fetch user data when the fragment is created
        fetchUserProfile();

        return view;
    }

    private void fetchUserProfile() {
        // Show loading progress bar and hide refresh button
        progressBar.setVisibility(View.VISIBLE);
        buttonRefresh.setVisibility(View.GONE);

        if (currentUser == null) {
            // User not logged in
            Toast.makeText(getContext(), "Please log in to view your profile.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            buttonRefresh.setVisibility(View.VISIBLE);
            return;
        }

        String userId = currentUser.getUid();
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Data exists, retrieve it as a Pojo object
                    // *** THIS IS THE KEY CHANGE ***
                    Pojo user = dataSnapshot.getValue(Pojo.class);
                    if (user != null) {
                        updateUI(user);
                    } else {
                        Log.e(TAG, "Pojo object is null after dataSnapshot.getValue()");
                        Toast.makeText(getContext(), "Failed to parse user data.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Data does not exist for the current user
                    Log.d(TAG, "No user data found for ID: " + userId);
                    Toast.makeText(getContext(), "Profile data not found.", Toast.LENGTH_SHORT).show();
                }

                // Hide progress bar and show refresh button
                progressBar.setVisibility(View.GONE);
                buttonRefresh.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load profile data.", Toast.LENGTH_SHORT).show();

                // Hide progress bar and show refresh button
                progressBar.setVisibility(View.GONE);
                buttonRefresh.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateUI(Pojo user) {
        // Set the UI elements with the retrieved user data
        String fullName = user.getFirstName() + " " + user.getLastName();
        txtFullName.setText(fullName);
        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtEmail.setText(user.getEmail());
        txtAddress.setText(user.getAddress());
        txtPhone.setText(user.getPhoneNumber()); // Note the getPhoneNumber() method call
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();

        // Navigate to the login screen and clear the back stack
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Finish the current activity
        requireActivity().finish();
    }
}
