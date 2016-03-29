package com.helukable.quickwork.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 用于对DefaultSharedPreferences进行数据操作的类
 */
public class ShareUtil {

	public static void save(Context context, String key, boolean value) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void save(Context context, String key, float value) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static void save(Context context, String key, int value) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void save(Context context, String key, long value) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void save(Context context, String key, String value) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static boolean read(Context context, String key, boolean defValue) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(key, defValue);
	}

	public static float read(Context context, String key, float defValue) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getFloat(key, defValue);
	}

	public static int read(Context context, String key, int defValue) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getInt(key, defValue);
	}

	public static long read(Context context, String key, long defValue) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getLong(key, defValue);
	}
	
	public static String read(Context context, String key, String defValue) {
		if (context == null) {// u盟会有Nullpointer
			return "";
		}
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(key, defValue);
	}

	public static String readFile(Context context, String key, String defValue) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = getFile(key);
			if (file.exists()) {
				try {
					BufferedReader mIn = new BufferedReader(
							new FileReader(file));
					String value = "";
					String str = "";
					while ((str = mIn.readLine()) != null) {
						value += str;
					}
					if (mIn != null) {
						mIn.close();
					}
					return value;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return defValue;
	}

	public static void saveFile(Context context, String key, String value) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File savefile = getFile(key);
			savefile.getParentFile().mkdirs();
			if (!savefile.exists()) {
				try {
					savefile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileOutputStream mFos;
			try {
				mFos = new FileOutputStream(savefile);
				mFos.write(value.getBytes());
				if (mFos != null) {
					mFos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static File getFile(String key) {
		String file = Environment.getExternalStorageDirectory() + "/helukable/quickwork/"
				+ key;
		return new File(file);
	}

	public static void clear(Context context, String key) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.remove(key);
		editor.commit();
	}
	
	public static void clearCookie(Context context){
		CookieSyncManager.createInstance(context);
		CookieManager.getInstance().removeAllCookie();
	}

	public static void clearAll(Context context) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.clear();
		editor.commit();
	}
}
