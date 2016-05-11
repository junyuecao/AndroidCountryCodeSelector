package me.zheteng.countrycodeselector;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * This layout contains a country selector, a country code EditText and a phone number EditText
 */
public class PhoneInputView extends FrameLayout {
    public static final String ACTION_SEND_RESULT = BuildConfig.APPLICATION_ID + ".action.SendResult";
    public static final String EXTRA_COUNTRY = BuildConfig.APPLICATION_ID + ".extra.Country";
    public static final String EXTRA_THEME_COLOR = BuildConfig.APPLICATION_ID + ".extra.ThemeColor";
    public static final String EXTRA_SELECTOR_TYPE = BuildConfig.APPLICATION_ID + ".extra.Type";
    public static final int SELECTOR_TYPE_ACTIVITY = 0;
    public static final int SELECTOR_TYPE_DIALOG = 1;
    private int mThemeColor;
    private int mCountrySelectorType;

    private EditText mPhoneNumber;

    ;
    private EditText mCountryCode;
    private Context mContext;
    private TextView mCountryName;
    private final OnClickListener mOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (v == mCountryName) {
                onCountryNameClicked();
            }
        }
    };
    /**
     * Receive country select result
     */
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Country country = intent.getParcelableExtra(EXTRA_COUNTRY);
            mCountryName.setText(country.getName());
            mCountryCode.setText(country.getCode());

            mPhoneNumber.requestFocus();

        }
    };
    /**
     * Callback to watch the text field for empty/non-empty
     */
    private TextWatcher mTextWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int before, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int after) {
            if (s.length() == 0) {
                mCountryName.setText(R.string.ccs_choose_a_country);
            } else {
                Country country = getCountryByCode(s.toString());

                if (country != null) {
                    mCountryName.setText(country.getName());
                    mPhoneNumber.requestFocus();
                } else {
                    mCountryName.setText(R.string.ccs_invalid_country_code);
                }
            }
        }

        public void afterTextChanged(Editable s) {

        }
    };

    public PhoneInputView(Context context) {
        this(context, null);
    }

    public PhoneInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhoneInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ccs_PhoneInputView, defStyleAttr, 0);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int layoutResId = a.getResourceId(R.styleable.ccs_PhoneInputView_ccs_layout, R.layout.ccs_phone_input_view);
        inflater.inflate(layoutResId, this, true);

        mCountryName = (TextView) findViewById(R.id.select_country);
        mCountryCode = (EditText) findViewById(R.id.edit_country_code);
        mPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);

        // theme color
        mThemeColor = a.getColor(R.styleable.ccs_PhoneInputView_ccs_theme_color,
                ContextCompat.getColor(context, R.color.ccs_default_color));
        mCountryName.getBackground().setColorFilter(mThemeColor, PorterDuff.Mode.SRC_ATOP);
        Drawable[] drawables = mCountryName.getCompoundDrawables();
        for (Drawable drawable : drawables) {
            if (drawable != null) {
                drawable.setColorFilter(mThemeColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
        mPhoneNumber.getBackground().setColorFilter(mThemeColor, PorterDuff.Mode.SRC_ATOP);
        mCountryCode.getBackground().setColorFilter(mThemeColor, PorterDuff.Mode.SRC_ATOP);

        // country selector type
        mCountrySelectorType = a.getInt(R.styleable.ccs_PhoneInputView_ccs_country_selector_type, SELECTOR_TYPE_ACTIVITY);

        // add listeners
        mCountryName.setOnClickListener(mOnClickListener);
        mCountryCode.addTextChangedListener(mTextWatcher);

        mCountryCode.setText("1");
        // recycle
        a.recycle();
    }

    /**
     * Get the phone number field text value
     *
     * @return the String in phone number field
     */
    public String getPhoneNumber() {
        return mPhoneNumber.getText().toString();
    }

    /**
     * Get the country code field text value
     *
     * @return the String in country code field
     */
    public String getCountryCode() {
        return mCountryCode.getText().toString();
    }

    /**
     * Set country code. from 1 - 999
     *
     * @param code country code
     */
    public void setCountryCode(@IntRange(from = 1, to = 999) int code) {
        mCountryCode.setText("" + code);
    }

    /**
     * Setter for theme color
     *
     * @param color theme color
     */
    public void setThemeColor(@ColorInt int color) {
        mThemeColor = color;
    }

    /**
     * Getter for theme color
     *
     * @return theme color
     */
    @ColorInt
    public int getThemeColor() {
        return mThemeColor;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        IntentFilter filter = new IntentFilter(ACTION_SEND_RESULT);
        mContext.registerReceiver(mResultReceiver, filter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mContext.unregisterReceiver(mResultReceiver);
    }

    private Country getCountryByCode(String s) {
        List<Country> list = CountryListManager.from(getContext()).getList();
        if (list == null) {
            return null;
        }
        List<Country> tmp = new ArrayList<>();
        for (Country country : list) {
            // There are many country use the same code, some of them has no Regexp patterns
            if (country.getCode().equals(s)) {
                tmp.add(country);
            }
        }
        if (tmp.size() == 1) {
            return tmp.get(0);
        } else if (tmp.size() == 0) {
            return null;
        } else {
            for (Country country : tmp) {
                if (country.getPhonePatterns() != null) {
                    return country;
                }
            }
            return tmp.get(0);
        }
    }

    private void onCountryNameClicked() {
        if (mCountrySelectorType == SELECTOR_TYPE_ACTIVITY) {
            Intent intent = new Intent(mContext, CountryCodeSelectorActivity.class);
            intent.putExtra(EXTRA_THEME_COLOR, mThemeColor);
            intent.putExtra(EXTRA_SELECTOR_TYPE, SELECTOR_TYPE_ACTIVITY);
            mContext.startActivity(intent);

        } else if (mCountrySelectorType == SELECTOR_TYPE_DIALOG) {
            if (mContext instanceof FragmentActivity) {
                FragmentActivity context = (FragmentActivity) mContext;

                Intent intent = new Intent();
                intent.putExtra(EXTRA_THEME_COLOR, mThemeColor);
                intent.putExtra(EXTRA_SELECTOR_TYPE, SELECTOR_TYPE_DIALOG);
                final CountryCodeSelectorFragment picker = CountryCodeSelectorFragment.newInstance(intent);
                picker.show(context.getSupportFragmentManager(), "COUNTRY_CODE_PICKER");

                picker.setOnCountrySelectListener(new CountryCodeSelectorFragment.OnCountrySelectListener() {
                    @Override
                    public void onCountrySelect(Country country) {
                        picker.dismiss();
                    }
                });
            } else {
                throw new IllegalStateException("Activity should be instance of FragmentActivity");
            }

        }
    }

    /**
     * Setter for country selector type
     *
     * @param selectorType can be {@link #SELECTOR_TYPE_ACTIVITY} or {@link #SELECTOR_TYPE_DIALOG}
     */
    public void setCountrySelectorType(@SelectorType int selectorType) {
        mCountrySelectorType = selectorType;
    }

    /**
     * Getter for country selector type
     *
     * @return can be {@link #SELECTOR_TYPE_ACTIVITY} or {@link #SELECTOR_TYPE_DIALOG}
     */
    public int getCountrySelectorType() {
        return mCountrySelectorType;
    }

    @IntDef({SELECTOR_TYPE_ACTIVITY, SELECTOR_TYPE_DIALOG})
    public @interface SelectorType {
    }
}
