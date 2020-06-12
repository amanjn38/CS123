package in.dete.oops;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaytmActivity extends AppCompatActivity {

    private Button btncontinue;
    private ProgressDialog progressDialog;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm);

        btncontinue = findViewById(R.id.continuebtn);

        btncontinue.setOnClickListener(v -> {

        });

        amount =getIntent().getStringExtra("amount");
        progressDialog = new ProgressDialog(this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JsonObject param = new JsonObject();
        param.addProperty("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        param.addProperty("amt", amount);
        //todo remove this
        param.addProperty("testing", true);
        Call<JsonObject> call = apiInterface.genCheckSum(param);
        progressDialog.setMessage("Fetching Bill");
        progressDialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    progressDialog.dismiss();
                    HashMap apiResponse = new Gson().fromJson(response.body(), HashMap.class);
                    PaytmPGService Service = PaytmPGService.getStagingService();
                    final PaytmOrder payorder = new PaytmOrder(apiResponse);
                    Service.initialize(payorder, null);
                    Service.startPaymentTransaction(PaytmActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                        public void someUIErrorOccurred(String inErrorMessage) {
                            Toast.makeText(PaytmActivity.this, inErrorMessage, Toast.LENGTH_SHORT).show();
                        }

                        public void onTransactionResponse(Bundle inResponse) {
                            String status = inResponse.getString("STATUS");
                            if (status != null && status.equals("TXN_SUCCESS")) {
                                Toast.makeText(PaytmActivity.this, "Payment Complete", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(PaytmActivity.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                        }

                        public void networkNotAvailable() {
                            Toast.makeText(PaytmActivity.this, "Network not available", Toast.LENGTH_SHORT).show();
                        }

                        public void clientAuthenticationFailed(String inErrorMessage) {
                            Toast.makeText(PaytmActivity.this, inErrorMessage, Toast.LENGTH_SHORT).show();
                        }

                        public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                            Toast.makeText(PaytmActivity.this, inErrorMessage, Toast.LENGTH_SHORT).show();
                        }

                        public void onBackPressedCancelTransaction() {
                            Toast.makeText(PaytmActivity.this, "Transaction Canceled", Toast.LENGTH_SHORT).show();
                        }

                        public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                            Toast.makeText(PaytmActivity.this, inErrorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}
