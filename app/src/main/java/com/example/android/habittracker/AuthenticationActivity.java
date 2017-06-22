package com.example.android.habittracker;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created using Firebase tutorials from Udacity and https://github.com/firebase/quickstart-android
 */

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FACEBOOK_LOGIN = "FacebookLogin";


    private FirebaseAuth mAuthentication;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallBackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        FacebookSdk.sdkInitialize(getApplicationContext());

        findViewById(R.id.signout).setOnClickListener(this);

        mAuthentication = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(FACEBOOK_LOGIN, "onAuthStateChanged:signed_in:"+user.getUid());
                }
                else{
                    Log.d(FACEBOOK_LOGIN, "onAuthStateChanged:signed_out" );
                }
                updateUI(user);
            }
        };

        mCallBackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile");
        loginButton.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(FACEBOOK_LOGIN, "facebook:onSuccess:"+loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(FACEBOOK_LOGIN, "facebook:onCancel");
                updateUI(null);

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(FACEBOOK_LOGIN, "facebook:onError", error);
                updateUI(null);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuthentication.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuthentication.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken accessToken){
        Log.d(FACEBOOK_LOGIN, "handleFacebookAccessToken:"+accessToken);

        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuthentication.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(FACEBOOK_LOGIN, "signInWithCredential:onComplete:"+task.isSuccessful());
                if(!task.isSuccessful()){
                    Log.w(FACEBOOK_LOGIN, "signInWithCredential",task.getException());
                    Toast.makeText(AuthenticationActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AuthenticationActivity.this, "You've been signed in. Welcome to Habit Tracker!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void signOut(){
        mAuthentication.signOut();
        LoginManager.getInstance().logOut();
        updateUI(null);
    }


    private void updateUI(FirebaseUser user){
       if(user != null){
            findViewById(R.id.signout).setVisibility(View.GONE);
           findViewById(R.id.login_button).setVisibility(View.VISIBLE);
       }
        else{
           findViewById(R.id.login_button).setVisibility(View.VISIBLE);
           findViewById(R.id.signout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.signout){
            signOut();
        }
    }
}
