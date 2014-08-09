package com.klinker.android.theme_spotlight.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.klinker.android.theme_spotlight.R;

/**
 * Very simple activity for notifying user about the auth request that we are about to make for Google. Add
 * it to an activity of it's own because we never want to actually authorize anything in the auth activity until
 * showing this popup
 */
public class AboutAuthActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AlertDialog.Builder(this)
                .setTitle(R.string.google_auth)
                .setMessage(R.string.google_auth_summary)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AboutAuthActivity.this, SpotlightActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }
}
