// Copyright (C) 2018 INTUZ.

// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
// the following conditions:

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
// ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
// THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.phonenumberui.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


public class Utility {
    public static void hideKeyBoardFromView(Activity mActivity) {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = mActivity.getCurrentFocus();
        if (view == null) {
            view = new View(mActivity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity mActivity) {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public static ProgressDialog initProgress(Object mActivity) {
        ProgressDialog mpDialog = null;
        if (mActivity instanceof Activity) {
            mpDialog = new ProgressDialog((Activity) mActivity);
        } else if (mActivity instanceof Context) {
            mpDialog = new ProgressDialog((Context) mActivity);
        }
        mpDialog.setTitle("Please wait...");
        mpDialog.setMessage("Verifying...");
        mpDialog.setCanceledOnTouchOutside(false);

        return mpDialog;
    }

    public static void dialogShow(Activity mActivity, final ProgressDialog mpDialog) {
        if (mpDialog != null && !mpDialog.isShowing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mpDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void dialogDismiss(Activity mActivity, final ProgressDialog mpDialog) {
        if (mpDialog != null && mpDialog.isShowing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mpDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public static void log(String message) {
        if (message != null) {
            Log.e("Phone Auth ", message);
        } else {
            Log.e("Message", "null");
        }
    }


    public static void showToast(final Context ctx, final String message) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
