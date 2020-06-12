package in.dete.oops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter myAdapter;
    private ArrayList<Cart> carts;
    ArrayList  history;
    TextView textView;
    String rest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        textView = findViewById(R.id.text);
        carts = new ArrayList<>();

        findViewById(R.id.home).setOnClickListener(v -> {
            startActivity(new Intent(OrderActivity.this, HomeActivity.class));
        });

        findViewById(R.id.cart).setOnClickListener(v -> {
            startActivity(new Intent(OrderActivity.this, CartActivity.class));
        });

        findViewById(R.id.search).setOnClickListener(v -> {
            startActivity(new Intent(OrderActivity.this, SearchActivity.class));
        });

        findViewById(R.id.profile).setOnClickListener(v -> {
            startActivity(new Intent(OrderActivity.this, ProfileActivity.class));
        });


//        FirebaseFirestore.getInstance().collection("Restaurants").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot ds = task.getResult();
//                    rest = ds.getString("name");
//                }
//            }
//        });
        FirebaseFirestore.getInstance().collection("Orders")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for(DocumentSnapshot ds:task.getResult().getDocuments()){
                            String name = ds.getString("name");
                            carts = new ArrayList<>();
                            Gson gson = new Gson();
                            Log.d("test", gson.toJson(ds.get("order")));
                            JsonArray arr = gson.fromJson(gson.toJson(ds.get("order")), JsonArray.class);
                            for(JsonElement e: arr)
                                carts.add(gson.fromJson(e, Cart.class));
                            //textView.setText(Integer.toString(carts.size()));
                        }
                    }
                });
        myAdapter = new OrderAdapter(OrderActivity.this, carts);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
        recyclerView.setAdapter(myAdapter);

    }
}
