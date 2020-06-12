package in.dete.oops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new CountDownTimer(1000,500){
            @Override
            public void onFinish() {
                startActivity(new Intent(SplashScreen.this, IntroActivity.class));
                finish();
            }

            @Override
            public void onTick(long l) {

            }
        }.start();
    }
}
