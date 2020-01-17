package com.dreamstark.fingerprintauthentication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

public class FingerPrintAuthDialog {

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private FingerPrintAuth.OnFingerAuthListener onFingerAuthListener;

    private AppCompatImageView imageView;
    private TextView textView;

    private FingerPrintAuth fingerAuth;

    private int successDelay = 1000;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public FingerPrintAuthDialog(Activity activity) {
        builder = new AlertDialog.Builder(activity, R.style.FingerAuthDialogStyle);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (fingerAuth!=null)
                    fingerAuth.cancelSignal();
            }
        });

        View dialogView = activity.getLayoutInflater().inflate(R.layout.fingerauth_dialog_content, null);
        imageView = (AppCompatImageView) dialogView.findViewById(R.id.fingerauth_dialog_icon);
        textView = (TextView) dialogView.findViewById(R.id.fingerauth_dialog_status);
        builder.setView(dialogView);
        init(activity);
    }

    public FingerPrintAuthDialog setTitle(CharSequence title) {
        builder.setTitle(title);
        return this;
    }

    public FingerPrintAuthDialog setCancelable(boolean cancelable) {
        builder.setCancelable(cancelable);
        return this;
    }

    public FingerPrintAuthDialog setPositiveButton(CharSequence text, DialogInterface.OnClickListener onClickListener) {
        builder.setPositiveButton(text, onClickListener);
        return this;
    }

    public FingerPrintAuthDialog setPositiveButton(@StringRes int text, DialogInterface.OnClickListener onClickListener) {
        builder.setPositiveButton(text, onClickListener);
        return this;
    }

    public FingerPrintAuthDialog setNegativeButton(CharSequence text, DialogInterface.OnClickListener onClickListener) {
        builder.setNegativeButton(text, onClickListener);
        return this;
    }

    public FingerPrintAuthDialog setNegativeButton(@StringRes int text, DialogInterface.OnClickListener onClickListener) {
        builder.setNegativeButton(text, onClickListener);
        return this;
    }

    public FingerPrintAuthDialog setMaxFailedCount(int maxFailedCount) {
        if (fingerAuth!=null)
            fingerAuth.setMaxFailedCount(maxFailedCount);
        return this;
    }

    public FingerPrintAuthDialog setSuccessDelay(int successDelayMillis) {
        successDelay = successDelayMillis;
        return this;
    }

    public FingerPrintAuthDialog setOnFingerAuthListener(FingerPrintAuth.OnFingerAuthListener onFingerAuthListener) {
        this.onFingerAuthListener = onFingerAuthListener;
        return this;
    }

    public void show() {
        if (alertDialog == null)
            alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismiss() {
        if (alertDialog!=null)
            alertDialog.dismiss();
    }

    private void init(final Context context) {
        fingerAuth = new FingerPrintAuth(context);
        fingerAuth.setOnFingerAuthListener(new FingerPrintAuth.OnFingerAuthListener() {
            @Override
            public void onFailure() {
                imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_fingerprint_error, null));
                textView.setText(context.getResources().getString(R.string.fingerauth_dialog_not_recognized));
                textView.setTextColor(context.getResources().getColor(R.color.fingerauth_dialog_color_error));
                onFingerAuthListener.onFailure();
            }

            @Override
            public void onError() {
                onFingerAuthListener.onError();
                dismiss();
            }

            @Override
            public void onSuccess() {
                imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_fingerprint_success, null));
                textView.setText(context.getResources().getString(R.string.fingerauth_dialog_success));
                textView.setTextColor(context.getResources().getColor(R.color.fingerauth_dialog_color_accent));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onFingerAuthListener.onSuccess();
                        dismiss();
                    }
                }, successDelay);
            }
        });
    }

}
