package com.media2359.jktmalls.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.github.johnkil.print.PrintView;
import com.media2359.jktmalls.R;
import com.media2359.jktmalls.repository.MallsRepository;
import com.media2359.jktmalls.repository.TenantsRepository;
import com.media2359.jktmalls.repository.item.Mall;
import com.media2359.jktmalls.repository.item.Tenant;
import com.media2359.jktmalls.tools.DeviceTools;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by randiwaranugraha on 12/11/14.
 */
public class TenantDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = TenantDetailActivity.class.getSimpleName();

    private TextView title;
    private MaterialMenuView materialMenu;

    @InjectView(R.id.name) TextView name;
    @InjectView(R.id.category) TextView category;
    @InjectView(R.id.floor) TextView floor;
    @InjectView(R.id.unit) TextView unit;
    @InjectView(R.id.phone) TextView phone;
    @InjectView(R.id.phone_icon) PrintView phoneIcon;

    private Tenant tenant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_tenant_detail);

        int tenant_id = getIntent().getIntExtra("tenant_id", 0);
        TenantsRepository tenantsRepo = new TenantsRepository(getApplicationContext());
        tenant = tenantsRepo.find(tenant_id);

        MallsRepository mallsRepository = new MallsRepository(getApplicationContext());
        Mall mall = mallsRepository.find(tenant.getMallId());

        title.setText("Tenant at " + mall.getName());

        name.setText(tenant.getName());
        category.setText(tenant.getCategory());
        floor.setText("#" + tenant.getFloor());
        unit.setText(tenant.getUnit());
        phone.setText(tenant.getPhone());
    }

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.actionbar_base, null);

        title = ButterKnife.findById(view, R.id.title);

        materialMenu = ButterKnife.findById(view, R.id.menu);
        materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
        materialMenu.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                onBackPressed();
                break;
        }
    }

    @OnClick({R.id.phone, R.id.phone_icon})
    public void makeCall() {
        DeviceTools.makeCall(tenant.getPhone(), this);
    }
}