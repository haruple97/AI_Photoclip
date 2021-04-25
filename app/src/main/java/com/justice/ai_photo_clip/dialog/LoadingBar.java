package com.justice.ai_photo_clip.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;



import butterknife.BindView;
import butterknife.ButterKnife;
import com.justice.ai_photo_clip.R;

public class LoadingBar {
    @BindView(R.id.loadingbar_progressbar) ProgressBar loadingBar;
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View inflatedView;



    public LoadingBar(Context context) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
        this.builder.setCancelable(false);
        this.inflatedView = LayoutInflater.from(context).inflate(R.layout.loadingbar, null);
        ButterKnife.bind(this, inflatedView);
    }

    public void show(){
        this.dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setView(inflatedView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if(context != null && !((Activity)context).isFinishing()){
            loadingBar.animate();
            dialog.show();
        }
    }

    public void hide(){
        dialog.dismiss();
    }


}
