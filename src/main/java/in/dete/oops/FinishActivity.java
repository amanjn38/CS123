package in.dete.oops;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FinishActivity extends AppCompatActivity {

    private PinEntryEditText pinEntry;
    private String documentID, otp;
    private Button btnFinish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        pinEntry = findViewById(R.id.txt_pin_entry);
        btnFinish = findViewById(R.id.btnFinish);
        documentID = getIntent().getStringExtra("documentID");
        otp = getIntent().getStringExtra("otp");

        btnFinish.setOnClickListener(v -> {
            if (pinEntry.getText().toString().equals("") || pinEntry.getText().toString().length() < 4) {
                Toast.makeText(this, "Please enter the otp", Toast.LENGTH_SHORT).show();
            } else if (pinEntry.getText().toString().equals(otp)) {
                HashMap<String, Object> update = new HashMap<>();
                update.put("status", "Order Completed");
                FirebaseFirestore.getInstance().collection("Orders").document(documentID).update(update);
                Toast.makeText(this, "Order Completed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FinishActivity.this, HomeActivityD.class));

            } else if (!pinEntry.getText().toString().equals(otp)) {
                Toast.makeText(this, "Please enter the correct otp", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
