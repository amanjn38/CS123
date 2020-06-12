package in.dete.oops;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class IdkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IdkAdapter myAdapter;
    private ArrayList<IDK> carts;
    private ArrayList<IDK> getCarts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idk);

        carts = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Orders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                if (doc.get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    Log.d("carts_size", doc.get("restaurantName").toString());
                                    Log.d("total price", doc.get("tp").toString());
                                    Log.d("items", doc.get("items").toString());
                                    Log.d("status", doc.get("status").toString());
                                    Log.d("otp", doc.get("otp").toString());

                                    carts.add(new IDK(doc.get("restaurantName").toString(), doc.get("tp").toString(), doc.get("items").toString(), doc.get("status").toString(), doc.get("otp").toString()));
                                }
                            }
                        }
                        if (carts.size() > 0) {
                            myAdapter = new IdkAdapter(IdkActivity.this, carts);
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(IdkActivity.this));
                            recyclerView.setAdapter(myAdapter);

                        }
                    }
                });

    }
}
