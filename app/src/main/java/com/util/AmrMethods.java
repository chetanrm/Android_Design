package com.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

public class AmrMethods {
	/** A M Remith Methods */

	public static String dateConverter(String dbDate) {// Convert DB date to// dd/MMM/yyyy format.
		String convertedDate;

		String[] date2 = dbDate.split(" ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dates = null;
		try {
			dates = sdf.parse(date2[0]);
		} catch (ParseException e) {

			e.printStackTrace();

		}

		sdf = new SimpleDateFormat("dd-MMM-yyyy");
		convertedDate = sdf.format(dates);

		return convertedDate;

	}

	public static String onlyStringMonth(String dbDate) {// Convert DB date to
		String convertedDate;
		String[] date2 = dbDate.split(" ");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dates = null;
		try {
			dates = sdf.parse(date2[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		sdf = new SimpleDateFormat("MMMM-yyyy");
		convertedDate = sdf.format(dates);

		return convertedDate.trim();

	}

	public String billDateMaker(String convertedDate) { // Convert to May 2014 format.

		String dateSplit[] = convertedDate.split("/");
		String month = dateSplit[1];
		String year = dateSplit[2];

		convertedDate = month + " " + year;

		return convertedDate;

	}

	public String fineCalculator(String amountBeforeDue) { // Fine Calculator

		double amount = Double.parseDouble(amountBeforeDue);
		double fine = amount * 1 / 100;
		double amountWithFine = amount + fine;

		BigDecimal bd = new BigDecimal(amountWithFine);
		float lonVal = bd.floatValue();

		amountBeforeDue = String.format(Locale.US, "%.2f", lonVal);

		return amountBeforeDue;

	}

	public static String currencyConverter(String getAmount) {
		double amount = Double.parseDouble(getAmount);

		getAmount = String.format(Locale.US, "%.2f", amount);

		if (android.os.Build.VERSION.SDK_INT < 11) {
			getAmount = "Rs. " + getAmount;
		} else {
			getAmount = "₹ " + getAmount;
		}

		return getAmount;
	}

	public long dateFormater(String indate) {
		Date date = null;
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		date = sdf.parse(indate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

	public static final long MIN_CLICK_INTERVAL = 1000; // in millis
	public static long lastClickTime = 0;

	public static boolean doubleClick() {
		long currentTime = SystemClock.elapsedRealtime();
		if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
			lastClickTime = currentTime;
			return true;
		}
		return false;
	}

	public static void setDefaults(String key, String value, Context context) {
		SharedPreferences prefs = PreferenceManager .getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getDefaults(String key, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(key, null);
	}

	public static void setLocale(String lang, Context context) {
		Locale myLocale = null;
		myLocale = new Locale(lang);
		Resources res = context.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);

	}

	public static String getDueDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(date));
			c.add(Calendar.DATE, 14); // Adding 14 days
			//System.out.println("sdf.format(c.getTime() : "+sdf.format(c.getTime()));
			return sdf.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static  int getColor(Context context, int id) {
	    final int version = Build.VERSION.SDK_INT;
	    if (version >= 23) {
	        return context.getResources().getColor(id, context.getTheme());
	    } else {
	        return context.getResources().getColor(id);
	    }
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static Drawable getExactDrawable(Context context,int id){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		    return context.getResources().getDrawable(id, context.getTheme());
		} else {
		    return context.getResources().getDrawable(id);
		}
	}
}
