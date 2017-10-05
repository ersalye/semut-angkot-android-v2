package id.pptik.semutangkot;



import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.login.widget.LoginButton;
import com.maksim88.easylogin.AccessToken;
import com.maksim88.easylogin.EasyLogin;
import com.maksim88.easylogin.listener.OnLoginCompleteListener;
import com.maksim88.easylogin.networks.FacebookNetwork;
import com.maksim88.easylogin.networks.GooglePlusNetwork;
import com.maksim88.easylogin.networks.SocialNetwork;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.util.Arrays;
import java.util.List;

import id.pptik.semutangkot.utils.CustomDrawable;


public class LoginActivity extends AppCompatActivity implements OnLoginCompleteListener {


    private Button mFbButton;
    private Button mGoogleButton;
    private Context mContext;
    private EasyLogin easyLogin;
    private GooglePlusNetwork gPlusNetwork;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EasyLogin.initialize();
        easyLogin = EasyLogin.getInstance();

        getSupportActionBar().hide();

        mContext = this;

        mFbButton = findViewById(R.id.facebook_button);
        mGoogleButton = findViewById(R.id.google_button);

        Drawable fbIcon = CustomDrawable.fontAwesome(mContext,
                FontAwesome.Icon.faw_facebook, 24, R.color.facebook_color);
        Drawable googleIcon = CustomDrawable.fontAwesome(mContext,
                FontAwesome.Icon.faw_google, 24, R.color.googleplus_color);

        mFbButton.setCompoundDrawables(fbIcon, null, null, null);
        mGoogleButton.setCompoundDrawables(googleIcon, null, null, null);


        List<String> fbScope = Arrays.asList("public_profile", "email");
        easyLogin.addSocialNetwork(new FacebookNetwork(this, fbScope));

        FacebookNetwork facebook = (FacebookNetwork) easyLogin.getSocialNetwork(SocialNetwork.Network.FACEBOOK);
        LoginButton fbLogin = findViewById(R.id.facebook_login_button);
        facebook.requestLogin(fbLogin, this);
        mFbButton.setOnClickListener(view -> fbLogin.performClick());



        easyLogin.addSocialNetwork(new GooglePlusNetwork(this));
        gPlusNetwork = (GooglePlusNetwork) easyLogin.getSocialNetwork(SocialNetwork.Network.GOOGLE_PLUS);
        gPlusNetwork.setListener(this);
        gPlusNetwork.setSignInButton(mGoogleButton);
        mGoogleButton.setOnClickListener(view -> {
            if (!gPlusNetwork.isConnected()) {
                gPlusNetwork.requestLogin(LoginActivity.this);
            }else {
                Log.i(TAG, "Google not ready");
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!gPlusNetwork.isConnected()) {
          //  gPlusNetwork.silentSignIn();
            mGoogleButton.setEnabled(true);
        } else {
            mGoogleButton.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginSuccess(SocialNetwork.Network network) {
        Log.i("MAIN", network.name());
        if (network == SocialNetwork.Network.FACEBOOK) {
            AccessToken token = easyLogin.getSocialNetwork(SocialNetwork.Network.FACEBOOK).getAccessToken();
            Log.d("MAIN", "FACEBOOK Login successful: " + token.getToken() + "|||" + token.getEmail()+" || "+token.getUserId());
        }else if (network == SocialNetwork.Network.GOOGLE_PLUS) {
            AccessToken token = easyLogin.getSocialNetwork(SocialNetwork.Network.GOOGLE_PLUS).getAccessToken();
            Log.d("MAIN", "G+ Login successful: " + token.getToken() + "|||" + token.getEmail());
        }
    }

    @Override
    public void onError(SocialNetwork.Network socialNetwork, String errorMessage) {
        Log.e("MAIN", "ERROR!" + socialNetwork + "|||" + errorMessage);
    }
}

