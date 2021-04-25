package com.justice.ai_photo_clip.utils;

import android.app.Activity;

import com.justice.ai_photo_clip.dialog.LoadingBar;

public class LoadingUtil {
    private static LoadingBar lb;

    public static void showLoadingBar(Activity activity) {
        lb = new LoadingBar(activity);
        lb.show();
    }

    public static void hideLoadingBar() {
        if (lb != null) {
            lb.hide();
        }
    }
}
