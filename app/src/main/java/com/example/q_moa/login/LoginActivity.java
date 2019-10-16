package com.example.q_moa.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.q_moa.R;
import com.example.q_moa.activity.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Hashtable;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
//ㄴㄹㅇ

    private static final int RC_SIGN_IN = 1000;
    private static final String TAG = "LoginActivity";
    //google
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    //naver
    private OAuthLogin mOAuthLoginModule;
    private OAuthLoginButton naver_login;
    private static String OAUTH_CLIENT_ID = "x2QacVmlmnDk_mVmd_TH";
    private static String OAUTH_CLIENT_SECRET = "wQLoRGeTAE";
    private static String OAUTH_CLIENT_NAME = "네이버 로그인";

    private LoginButton kakao_login;
    private SessionCallback callback;


    Context context = LoginActivity.this;
    View login_view;
    DatabaseReference myRef;
    FirebaseUser user;
    String name, email, userUid;
    Uri user_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        findViewById(R.id.btn_facebook_login).setOnClickListener(this);
        findViewById(R.id.btn_google_login).setOnClickListener(this);
        findViewById(R.id.btn_naver_login).setOnClickListener(this);
        findViewById(R.id.btn_kakao_login).setOnClickListener(this);
        findViewById(R.id.btn_guest_login).setOnClickListener(this);
        login_view = findViewById(R.id.login_activity);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //페북 로그인
        mCallbackManager = CallbackManager.Factory.create();

        naver_login = findViewById(R.id.naver_login);
        setNaver();

        kakao_login = findViewById(R.id.kakao_login);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_google_login:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.btn_facebook_login:
                LoginManager loginManager = LoginManager.getInstance();
                loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile", "user_friends"));
                loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError " + error.toString() + "  " + error.getLocalizedMessage());

                    }
                });
                break;

            case R.id.btn_naver_login:
                naver_login.performClick();
                break;

            case R.id.btn_kakao_login:
                kakao_login.performClick();
                break;

            case R.id.btn_guest_login:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    //구글 로그인
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        } else {
                            //구글 인증 실패
                        }
                    }
                });
    }

    //페이스북 로그인
    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void setNaver() {
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(context, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

        naver_login.setOAuthLoginHandler(mOAuthLoginHandler);
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {

                String accessToken = mOAuthLoginModule.getAccessToken(context);
                String refreshToken = mOAuthLoginModule.getRefreshToken(context);
                long expiresAt = mOAuthLoginModule.getExpiresAt(context);
                String tokenType = mOAuthLoginModule.getTokenType(context);
                Log.d(TAG, "run: accesstoken : " + accessToken);
                new naverProfileTask(accessToken).execute();

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(context).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(context);
                Toast.makeText(context, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }


    };

    protected class naverProfileTask extends AsyncTask<Void, Void, JSONObject> {

        String token;
        naverProfileTask(String token) {
            this.token = token;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            String data = mOAuthLoginModule.requestApi(context, token, "https://openapi.naver.com/v1/nid/me");
            JSONObject result = null;
            try {
                result = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            String name = null;
            String email = null;
            String photo = null;
            try {
                name = result.getJSONObject("response").getString("name");
                email = result.getJSONObject("response").getString("email");
                photo = result.getJSONObject("response").getString("profile_image");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "doInBackground: " + "/ name : " + name + "/ email : " + email + "/ photo : " + photo);

            showSnackbar(name, "네이버 로그인");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }

    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        requestMe();
    }

    private void requestMe() {

        UserManagement.requestMe(new MeResponseCallback() {

            @Override

            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onSuccess(UserProfile userProfile) {

                String name = userProfile.getNickname();
                String photo = userProfile.getProfileImagePath();

                Log.d(TAG, "onSuccess: " + " name : " + name + "  photo : " + photo);
                showSnackbar(name,"카카오 로그인");
            }

            @Override
            public void onNotSignedUp() {

            }

        });

    }

    public void showSnackbar(String name, String message){
        Snackbar.make(login_view, name+"님, "+message+"은 아직 준비 중입니다.", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setActionTextColor(Color.RED).show();
    }


    //파베 유저 정보 업로드
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            userUid = user.getUid();
            name = user.getDisplayName();
            user_photo = user.getPhotoUrl();
            email = user.getEmail();

            if (email == null) {
                email = "페이스북 로그인입니다.";
            }

            Hashtable<String, String> profile = new Hashtable<String, String>();
            profile.put("name", name);
            profile.put("email", email);
            profile.put("photo", String.valueOf(user_photo));
            myRef.child(user.getUid()).setValue(profile);

        } else {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
