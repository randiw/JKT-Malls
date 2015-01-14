package com.media2359.jktmalls;

import android.app.Application;
import android.graphics.Typeface;

import com.github.johnkil.print.PrintConfig;
import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;

/**
 * Created by randiwaranugraha on 12/10/14.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private static AppController instance;

    private TypefaceCollection roboto;
    private TypefaceCollection robotoCondensed;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        PrintConfig.initDefault(getAssets(), "fonts/material-icon-font.ttf");

        initRobotoTypeface();
        initRobotoCondensedTypeface();
        TypefaceHelper.init(robotoCondensed);
    }

    private void initRobotoTypeface() {
        roboto = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"))
                .set(Typeface.BOLD, Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"))
                .set(Typeface.ITALIC, Typeface.createFromAsset(getAssets(), "fonts/Roboto-Italic.ttf"))
                .set(Typeface.BOLD_ITALIC, Typeface.createFromAsset(getAssets(), "fonts/Roboto-BoldItalic.ttf"))
                .create();
    }

    public TypefaceCollection getRoboto() {
        return roboto;
    }

    private void initRobotoCondensedTypeface() {
        robotoCondensed = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Regular.ttf"))
                .set(Typeface.BOLD, Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Bold.ttf"))
                .set(Typeface.ITALIC, Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Italic.ttf"))
                .set(Typeface.BOLD_ITALIC, Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-BoldItalic.ttf"))
                .create();

    }

    public TypefaceCollection getRobotoCondensed() {
        return robotoCondensed;
    }

    public static AppController getInstance() {
        return instance;
    }
}