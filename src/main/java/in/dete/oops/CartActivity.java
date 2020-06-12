package in.dete.oops;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    private RecyclerView recyclerView;
    private CartAdapter myAdapter;
    final int SEND_SMS_REQUEST_CODE = 1;
    private ArrayList<Cart> carts;
    private TextView btnConfirm;
    private ArrayList<Cart> cartArrayList;
    private double total;
    private RadioButton paytm, cod;
    private String paymentMethod = "", phonenumber = "";
    private ArrayList<MenuModel> menuModelArrayList;
    private TextView itemCount, totalPrice, viewCart, address;
    private String restuarantName, uid, phone = "";
    private ArrayList<String> names;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    private String otp = "", totalPricesms = "";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReciever, smsDeliverReciever;
    private String totalPrice11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        cartArrayList = new ArrayList<>();
        names = new ArrayList<>();
        btnConfirm = findViewById(R.id.btnConfirm);

        totalPrice11 = getIntent().getStringExtra("total-price");
        cartArrayList = getIntent().getParcelableArrayListExtra("order");
        menuModelArrayList = getIntent().getParcelableArrayListExtra("menus");
        restuarantName = getIntent().getStringExtra("restaurantName");
        uid = getIntent().getStringExtra("uid");
        Log.d("restaurant", restuarantName);
        Log.d("uid", uid);
        cod = findViewById(R.id.cod);
        itemCount = findViewById(R.id.itemCount);
        totalPrice = findViewById(R.id.totalPrice);
        viewCart = findViewById(R.id.viewCart);
        address = findViewById(R.id.address);

        address.setText("Connaught Place,New Delhi");
        String ff = getIntent().getStringExtra("address");
        address.setText(ff);

        total = cartArrayList.get(cartArrayList.size() - 1).getTp();
        myAdapter = new CartAdapter(CartActivity.this, cartArrayList, this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        recyclerView.setAdapter(myAdapter);

        itemCount.setText(Integer.toString(cartArrayList.size()));
        HashMap<String, Object> cart = new HashMap<>();
        cart.put("order", cartArrayList);
        cart.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        cart.put("restaurantName", restuarantName);
        cart.put("restaurant-uid", uid);
        totalPricesms = Double.toString(cartArrayList.get(cartArrayList.size() - 1).getTp());
        totalPrice.setText(totalPricesms);
        cart.put("tp", totalPricesms);
        cart.put("status", "Order Placed");
        cart.put("items", Integer.toString(cartArrayList.size()));
        otp = Integer.toString((int) (Math.random() * 9000) + 1000);
        cart.put("otp", Integer.toString((int) (Math.random() * 9000) + 1000));
        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        if (doc.get("s").toString().equals("1") && doc.get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            cart.put("phone", doc.get("m").toString());
                            cart.put("address", doc.get("a").toString());
                            cart.put("lat", doc.get("lt").toString());
                            cart.put("lon", doc.get("lo").toString());
                            phonenumber = doc.get("m").toString();
                        }
                        Log.d("phone", phonenumber);
                    }
                }
            }
        });
        btnConfirm.setOnClickListener(v -> {
//            if (paytm.isChecked()) {
//                paymentMethod = paytm.getText().toString();
//                cart.put("paymentMethod", paymentMethod);
//                FirebaseFirestore.getInstance().collection("Orders").add(cart).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if (task.isSuccessful() && task.getResult() != null) {
//                                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
//                                            if (doc.get("s").toString().equals("1") && doc.get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                                                cart.put("phone", doc.get("m").toString());
//                                                cart.put("address", doc.get("a").toString());
//                                                cart.put("lat", doc.get("lt").toString());
//                                                cart.put("lon", doc.get("lo").toString());
//                                                phonenumber = doc.get("m").toString();
//                                                Log.d("phone", phonenumber);
//                                                Log.d("total ", Integer.toString(cartArrayList.size() - 1));
//
//                                                Toast.makeText(CartActivity.this, "Order Placed Successfully.. Go to your orders to check the status", Toast.LENGTH_SHORT).show();
//                                                if (ContextCompat.checkSelfPermission(CartActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//                                                    ActivityCompat.requestPermissions(CartActivity.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_REQUEST_CODE);
//
//                                                } else {
//                                                    SmsManager smsManager = SmsManager.getDefault();
//                                                    smsManager.sendTextMessage(phonenumber, null, "Your Order has been successfully Placed. Total Amount including delivery charges: 990 and OTP for the transaction" + otp, sentPI, deliveredPI);
//                                                }
//                                                break;
//                                            }
//
//
//                                        }
//                                    }
//                                }
//                            });
//
//                            Intent intent = new Intent(CartActivity.this, HomeActivity.class);
//                            intent.putExtra("amount", Double.toString(cartArrayList.get(cartArrayList.size() - 1).getTp()));
//                            startActivity(intent);
//                        }
//                    }
//                });
//            }
            if (cod.isChecked()) {
                paymentMethod = cod.getText().toString();
                cart.put("paymentMethod", paymentMethod);
                FirebaseFirestore.getInstance().collection("Orders").add(cart).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                            if (doc.get("s").toString().equals("1") && doc.get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                cart.put("phone", doc.get("m").toString());
                                                cart.put("address", doc.get("a").toString());
                                                cart.put("lat", doc.get("lt").toString());
                                                cart.put("lon", doc.get("lo").toString());
                                                phonenumber = doc.get("m").toString();
                                                Log.d("phone", phonenumber);
                                                Log.d("total ", Integer.toString(cartArrayList.size() - 1));

                                                Toast.makeText(CartActivity.this, "Order Placed Successfully.. Go to your orders to check the status", Toast.LENGTH_SHORT).show();
                                                if (ContextCompat.checkSelfPermission(CartActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(CartActivity.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_REQUEST_CODE);

                                                } else {
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(phonenumber, null, "Your Order has been successfully Placed. Total Amount" + totalPricesms + "and OTP " + otp, sentPI, deliveredPI);
                                                }
                                                break;
                                            }

                                        }
                                    }
                                }
                            });

                            Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                            intent.putExtra("amount", Double.toString(cartArrayList.get(cartArrayList.size() - 1).getTp()));
                            startActivity(intent);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please select the Payment Method", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onItemClick(int position) {
        int dish = 0;
        {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsDeliverReciever);
        unregisterReceiver(smsSentReciever);
    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSentReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "Sms SENT", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Sms GENERIC", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "Sms error", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Sms NULL", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Sms RADIO", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };

        smsDeliverReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "Sms SENT", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "Sms Cancelled", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };

        registerReceiver(smsSentReciever, new IntentFilter(SENT));
        registerReceiver(smsDeliverReciever, new IntentFilter(DELIVERED));
    }


    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
