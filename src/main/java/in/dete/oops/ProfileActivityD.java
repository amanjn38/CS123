package in.dete.oops;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.tasks.Task;
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

public class ProfileActivityD extends AppCompatActivity {

    private EditText fullname;
    private RadioButton male, female;
    private String gender;
    private FirebaseUser firebaseUser;
    private User user;
    private TextView btnLogout;
    private EditText email, phonenumber;
    private FirebaseAuth firebaseAuth;
    private TextView save, birthdate, addId;
    private String name, phone, email1, birth, gender1, address;
    ImageView btnBack;
    private EditText aptAddress;
    public static int AUTOCOMPLETE_ACTIVITY = 180;
    private GeoPoint aptLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_d);


        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivityD.this);
            builder.setMessage("Are you sure you want to sign out of this app");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                Paper.book().destroy();
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                FirebaseAuth.getInstance().signOut();
                getSharedPreferences("MyPrefs", MODE_PRIVATE).edit().clear().apply();
                Intent intent = new Intent(ProfileActivityD.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
            builder.setNegativeButton("No", null);
            builder.create().show();
        });
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = Utils.fetchUserInfo(ProfileActivityD.this);
        fullname = findViewById(R.id.firstname);
        aptAddress = findViewById(R.id.aptAddress);
        firebaseAuth = FirebaseAuth.getInstance();

        btnBack = findViewById(R.id.btnBack);
        birthdate = findViewById(R.id.birthdate);
        email = findViewById(R.id.email);
        phonenumber = findViewById(R.id.phonenumber);
        save = findViewById(R.id.save);


        aptAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(getApplicationContext());

        });


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
                DatePickerDialog dp = new DatePickerDialog(ProfileActivityD.this, dplistener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.create();
                if (!isFinishing())
                    dp.show();
            }
        });

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

//        User user = Utils.fetchUserInfo(ProfileActivityD.this);
//        if (user != null) {
//            fullname.setText(user.getName());
//            phonenumber.setText(user.getPhone());
//            email.setText(user.getEmail());
//        }

//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//        RootRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String UID = firebaseAuth.getCurrentUser().getUid();
//                User user = dataSnapshot.child("Users").child(UID).getValue(User.class);
//                Prevalent.currentOnlineUser = user;
//                fullname.setText(user.getName());
//                phonenumber.setText(user.getPhone());
//                email.setText(user.getEmail());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

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
                        Toast.makeText(ProfileActivityD.this, "Your data has been updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProfileActivityD.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(ProfileActivityD.this, "Please enter the name", Toast.LENGTH_LONG).show();
            return false;
        }
        name = Utils.formatName(name);
        fullname.setText(name);

        if (email1 == null) {
            Toast.makeText(ProfileActivityD.this, "Please enter the email", Toast.LENGTH_LONG).show();
            return false;
        } else if (gender1 == null) {
            Toast.makeText(ProfileActivityD.this, "Please select the gender", Toast.LENGTH_LONG).show();
            return false;
        } else if (birth == null) {
            Toast.makeText(ProfileActivityD.this, "Please enter the DOB", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
