package me.zheteng.countrycodeselector;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import android.content.Context;

/**
 *
 */
public class CountryListManager {

    private List<Country> mList;
    private static CountryListManager INSTANCE;

    private CountryListManager(InputStream inputStream) {
        mList = parseCountryList(inputStream);
    }

    public static CountryListManager from(Context context) {
        if (INSTANCE == null) {
            synchronized(CountryListManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CountryListManager(context.getResources().openRawResource(R.raw.ccs_countries));
                }
            }
        }
        return INSTANCE;
    }

    public List<Country> parseCountryList(InputStream is) {
        try {
            TsvParserSettings settings = new TsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            // creates a TSV parser
            TsvParser parser = new TsvParser(settings);
            List<String[]> allRows = parser.parseAll(is);
            List<Country> countries = new ArrayList<>();

            for (String[] allRow : allRows) {
                Country country = new Country();
                country.setIsoCode(allRow[0]);
                country.setName(allRow[1]);
                country.setCode(allRow[2]);
                country.setMcc(allRow[3]);
                country.setMnc(allRow[4]);

                String pattern = allRow[7];
                if (pattern != null) {
                    String[] split = pattern.split(";");
                    country.setPhonePatterns(Arrays.asList(split));
                }

                String group = allRow[8];
                if (group != null) {
                    String[] split = group.split(";");
                    country.setPhonePatternGroups(Arrays.asList(split));
                }

                country.setNameInEnglish(allRow[11]);

                countries.add(country);
            }
            return countries;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Country> getList() {
        return mList;
    }

    /**
     * release the list
     */
    public synchronized void release() {
        if (mList != null) {
            mList.clear();
            mList = null;
            INSTANCE = null;
        }
    }
}
