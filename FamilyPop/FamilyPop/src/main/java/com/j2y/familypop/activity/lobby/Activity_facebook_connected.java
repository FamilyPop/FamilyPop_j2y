package com.j2y.familypop.activity.lobby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.j2y.familypop.MainActivity;
import com.j2y.familypop.activity.BaseActivity;
import com.nclab.familypop.R;

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
