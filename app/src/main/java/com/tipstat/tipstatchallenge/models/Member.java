package com.tipstat.tipstatchallenge.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E860252 on 10/18/2015.
 */
public class Member implements Parcelable {

    public enum Ethnicity {

        ASIAN, INDIAN, AFRICAN_AMERICANS, ASIAN_AMERICANS, EUROPEAN,
        BRITISH,
        JEWISH,
        LATINO,
        NATIVE_AMERICAN,
        ARABIC
    }

    private String id;
    private String dob;
    private String status;
    private Ethnicity ethnicity;
    private long weight;
    private long height;
    private boolean isVeg;
    private boolean drink;
    private String image;

    public Member(String id, String dob, String status, String ethnicity, String weight, String height, boolean isVeg, boolean drink, String image) {
        this.id = id;
        this.dob = dob;
        this.status = status;
        this.ethnicity = findEthnicity(ethnicity);
        this.weight = Long.parseLong(weight);
        this.height = Long.parseLong(height);
        this.isVeg = isVeg;
        this.drink = drink;
        this.image = image;
    }

    private Ethnicity findEthnicity(String ethnicity) {

        int eth = Integer.parseInt(ethnicity);
        switch (eth) {
            case 0:
                return Ethnicity.ASIAN;
            case 1:
                return Ethnicity.INDIAN;
            case 2:
                return Ethnicity.AFRICAN_AMERICANS;
            case 3:
                return Ethnicity.ASIAN_AMERICANS;
            case 4:
                return Ethnicity.EUROPEAN;
            case 5:
                return Ethnicity.BRITISH;
            case 6:
                return Ethnicity.JEWISH;
            case 7:
                return Ethnicity.LATINO;
            case 8:
                return Ethnicity.NATIVE_AMERICAN;
            case 9:
                return Ethnicity.ARABIC;
            default:
                return Ethnicity.ASIAN;

        }
    }

    // getter methods
    public String getImage() {
        return image;
    }

    public boolean isDrink() {
        return drink;
    }

    public boolean isVeg() {
        return isVeg;
    }

    public Ethnicity getEthnicity() {
        return ethnicity;
    }

    public long getHeight() {
        return height;
    }

    public long getWeight() {
        return weight;
    }

    public String getStatus() {
        return status;
    }

    public String getDob() {
        return dob;
    }

    public String getId() {
        return id;
    }


    protected Member(Parcel in) {
        id = in.readString();
        dob = in.readString();
        status = in.readString();
        ethnicity = (Ethnicity) in.readValue(Ethnicity.class.getClassLoader());
        weight = in.readLong();
        height = in.readLong();
        isVeg = in.readByte() != 0x00;
        drink = in.readByte() != 0x00;
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(dob);
        dest.writeString(status);
        dest.writeValue(ethnicity);
        dest.writeLong(weight);
        dest.writeLong(height);
        dest.writeByte((byte) (isVeg ? 0x01 : 0x00));
        dest.writeByte((byte) (drink ? 0x01 : 0x00));
        dest.writeString(image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}