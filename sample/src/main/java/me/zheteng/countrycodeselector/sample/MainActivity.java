package me.zheteng.countrycodeselector.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import me.zheteng.countrycodeselector.PhoneInputView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PhoneInputView view = (PhoneInputView) findViewById(R.id.phone_input);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.dialog) {
                    view.setCountrySelectorType(PhoneInputView.SELECTOR_TYPE_DIALOG);
                } else if (checkedId == R.id.activity) {
                    view.setCountrySelectorType(PhoneInputView.SELECTOR_TYPE_ACTIVITY);
                }
            }
        });
    }
}
