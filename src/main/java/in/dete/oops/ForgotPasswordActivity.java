package in.dete.oops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnForgotPassword;
    EditText etForgotPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_activityt);

        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        etForgotPassword = findViewById(R.id.etForgotPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = etForgotPassword.getText().toString().trim();
                if(TextUtils.isEmpty(userEmail))
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter the email address",Toast.LENGTH_LONG).show();
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                            }
                            else
                            {
                                String messaage = task.getException().toString();
                                Toast.makeText(ForgotPasswordActivity.this, "Error occured" + messaage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}
