package com.example.manaswini;

import android.Manifest;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class    AlertFragment extends Fragment {
    private static final int REQUEST_SMS_PERMISSION = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    boolean locationToastDisplayed=false;

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Handle location updates here (if needed)
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    private TextView locationTextView;

    public AlertFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        ImageButton alertButton = view.findViewById(R.id.alertButton);
        locationTextView = view.findViewById(R.id.locationTextView);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSmsPermission();
            }
        });
        return view;
    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // Permissions already granted, get the location and send SMS
            onSendButtonClickWithLocation();
        }
    }

    private void onSendButtonClickWithLocation() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String message = preferences.getString("userMessage", "");
        String[] contactKeys = {"phone1", "phone2", "phone3", "phone4"};
        for (String contactKey : contactKeys) {
            String phoneNumber = preferences.getString(contactKey, "");

            if (!phoneNumber.isEmpty()) {
                try {
                    // Get the current location
                    Location currentLocation = getCurrentLocation();

                    Handler handler=new Handler();
                    handler.postDelayed(()->{
                        locationToastDisplayed=false;
                    },10000);

                    if (currentLocation != null) {
                        double latitude = currentLocation.getLatitude();
                        double longitude = currentLocation.getLongitude();

                        //get address using geocoder
                        String addressWithLocation = getAddressWithLocation(latitude, longitude);
                        // Include the location in the SMS message
                        String messageWithLocation = message + " My Current Location: " + addressWithLocation;

                        locationTextView.setText(addressWithLocation);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, messageWithLocation, null, null);
                        // SMS sent successfully
                        Log.e("SmsManager", "SMS sent to " + phoneNumber);
                        //------display address on screen as well
                        String[] contactnames = {"name1", "name2", "name3", "name4"};
                        for (String name : contactnames) {
                            String savedname = preferences.getString(name, "");
                            Toast.makeText(requireContext(), "SMS Sent Successfully to " + savedname, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (!locationToastDisplayed) {
                            // Handle the case if location couldn't be retrieved
                            Log.e("SmsManager", "Unable to retrieve current location");
                            // You can also show a toast message to inform the user about the location issue
                            Toast.makeText(requireContext(), "Unable to retrieve current location", Toast.LENGTH_SHORT).show();
                            locationToastDisplayed = true;
                        }
                    }
                } catch (Exception e) {
                    // Handle the exception if SMS sending failed
                    Log.e("SmsManager", "Error sending SMS to " + phoneNumber + ": " + e.getMessage());
                    // You can also show a toast message for SMS sending failure
                    Toast.makeText(requireContext(), "Error sending SMS", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getAddressWithLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        String addressString = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                addressString = address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressString;
    }

    private Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return lastKnownLocation;
        } else {
            // Handle the case when the location permission is not granted
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Proceed to send SMS with location
                onSendButtonClickWithLocation();
            } else {
                // Permission denied, show a message
                Toast.makeText(requireContext(), "SMS permission is Required to send SMS", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed to send SMS with location
                onSendButtonClickWithLocation();
            } else {
                // Location Permission Denied
                Toast.makeText(requireContext(), "Location Permission is required to get the current location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}