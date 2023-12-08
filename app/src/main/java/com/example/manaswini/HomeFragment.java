package com.example.manaswini;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

public class HomeFragment extends Fragment {
    private String phoneNumber;
    private PDFView pdfView;
    private static final int REQUEST_CALL_PERMISSION = 1;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button OpenPdf = view.findViewById(R.id.OpenPdf);
        pdfView = view.findViewById(R.id.pdfView);
        OpenPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPDF();
            }
        });
        //call button1
        Button button1 = view.findViewById(R.id.callButton1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber="100";
                makeCall(phoneNumber);
            }
        });
        //call button2
        Button button2 = view.findViewById(R.id.callButton2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("102");
            }
        });
        //call button3
        Button button3 = view.findViewById(R.id.callButton3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber="1091";
                makeCall(phoneNumber);
            }
        });
        //call button4
        Button button4 = view.findViewById(R.id.callButton4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber="101";
                makeCall(phoneNumber);
            }
        });
        return view;
    }
    //makeCall function
    private void makeCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            // Replace "phoneNumber" with the desired phone number to call
            //String phoneNumber = "+919405253452";
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(phoneNumber);
            }
        }
    }
    private void displayPDF() {
        pdfView.setVisibility(View.VISIBLE);
        pdfView.fromAsset("WomenSafetyHandbook.pdf").load();
    }
}