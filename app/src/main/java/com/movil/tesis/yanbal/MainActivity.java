package com.movil.tesis.yanbal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import com.movil.tesis.yanbal.util.Constants;
import com.movil.tesis.yanbal.util.Preferences;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String consultantIdentification;

    public static final int MAP_LOCATION_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        consultantIdentification = Preferences.getInstance(this).readStringPreference(Constants.CONSULTANT_ID, null);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                SharedPreferences.Editor sharedPreferences = getSharedPreferences(Constants.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE).edit();
                sharedPreferences.putBoolean(Constants.IS_LOGGED, false);
                sharedPreferences.remove(Constants.CONSULTANT_ID);
                sharedPreferences.apply();
                openLoginActivity();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void openLoginActivity() {
        Intent welcomeActivityintent = new Intent(this, WelcomeActivity.class);
        startActivity(welcomeActivityintent);
        finish();
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ClientRegisterFragment(), "Registro de Clientes");
        adapter.addFragment(new OrderFragment(), "Pedidos");
        adapter.addFragment(new ConsolidatedFragment(), "Consolidado");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAP_LOCATION_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                TextView clientLocationLatitudeTextView = (TextView) findViewById(R.id.clientLocationLatitudeTextView);
                TextView clientLocationLongitudeTextView = (TextView) findViewById(R.id.clientLocationLongitudeTextView);
                Location location = data.getParcelableExtra("location");
                clientLocationLatitudeTextView.setText(String.valueOf(location.getLatitude()));
                clientLocationLongitudeTextView.setText(String.valueOf(location.getLongitude()));
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView clientBirthDateTextView = (TextView) findViewById(R.id.clientBirthDateTextView);
        clientBirthDateTextView.setText(year + "/" + (month + 1)  + "/" + dayOfMonth);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public String getConsultantIdentification() {
        return consultantIdentification;
    }
}
