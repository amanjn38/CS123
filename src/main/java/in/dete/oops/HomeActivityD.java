package in.dete.oops;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivityD extends AppCompatActivity implements RecyclerViewClickInterface {

    private String documentID = "", otp = "";
    private FusedLocationProviderClient mFusedLocationClient;
    private static int AUTOCOMPLETE_PLACES = 502;
    private Location selectedLocation;
    private Double dist1 = 0.00, dist2 = 0.00;
    private CardView card;
    private Button go;
    private String number;
    private SeekBar radiusSlider;
    private Double latitude = 0.00, longitude = 0.00;
    private TextView radiusMeter, locationTxt;
    private CircleImageView profile;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Restaurant> restaurantArrayList;
    private TextView restaurantName, items, price, addressHome, paymentMode, getDirections, call, distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_d);

        restaurantName = findViewById(R.id.restaurantName);
        items = findViewById(R.id.items);
        price = findViewById(R.id.price);
        addressHome = findViewById(R.id.addressHome);
        paymentMode = findViewById(R.id.paymentMode);
        getDirections = findViewById(R.id.getDirections);
        call = findViewById(R.id.call);
        card = findViewById(R.id.card);
        distance = findViewById(R.id.distance);
        radiusSlider = findViewById(R.id.radiusSlider);
        radiusMeter = findViewById(R.id.radiusMeter);
        profile = findViewById(R.id.profile);
        locationTxt = findViewById(R.id.location);
        requestPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        go = findViewById(R.id.findDistance);

        findViewById(R.id.txtCurrentLoc).setOnClickListener(v -> {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(HomeActivityD.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        selectedLocation = location;
                    }
                }
            });
        });


        card.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivityD.this, FinishActivity.class);
            intent.putExtra("documentID", documentID);
            intent.putExtra("otp", otp);
            startActivity(intent);
        });
        restaurants = new ArrayList<>();
        restaurantArrayList = new ArrayList<>();
        profile.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivityD.this, ProfileActivityD.class));
        });
        Places.initialize(HomeActivityD.this, "AIzaSyBXpHTrurGbpDlb6RXy5AisDcQkKxWiXyU");
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        locationTxt.setOnClickListener(v -> {
            startActivityForResult(new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .setCountry("IN")
                    .build(HomeActivityD.this), AUTOCOMPLETE_PLACES);
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

        go.setOnClickListener(v -> {
            if (selectedLocation == null) {
                Toast.makeText(HomeActivityD.this, "Please choose a location", Toast.LENGTH_SHORT).show();
                return;
            } else {
                FirebaseFirestore.getInstance().collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        latitude = 77.21809;
                        longitude = 28.62624;
                        Log.d("lat", latitude.toString());
                        fetchDistance(latitude, longitude);
                    }
                });
            }
        });

        getDirections.setOnClickListener(v -> {

        Uri gmmIntentUri = Uri.parse("google.navigation:q=Connaught+Place,+New+Delhi,Delhi&mode=l");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        });




        radiusSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusMeter.setText("Radius = " + (10 + progress) + " kms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void fetchDistance(Double latitude, Double longitude) {
        FirebaseFirestore.getInstance().collection("Restaurants").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    dist1 = 0.00;
                    Double lon = doc.getDouble("lon");
                    Double lat = doc.getDouble("lat");
                    Log.d("lat", doc.getDouble("lon").toString());
                    dist1 = distance(lat, lon, latitude, longitude);
                    distance.setText(Double.toString(dist1));
                    String uid = doc.get("uid").toString();
                    Log.d("uid", uid);
                    findOrder(uid);
                }
            }
        });
    }

    private void findOrder(String uid) {
        FirebaseFirestore.getInstance().collection("Orders").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Log.d("dddhhh", doc.get("restaurant-uid").toString());
                    if (doc.get("restaurant-uid").toString().equals(uid)) {
                        Log.d("statyts", doc.get("status").toString());
                        if (doc.get("status").toString().equals("Order Placed")) {
                            Log.d("name", doc.get("restaurantName").toString());
                            Log.d("items", doc.get("items").toString());
                            Log.d("price", doc.get("tp").toString());
                            Log.d("payment mode", doc.get("paymentMethod").toString());
                            Log.d("address home", doc.get("address").toString());
                            restaurantName.setText(doc.get("restaurantName").toString());
                            items.setText(doc.get("items").toString());
                            price.setText(doc.get("tp").toString());
                            addressHome.setText(doc.get("address").toString());
                            paymentMode.setText(doc.get("paymentMethod").toString());
                            number = doc.get("phone").toString();
                            String did = doc.getId();
                            otp = doc.get("otp").toString();
                            Log.d("otp", otp);
                            documentID = did;
                            HashMap<String, Object> update = new HashMap<>();
                            update.put("status", "In-Progress");
                            FirebaseFirestore.getInstance().collection("Orders").document(did).update(update);
                            break;
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == AUTOCOMPLETE_PLACES)) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.d("BidRyd", "Place: " + place.getName() + ", " + place.getId());
                locationTxt.setText(place.getName());
                selectedLocation = new Location("");
                selectedLocation.setLatitude(place.getLatLng().latitude);
                selectedLocation.setLongitude(place.getLatLng().longitude);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.d("BidRyd", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onItemClick(int position) {

    }
}
