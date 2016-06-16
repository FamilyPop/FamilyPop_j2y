package com.j2y.familypop.activity.lobby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.facebook.FacebookSdk;
import com.j2y.familypop.MainActivity;
import com.j2y.familypop.activity.BaseActivity;
import com.nclab.familypop.R;

import org.andengine.util.base64.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lsh on 2016-05-14.
 */
public class Activity_facebook_connected extends BaseActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialogue_start_client_facebook);

        ((ImageButton)findViewById(R.id.ClientInputNextButton)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.ClientInputCancle)).setOnClickListener(this);

        // 페이스북 초기화.
        FacebookSdk.sdkInitialize(this);

        //keyHash 얻기.

        try {
            String packageName = getApplicationContext().getPackageName();
            PackageInfo info = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for( Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ClientInputNextButton:
                startActivity(new Intent(MainActivity.Instance, Activity_topicGallery.class));
                finish();
                break;
            case R.id.ClientInputCancle:
                startActivity(new Intent(MainActivity.Instance, Activity_input_userName.class));
                finish();
                break;

        }

    }


}
