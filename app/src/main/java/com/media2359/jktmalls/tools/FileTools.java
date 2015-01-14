package com.media2359.jktmalls.tools;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by randiwaranugraha on 12/10/14.
 */
public class FileTools {

    public static final String TAG = FileTools.class.getSimpleName();

    public static String loadStringfromAsset(Context context, String path) {
        String str = null;

        try {
            InputStream inputStream = context.getAssets().open(path);

            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            str = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return str;
    }

}