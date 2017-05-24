package com.udacity.stockhawk.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by ESIDEM jnr on 5/24/2017.
 */

public final class CurrencyUtil {

    private CurrencyUtil(){}

    public static final DecimalFormat dollarFormatWithPlus;
    public static final DecimalFormat dollarFormat;
    public static final DecimalFormat percentageFormat;

    static {
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }
}
