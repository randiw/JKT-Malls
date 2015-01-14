package com.media2359.jktmalls.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.media2359.jktmalls.R;
import com.media2359.jktmalls.repository.MallsRepository;
import com.media2359.jktmalls.repository.item.Mall;
import com.media2359.jktmalls.tools.DeviceTools;
import com.squareup.picasso.Picasso;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by randiwaranugraha on 12/15/14.
 */
public class MallInfoFragment extends BaseFragment {

    public static final String TAG = MallInfoFragment.class.getSimpleName();

    @InjectView(R.id.navigation) PrintView navigation;
    @InjectView(R.id.mall_name) TextView mallName;
    @InjectView(R.id.operational_hour) TextView operationalHour;
    @InjectView(R.id.phone) TextView phone;
    @InjectView(R.id.static_map) ImageView staticMap;
    @InjectView(R.id.address) TextView address;
    @InjectView(R.id.area) TextView area;

    private Mall mall;

    public static MallInfoFragment newInstance(int mall_id) {
        MallInfoFragment mallInfoFragment = new MallInfoFragment();

        Bundle data = new Bundle();
        data.putInt("mall_id", mall_id);
        mallInfoFragment.setArguments(data);

        return mallInfoFragment;
    }

    @Override
    protected View setupLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_mall_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle data = getArguments();
        int mallId = data.getInt("mall_id");

        MallsRepository mallsRepo = new MallsRepository(getActivity().getApplicationContext());
        mall = mallsRepo.find(mallId);

        String operational_hour = "Open " + mall.getOpenHour() + " - " + mall.getCloseHour();
        String url = generateStaticUrl(mall.getPosition().latitude, mall.getPosition().longitude);

        Picasso.with(getActivity().getApplicationContext()).load(url).into(staticMap);

        mallName.setText(mall.getName());
        operationalHour.setText(operational_hour);
        phone.setText(mall.getPhone());
        address.setText(mall.getAddress());
        area.setText(mall.getArea());
    }

    @OnClick({R.id.static_map, R.id.navigation})
    public void openDirections() {
        DeviceTools.openDirections(mall.getPosition().latitude, mall.getPosition().longitude, this);
    }

    @OnClick(R.id.phone)
    public void makeCall() {
        DeviceTools.makeCall(mall.getPhone(), this);
    }

    private String generateStaticUrl(double latitude, double longitude) {
        String url = getString(R.string.google_static_map_url);

        String point = latitude + "," + longitude;
        String center = "center=" + point;
        String zoom = "zoom=15";
        String size = "size=700x150";
        String markers = "markers=color:blue%7C" + point;

        url += center + "&" + zoom + "&" + size + "&" + markers;
        return url;
    }
}