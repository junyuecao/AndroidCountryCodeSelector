package me.zheteng.countrycodeselector;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Country contains phone information of a country
 */
public class Country implements Parcelable {
    /**
     * ISO code of this country. eg: US, CN
     */
    private String isoCode;
    /**
     * Country name. This is for display to user.
     */
    private String name;

    /**
     * Country code. also as known as phone country code.
     */
    private String code;

    /**
     * Mobile Country Code，MCC
     * <p/>
     * MCC / MNC see: <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * https://en.wikipedia.org/wiki/Mobile_country_code</a>
     */
    private String mcc;

    /**
     * Mobile Network Code，MNC
     * <p/>
     * MCC / MNC see: <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * https://en.wikipedia.org/wiki/Mobile_country_code</a>
     */
    private String mnc;

    /**
     * Country's English Name;
     */
    private String nameInEnglish;

    /**
     * Reg exp to check if a phone number is legal.
     */
    private List<String> phonePatterns;

    /**
     * Reg exp group that corresponding with {@link #phonePatterns}
     */
    private List<String> phonePatternGroups;

    /**
     * ISO code of this country. eg: US, CN
     */
    public String getIsoCode() {
        return isoCode;
    }

    /**
     * ISO code of this country. eg: US, CN
     */
    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    /**
     * Country name. This is for display to user.
     */
    public String getName() {
        return name;
    }

    /**
     * Country name. This is for display to user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Country code. also as known as phone country code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Country code. also as known as phone country code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Mobile Country Code，MCC
     * <p/>
     * MCC / MNC see: <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * https://en.wikipedia.org/wiki/Mobile_country_code</a>
     */
    public String getMcc() {
        return mcc;
    }

    /**
     * Mobile Country Code，MCC
     * <p/>
     * MCC / MNC see: <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * https://en.wikipedia.org/wiki/Mobile_country_code</a>
     */
    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    /**
     * Mobile Network Code，MNC
     * <p/>
     * MCC / MNC see: <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * https://en.wikipedia.org/wiki/Mobile_country_code</a>
     */
    public String getMnc() {
        return mnc;
    }

    /**
     * Mobile Network Code，MNC
     * <p/>
     * MCC / MNC see: <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * https://en.wikipedia.org/wiki/Mobile_country_code</a>
     */
    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    /**
     * Country's English Name;
     */
    public String getNameInEnglish() {
        return nameInEnglish;
    }

    /**
     * Country's English Name;
     */
    public void setNameInEnglish(String nameInEnglish) {
        this.nameInEnglish = nameInEnglish;
    }

    /**
     * Reg exp to check if a phone number is legal.
     */
    public List<String> getPhonePatterns() {
        return phonePatterns;
    }

    /**
     * Reg exp to check if a phone number is legal.
     */
    public void setPhonePatterns(List<String> phonePatterns) {
        this.phonePatterns = phonePatterns;
    }

    /**
     * Reg exp group that corresponding with {@link #phonePatterns}
     */
    public List<String> getPhonePatternGroups() {
        return phonePatternGroups;
    }

    /**
     * Reg exp group that corresponding with {@link #phonePatterns}
     */
    public void setPhonePatternGroups(List<String> phonePatternGroups) {
        this.phonePatternGroups = phonePatternGroups;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isoCode);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.mcc);
        dest.writeString(this.mnc);
        dest.writeString(this.nameInEnglish);
        dest.writeStringList(this.phonePatterns);
        dest.writeStringList(this.phonePatternGroups);
    }

    public Country() {
    }

    protected Country(Parcel in) {
        this.isoCode = in.readString();
        this.name = in.readString();
        this.code = in.readString();
        this.mcc = in.readString();
        this.mnc = in.readString();
        this.nameInEnglish = in.readString();
        this.phonePatterns = in.createStringArrayList();
        this.phonePatternGroups = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
