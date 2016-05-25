package com.j2y.familypop.activity.lobby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.j2y.engine.ColumnListView;
import com.j2y.familypop.MainActivity;
import com.j2y.familypop.activity.Activity_clientMain;
import com.j2y.familypop.activity.BaseActivity;
import com.nclab.familypop.R;

import org.andengine.entity.sprite.ButtonSprite;

/**
 * Created by lsh on 2016-05-14.
 */
public class Activity_input_userName extends BaseActivity implements View.OnClickListener
{
    private EditText _user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialogue_start_client_input_id);

        _user_name = (EditText)findViewById(R.id.Text_userName);

        ((ImageButton)findViewById(R.id.ClientInputFaceBookButton)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.ClientInputNextButton)).setOnClickListener(this);


        SharedPreferences prefs = getSharedPreferences("FamilypopClient", MODE_PRIVATE);
        String text_username = prefs.getString("Username", "UserName");
        _user_name.setText(text_username);
    }
    @Override
    public void onDestroy()
    {
        Log.i("[J2Y]", "Activity_talkHistory:onDestroy");

        super.onDestroy();
    }
    @Override
    public void onClick(View v)
    {
        SharedPreferences prefs = getSharedPreferences("FamilypopClient", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        switch (v.getId())
        {
            case R.id.ClientInputNextButton:
                editor.putString("Username", _user_name.getText().toString());
                MainActivity.Instance._userName = _user_name.getText();

                startActivity(new Intent(MainActivity.Instance, Activity_clientMain.class));

                break;
            case R.id.ClientInputFaceBookButton:

                startActivity(new Intent(MainActivity.Instance, Activity_facebook_connected.class));

                break;
        }
    }
}
