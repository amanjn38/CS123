package in.dete.oops;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private static final String TAG = "LoginActivity";
    private TextView tvForgotPassword, tvSignUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private int selection;
    private CheckBox cbRemember;
    private CallbackManager mFacebookManager;
    private FirebaseUser currentUser;
    private final static int RC_SIGN_IN = 1;
    private LoginButton ivFacebook;
    SignInButton ivGoogle;
    private GoogleApiClient mGoogleSignInClient;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        ivFacebook = findViewById(R.id.login_button);
        ivGoogle = findViewById(R.id.sign_in_button);
        tvSignUp = findViewById(R.id.tvSignUp);
        mFacebookManager = CallbackManager.Factory.create();

        gestureDetector = new GestureDetector(this);


        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Paper.init(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
            }
        });

        ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(LoginActivity.this, "Connection to google sign in failed", Toast.LENGTH_SHORT).show();

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        FacebookSdk.sdkInitialize(getApplicationContext());

        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivFacebook.setEnabled(false);

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mFacebookManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //Toast.makeText(MainActivity.this,"Login successful",Toast.LENGTH_LONG).show();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "On Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(LoginActivity.this, "On Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if(firebaseUser!=null){
//            UpdateUI();
//        }
//    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//    private void UpdateUI() {
//        Toast.makeText(this, "You are Logged in!!", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            progressDialog.setTitle("Google Sign In");
            progressDialog.setMessage("Logging in using your google account");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Toast.makeText(LoginActivity.this, "Please wait, while we are getting your auth results", Toast.LENGTH_LONG).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Can't get Auth results", Toast.LENGTH_SHORT).show();
            }
        } else {
            mFacebookManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Signed In successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            //intent.putExtra("update", true);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            String message = task.getException().toString();
                           // SendUserToLoginActivity();
                            Toast.makeText(LoginActivity.this, "Not authenticated " + message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        //Toast.makeText(MainActivity.this,"Handle facebook handle token" + token,Toast.LENGTH_LONG).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            ivFacebook.setEnabled(true);
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            // intent.putExtra("update", true);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            // updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Error", task.getException().toString());
                            ivFacebook.setEnabled(true);
                            //updateUI();
                        }

                    }
                });
    }

//    public void completeLogin() {
//        String ph = currentUser.getEmail();
//        Intent intent;
//        if (ph == null) {
//            progressDialog.dismiss();
//            intent = new Intent(LoginActivity.this, RegisterActivity.class);
//            // intent.putExtra("update", true);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } else {
//            String userJson = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("loggedInUser", "");
//            User user = null;
//            try {
//                user = new Gson().fromJson(userJson, User.class);
//            } catch (Exception e) {
//            }
//            if (user == null) {
//                FirebaseFirestore.getInstance().collection("users").whereEqualTo("m", ph).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        progressDialog.dismiss();
//                        if (task.isSuccessful()) {
//                            List<DocumentSnapshot> l = task.getResult().getDocuments();
//                            if (l.size() == 1 && l.get(0).exists()) {
//                                User userData = l.get(0).toObject(User.class);
//                                String json = new Gson().toJson(userData);
//                                getSharedPreferences("MyPrefs", MODE_PRIVATE).edit().putString("loggedInUser", json).apply();
//                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                intent.putExtra("user", userData);
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(LoginActivity.this, "Couldn't load profile, try later", Toast.LENGTH_LONG).show();
//                            }
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            } else {
//                progressDialog.dismiss();
//                intent = new Intent(LoginActivity.this, HomeActivity.class);
//                intent.putExtra("user", user);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        }
//    }

    private void validate(String username, String userPassword) {
        final String[] selection = new String[1];
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        if (cbRemember.isChecked()) {
            Paper.book().write(Prevalent.user_email, username);
            Paper.book().write(Prevalent.user_password, userPassword);
        }
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(username, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                        FirebaseFirestore.getInstance().collection("users").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.getResult().getDocuments().size() != 0) {
                                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                                // if (doc.get("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                //User user = doc.toObject(User.class);
                                                //     Log.d("nam", user.getName());
                                                selection[0] = doc.get("s").toString();
                                                if (Integer.parseInt(selection[0]) == 1) {
                                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                } else {
                                                    startActivity(new Intent(LoginActivity.this, HomeActivityD.class));

                                                }
                                            }
                                            //    }
                                        }
                                    }
                                });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_LONG).show();
                    }

                } else if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException invalidEmail) {
                        Toast.makeText(LoginActivity.this, invalidEmail.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

                    } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, wrongPassword.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

//    private void retrieveData() {
//       User user = Utils.fetchUserInfo(LoginActivity.this);
//       selection = user.getSelection();
//
//    }

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
        if (diffX > 100) {
            onSwipeLeft();
            result = true;
        } else if (diffX < -100) {
            onSwipeRight();
            result = true;
        }
        return result;
    }

    private void onSwipeLeft() {
        onBackPressed();
    }

    private void onSwipeRight() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
