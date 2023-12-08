package com.example.manaswini;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment {

    EditText nameEditText1, phoneEditText1, nameEditText2, phoneEditText2, nameEditText3, phoneEditText3, nameEditText4, phoneEditText4;
    SharedPreferences sharedPreferences;
    private TextInputEditText msgEditText;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Initialize your EditText and TextView references
        nameEditText1 = view.findViewById(R.id.nameEditText1);
        phoneEditText1 = view.findViewById(R.id.phoneNumberEditText1);
        nameEditText2 = view.findViewById(R.id.nameEditText2);
        phoneEditText2 = view.findViewById(R.id.phoneNumberEditText2);
        nameEditText3 = view.findViewById(R.id.nameEditText3);
        phoneEditText3 = view.findViewById(R.id.phoneNumberEditText3);
        nameEditText4 = view.findViewById(R.id.nameEditText4);
        phoneEditText4 = view.findViewById(R.id.phoneNumberEditText4);
        msgEditText = view.findViewById(R.id.emergmsgtextbox);


        // Load previously saved data (if any)
        loadSavedData();

        // Add a click listener to a save button or any other event that triggers saving
        Button saveButton = view.findViewById(R.id.saveNoBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        loadSavedMsg();
        Button msgsaveButton = view.findViewById(R.id.msgSaveBtn);
        msgsaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMsgData();
            }
        });

        return view;
    }

    private void loadSavedData() {
        // Load data from SharedPreferences and populate the EditTexts
        nameEditText1.setText(sharedPreferences.getString("name1", ""));
        phoneEditText1.setText(sharedPreferences.getString("phone1", ""));
        nameEditText2.setText(sharedPreferences.getString("name2", ""));
        phoneEditText2.setText(sharedPreferences.getString("phone2", ""));
        nameEditText3.setText(sharedPreferences.getString("name3", ""));
        phoneEditText3.setText(sharedPreferences.getString("phone3", ""));
        nameEditText4.setText(sharedPreferences.getString("name4", ""));
        phoneEditText4.setText(sharedPreferences.getString("phone4", ""));
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name1", nameEditText1.getText().toString());
        editor.putString("phone1", phoneEditText1.getText().toString());
        editor.putString("name2", nameEditText2.getText().toString());
        editor.putString("phone2", phoneEditText2.getText().toString());
        editor.putString("name3", nameEditText3.getText().toString());
        editor.putString("phone3", phoneEditText3.getText().toString());
        editor.putString("name4", nameEditText4.getText().toString());
        editor.putString("phone4", phoneEditText4.getText().toString());
        editor.apply();

        Toast.makeText(requireContext(), "Data saved!", Toast.LENGTH_SHORT).show();
    }

    private void saveMsgData() {
        String userInput = msgEditText.getText().toString();

        // Save the user's input to SharedPreferences
        SharedPreferences.Editor editr = sharedPreferences.edit();
        editr.putString("userMessage", userInput);
        editr.apply();

        // Optionally, you can provide feedback to the user
        Toast.makeText(requireContext(), "Data saved!", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedMsg() {
        // Load data from SharedPreferences
        String savedMessage = sharedPreferences.getString("userMessage", "");

        // Display the saved message, e.g., set it in the TextInputEditText
        msgEditText.setText(savedMessage);
    }
}