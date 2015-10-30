package com.tipstat.tipstatchallenge.utils;

import com.tipstat.tipstatchallenge.R;
import com.tipstat.tipstatchallenge.models.Member;

import java.util.Comparator;

/**
 * Created by mithun on 24/10/15.
 */
public class Utils {

    public static int getFilterText(Member.Ethnicity ethnicity) {
        switch (ethnicity) {
            case ASIAN:
                return R.string.asian;
            case AFRICAN_AMERICANS:
                return R.string.african_americans;
            case ASIAN_AMERICANS:
                return R.string.asian_americans;
            case EUROPEAN:
                return R.string.european;
            case BRITISH:
                return R.string.british;
            case JEWISH:
                return R.string.jewish;
            case LATINO:
                return R.string.latino;
            case NATIVE_AMERICAN:
                return R.string.native_american;
            case ARABIC:
                return R.string.arabic;
            default:
                return R.string.asian;
        }
    }
}
