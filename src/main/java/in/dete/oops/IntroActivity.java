package in.dete.oops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.Login;
import com.google.android.material.tabs.TabLayout;

public class IntroActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private ViewPager viewPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private TabLayout tabLayout;
    private Button btnLoginon, btnRegister;
    private TextView btnSkip;
    private Animation btnAnimate;
    private static int SCREENS = 3;
    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (restorePrefData()) {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_intro);

        tabLayout = findViewById(R.id.tabLayout);
        btnSkip = findViewById(R.id.btnSkip);
        btnLoginon = findViewById(R.id.btnLoginon);
        btnRegister = findViewById(R.id.btnRegister);

        gestureDetector = new GestureDetector(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLoginon.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        /*btnSignupon.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);
            startActivity(intent);
        });*/
        btnAnimate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        viewPager = findViewById(R.id.viewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this);
        viewPager.setAdapter(introViewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        btnSkip.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == SCREENS - 1) {
                    btnSkip.setVisibility(View.INVISIBLE);
                    btnLoginon.setVisibility(View.VISIBLE);
                    btnRegister.setVisibility(View.VISIBLE);
                } else {
                    btnSkip.setVisibility(View.VISIBLE);
                    btnLoginon.setVisibility(View.INVISIBLE);
                    btnRegister.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int val = getIntent().getIntExtra("screen", 1);
        viewPager.setCurrentItem(val - 1);
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return pref.getBoolean("isIntroOpened", false);

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        boolean result = false;
        float diffX = moveEvent.getX() - motionEvent.getX();
        if(viewPager.getCurrentItem() == 2){
            if(diffX < -100) {
                onSwipeRight();
                result = true;
            }
        }
        return result;
    }

    private void onSwipeLeft() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void onSwipeRight() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
