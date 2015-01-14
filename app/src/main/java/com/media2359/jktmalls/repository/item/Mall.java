package com.media2359.jktmalls.repository.item;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.media2359.jktmalls.repository.MallsRepository;
import com.media2359.jktmalls.tools.RepoTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by randiwaranugraha on 12/8/14.
 */
public class Mall {

    public static final String TAG = Mall.class.getSimpleName();

    private int _id;
    private int mallId;
    private String name;
    private String address;
    private String area;
    private LatLng position;
    private String openHour;
    private String closeHour;
    private String phone;

    public Mall() {
    }

    public Mall(Cursor cursor) {
        int id = RepoTools.getInt(cursor, MallsRepository.ID);
        setId(id);

        int mall_id = RepoTools.getInt(cursor, MallsRepository.MALL_ID);
        setMallId(mall_id);

        String name = RepoTools.getString(cursor, MallsRepository.NAME);
        setName(name);

        String area = RepoTools.getString(cursor, MallsRepository.AREA);
        setArea(area);

        String address = RepoTools.getString(cursor, MallsRepository.ADDRESS);
        setAddress(address);

        double latitude = RepoTools.getDouble(cursor, MallsRepository.LATITUDE);
        double longitude = RepoTools.getDouble(cursor, MallsRepository.LONGITUDE);
        setPosition(latitude, longitude);

        String openHour = RepoTools.getString(cursor, MallsRepository.OPEN_HOUR);
        setOpenHour(openHour);

        String closeHour = RepoTools.getString(cursor, MallsRepository.CLOSE_HOUR);
        setCloseHour(closeHour);

        String phone = RepoTools.getString(cursor, MallsRepository.PHONE);
        setPhone(phone);
    }

    public Mall(JSONObject json) {
        try {
            int mall_id = json.getInt("id");
            setMallId(mall_id);

            String name = json.getString("name");
            setName(name);

            String address = json.getString("address");
            setAddress(address);

            String area = json.getString("area");
            setArea(area);

            double latitude = json.getDouble("latitude");
            double longitude = json.getDouble("longitude");
            setPosition(latitude, longitude);

            String open_hour = json.getString("open_hour");
            setOpenHour(open_hour);

            String close_hour = json.getString("close_hour");
            setCloseHour(close_hour);

            String phone = json.getString("phone");
            setPhone(phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getMallId() {
        return mallId;
    }

    public void setMallId(int mallId) {
        this.mallId = mallId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(double latitude, double longitude) {
        setPosition(new LatLng(latitude, longitude));
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(MallsRepository.MALL_ID, getMallId());
        values.put(MallsRepository.NAME, getName());
        values.put(MallsRepository.ADDRESS, getAddress());
        values.put(MallsRepository.AREA, getArea());
        values.put(MallsRepository.LATITUDE, getPosition() != null ? getPosition().latitude : 0);
        values.put(MallsRepository.LONGITUDE, getPosition() != null ? getPosition().longitude : 0);
        values.put(MallsRepository.OPEN_HOUR, getOpenHour());
        values.put(MallsRepository.CLOSE_HOUR, getCloseHour());
        values.put(MallsRepository.PHONE, getPhone());

        return values;
    }
}