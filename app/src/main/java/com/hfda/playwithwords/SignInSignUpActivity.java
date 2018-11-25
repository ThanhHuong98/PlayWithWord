package com.hfda.playwithwords;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class SignInSignUpActivity extends AppCompatActivity implements fromFragToContainer {

    private int out=0;
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;
    private CallbackManager callbackManager;
    ProgressDialog mDialog;
    String email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragmentMain = new MainFragment();
        FragmentTransaction fs =getSupportFragmentManager().beginTransaction();
        fs.replace(R.id.holder,fragmentMain);
        fs.addToBackStack("MAIN");
        fs.commit();
        setContentView(R.layout.activity_sign_in_sign_up);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Log.d("Success", "Login");

                    }

                    @Override
                    public void onCancel() {
                        //Toast.makeText(AuthorizationActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        //Toast.makeText(AuthorizationActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void getData(JSONObject object) {
        try {
            email = object.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void Action(String action)
    {
        if(action.equals("SIGN IN"))
        {
            Fragment fragmentSignIn = new SignInFragment();
            replaceFragment(fragmentSignIn,"SIGN IN");
        }
        else if(action.equals("SIGN UP"))
        {
            Fragment fragmentSignUp = new SignUpFragment();
            replaceFragment(fragmentSignUp,"SIGN UP");
        }
        else if(action.equals("GOOGLE")){
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else if(action.equals("FB")) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
        }
        out=0;
    }
    public void replaceFragment(Fragment fragment, String tag) {
        //Get current fragment placed in container
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.holder);
        //Prevent adding same fragment on top
        if (currentFragment.getClass() == fragment.getClass()) {
            return;
        }
        //If fragment is already on stack, we can pop back stack to prevent stack infinite growth
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            getSupportFragmentManager().popBackStack(tag, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
        }
        //Otherwise, just replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.holder, fragment, tag)
                .commit();
    }
    @Override
    public void onBackPressed()
    {
        int fragmentsInStack = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmentsInStack > 1)
        { // If we have more than one fragment, pop back stack
            getSupportFragmentManager().popBackStack();
        }
        else if (fragmentsInStack == 1)
        { // Finish activity, if only one fragment left, to prevent
            //leaving empty screen
            Context context = getApplicationContext();
            CharSequence text = "Press back again to exit!";
            out++;
            int duration = Toast.LENGTH_SHORT;
            if(out<2)
            {
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            if(out==2)
                finish();
        }
        else
            super.onBackPressed();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this,"Google sign in failed",Toast.LENGTH_SHORT).show();

                // ...
            }

        }

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        Toast.makeText(this,"firebaseAuthWithGoogle:",Toast.LENGTH_SHORT).show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(),"signInWithCredential:success",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getApplicationContext(),"signInWithCredential:failure",Toast.LENGTH_SHORT).show();
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //String text = account.getEmail();

            Toast.makeText(getApplicationContext(),"Gmail...",Toast.LENGTH_LONG).show();
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            /*Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null)*/;
        }
    }

}
