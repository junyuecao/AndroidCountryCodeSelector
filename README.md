AndroidCountryCodeSelector
===============

This is a simple library which makes it easier to pick a country code when input phone numbers. The country data is the same as Whatsapp.

## Screenshots



## Usage

1. PhoneInputView

-----
```xml
<me.zheteng.countrycodeselector.PhoneInputView
            app:ccs_theme_color="#14b4c2"
            app:ccs_country_selector_type="dialog"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

    </me.zheteng.countrycodeselector.PhoneInputView>
```


2. `CountryCodeSelectorFragment` as a fragment

```Java
getSupportFragmentManager().beginTransaction()
                .add(R.id.container, CountryCodeSelectorFragment.newInstance(getIntent()))
                .commit();
```


3. `CountryCodeSelectorFragment` as a dialog

```
final CountryCodeSelectorFragment picker = CountryCodeSelectorFragment.newInstance(intent);
picker.show(context.getSupportFragmentManager(), "COUNTRY_CODE_PICKER");

picker.setOnCountrySelectListener(new CountryCodeSelectorFragment.OnCountrySelectListener() {
    @Override
    public void onCountrySelect(Country country) {
        picker.dismiss();
    }
});
```


License
-------

    Copyright 2014 - 2015 Junyue Cao

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
