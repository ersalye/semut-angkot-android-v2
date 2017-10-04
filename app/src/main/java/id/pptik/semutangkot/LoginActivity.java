package id.pptik.semutangkot;



import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import id.pptik.semutangkot.utils.CustomDrawable;


public class LoginActivity extends AppCompatActivity{


    private Button mFbButton, mGoogleButton;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mContext = this;

        mFbButton = findViewById(R.id.facebook_button);
        mGoogleButton = findViewById(R.id.google_button);

        Drawable fbIcon = CustomDrawable.fontAwesome(mContext,
                FontAwesome.Icon.faw_facebook, 24, R.color.facebook_color);
        Drawable googleIcon = CustomDrawable.fontAwesome(mContext,
                FontAwesome.Icon.faw_google_plus, 24, R.color.googleplus_color);

        mFbButton.setCompoundDrawables(fbIcon, null, null, null);
        mGoogleButton.setCompoundDrawables(googleIcon, null, null, null);

    }
}

