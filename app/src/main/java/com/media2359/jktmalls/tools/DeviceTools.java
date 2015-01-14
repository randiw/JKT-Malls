package com.media2359.jktmalls.tools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Created by randiwaranugraha on 1/9/15.
 */
public class DeviceTools {

    public static Intent makeCallIntent(String phone) {
        if (phone == null || phone.length() == 0) {
            return null;
        }

        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phone, "ID");
            if (phoneUtil.isValidNumber(phoneNumber)) {
                phone = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        String numberUri = "tel:" + phone;
        return new Intent(Intent.ACTION_CALL, Uri.parse(numberUri));
    }

    public static void makeCall(String phone, Fragment fragment) {
        Intent callIntent = makeCallIntent(phone);
        if (callIntent == null) {
            return;
        }

        fragment.startActivity(callIntent);
    }

    public static void makeCall(String phone, Activity activity) {
        Intent callIntent = makeCallIntent(phone);
        if (callIntent == null) {
            return;
        }

        activity.startActivity(callIntent);
    }

    public static Intent openDirectionsIntent(double latitude, double longitude) {
        String uri = "http://maps.google.com/maps?";
        uri += "&daddr=" + latitude + "," + longitude;
        return new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    }

    public static void openDirections(double latitude, double longitude, Fragment fragment) {
        fragment.startActivity(openDirectionsIntent(latitude, longitude));
    }

    public static void openDirections(double latitude, double longitude, Activity activity) {
        activity.startActivity(openDirectionsIntent(latitude, longitude));
    }
}