package com.assignment.movietime.view.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.assignment.movietime.R;

/**
 * Created by Rashmi on 18/08/17.
 */

public class BaseActivity extends AppCompatActivity {
    public static BaseActivity currentActivity;

    private ProgressDialog progressDialog;
    private boolean isViewDirty;
    private boolean isViewActive;

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;

        isViewActive = true;

        if (isViewDirty) {
            dismissLoader();
            isViewDirty = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentActivity = null;
        isViewActive = false;
    }

    public void showLoader() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.txt_please_wait));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    public void dismissLoader() {
        if (isViewActive && progressDialog.isShowing())
            progressDialog.dismiss();
        else
            isViewDirty = true;
    }
}
