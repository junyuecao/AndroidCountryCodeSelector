package me.zheteng.countrycodeselector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Select the country
 */
public class CountryCodeSelectorActivity extends AppCompatActivity {
    /**
     * Receive country select result
     */
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selector);

        setTitle(R.string.choose_a_country);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, CountryCodeSelectorFragment.newInstance(getIntent()))
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        IntentFilter filter = new IntentFilter(PhoneInputView.ACTION_SEND_RESULT);
        registerReceiver(mResultReceiver, filter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mResultReceiver);
    }
}
