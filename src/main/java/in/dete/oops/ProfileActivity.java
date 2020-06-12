package in.dete.oops;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {

    private EditText fullname;
    private RadioButton male, female;
    private String gender;
    private TextView btnLogout;
    private FirebaseUser firebaseUser;
    private EditText email, phonenumber;
    private TextView save, birthdate, addId;
    private Location selectedLocation;
    private String name, phone, email1, birth, address, gender1;
    ImageView btnBack;
    private EditText aptAddress;
    public static int AUTOCOMPLETE_ACTIVITY = 180;
    private GeoPoint aptLoc;
    private ImageView home, cart, profile, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        cart = findViewById(R.id.cart);
        profile = findViewById(R.id.profile);

        home.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        });
        cart.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, ShowCartActivity.class));
        });
        findViewById(R.id.search).setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
        });

        findViewById(R.id.order).setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, OrderActivity.class));
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setMessage("Are you sure you want to sign out of this app");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                Paper.book().destroy();
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                FirebaseAuth.getInstance().signOut();
                getSharedPreferences("MyPrefs", MODE_PRIVATE).edit().clear().apply();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
            builder.setNegativeButton("No", null);
            builder.create().show();
        });


//        search.setOnClickListener(v -> {
//            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
//        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fullname = findViewById(R.id.firstname);
        aptAddress = findViewById(R.id.aptAddress);

        btnBack = findViewById(R.id.btnBack);
        birthdate = findViewById(R.id.birthdate);
        email = findViewById(R.id.email);
        phonenumber = findViewById(R.id.phonenumber);
        save = findViewById(R.id.save);

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        try {
            Places.initialize(ProfileActivity.this, "AIzaSyBXpHTrurGbpDlb6RXy5AisDcQkKxWiXyU");
        } catch (Exception e) {
        }
        aptAddress.setOnClickListener(v ->
                startActivityForResult(new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .setCountry("IN")
                        .build(ProfileActivity.this), AUTOCOMPLETE_ACTIVITY));


        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dplistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                birthdate.setText(" " + i2 + "." + (i1 + 1) + "." + i);
            }
        };
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(ProfileActivity.this, dplistener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.create();
                if (!isFinishing())
                    dp.show();
            }
        });


//        User user = Utils.fetchUserInfo(ProfileActivity.this);
//        if (user != null) {
//            fullname.setText(user.getName());
//            phonenumber.setText(user.getPhone());
//            email.setText(user.getEmail());
//        }


        FirebaseFirestore.getInstance().collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().getDocuments().size() != 0) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                if (doc.get("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    User user = doc.toObject(User.class);
                                    //     Log.d("nam", user.getName());
                                    fullname.setText(doc.get("n").toString());
                                    phonenumber.setText(doc.get("m").toString());
                                    email.setText(doc.get("e").toString());
                                    aptAddress.setText(doc.get("a").toString());
                                }
                            }
                        }
                    }
                });

        save.setOnClickListener(v1 -> {
            String gender = "";
            if (validate()) {
                if (male.isChecked()) {
                    gender = male.getText().toString();
                } else if (female.isChecked()) {
                    gender = female.getText().toString();
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("b", birthdate.getText().toString());
                map.put("g", gender);
                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .set(map, SetOptions.merge()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Your data has been updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        if (requestCode == AUTOCOMPLETE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                aptAddress.setText(place.getName());
                address = place.getAddress();
                aptLoc = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
                selectedLocation = new Location("");
                selectedLocation.setLatitude(place.getLatLng().latitude);
                selectedLocation.setLongitude(place.getLatLng().longitude);
                HashMap<String, Object> map = new HashMap<>();
                map.put("lat", selectedLocation.getLatitude());
                map.put("lon", selectedLocation.getLongitude());
                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

                // findViewById(R.id.addressExtraDetails).setVisibility(View.VISIBLE);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.d("Address", status.getStatusMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Boolean validate() {
        name = fullname.getText().toString();
        email1 = email.getText().toString();
        birth = birthdate.getText().toString();
        if (name == null || name.isEmpty()) {
            Toast.makeText(ProfileActivity.this, "Please enter the name", Toast.LENGTH_LONG).show();
            return false;
        }
        name = Utils.formatName(name);
        fullname.setText(name);

        if (email1 == null) {
            Toast.makeText(ProfileActivity.this, "Please enter the email", Toast.LENGTH_LONG).show();
            return false;
        } else if (birth == null) {
            Toast.makeText(ProfileActivity.this, "Please enter the DOB", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
