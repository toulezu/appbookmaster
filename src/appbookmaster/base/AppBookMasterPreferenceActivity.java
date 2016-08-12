package appbookmaster.base;

import longghoststory2.main.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.TextView;
import android.widget.Toast;

public class AppBookMasterPreferenceActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	private CheckBoxPreference nightMode;
	private ListPreference textSize;
	private ListPreference contentBgColor;
	private Preference reset;
	private ColorPickerPreference colorpicker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppBookMasterApplication.getInstance().addActivity(this);
		addPreferencesFromResource(R.xml.mypreference);
		nightMode = (CheckBoxPreference) findPreference("nightMode");
		textSize = (ListPreference) findPreference("textSize");
		contentBgColor = (ListPreference) findPreference("contentBgColor");
		reset = findPreference("reset");
		colorpicker = (ColorPickerPreference) findPreference("colorpiker");

		nightMode.setOnPreferenceChangeListener(this);
		textSize.setOnPreferenceChangeListener(this);
		contentBgColor.setOnPreferenceChangeListener(this);
		colorpicker.setOnPreferenceChangeListener(this);
		showSettings();
	}

	private void setTextSizeSummary(String textSizeValue) {
		if (textSizeValue.equals("0")) {
			textSize.setSummary("正常");
		} else if (textSizeValue.equals("1")) {
			textSize.setSummary("小");
		} else if (textSizeValue.equals("2")) {
			textSize.setSummary("中");
		} else if (textSizeValue.equals("3")) {
			textSize.setSummary("大");
		}
	}
	
	private void setContentBgSummary(String contentBgColorValue) {	
		if (contentBgColorValue.equals("0")) {
			contentBgColor.setSummary("默认");
		} else if (contentBgColorValue.equals("1")) {
			contentBgColor.setSummary("深色");
		} else if (contentBgColorValue.equals("2")) {
			contentBgColor.setSummary("蓝色");
		} else if (contentBgColorValue.equals("3")) {
			contentBgColor.setSummary("嫩灰色");
		} else if (contentBgColorValue.equals("4")) {
			contentBgColor.setSummary("淡紫色");
		} else if (contentBgColorValue.equals("5")) {
			contentBgColor.setSummary("玫瑰");
		} else if (contentBgColorValue.equals("6")) {
			contentBgColor.setSummary("暗黄色");
		} else if (contentBgColorValue.equals("7")) {
			contentBgColor.setSummary("芥末");
		} else if (contentBgColorValue.equals("8")) {
			contentBgColor.setSummary("薄荷绿");
		} else if (contentBgColorValue.equals("9")) {
			contentBgColor.setSummary("海泡石");
		} else if (contentBgColorValue.equals("10")) {
			contentBgColor.setSummary("幽暗黄昏");
		} else if (contentBgColorValue.equals("11")) {
			contentBgColor.setSummary("桃花心木色");
		} else if (contentBgColorValue.equals("12")) {
			contentBgColor.setSummary("茄紫色");
		}
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		if (preference == reset) {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle(R.string.setting_reset);
			builder.setMessage(R.string.setting_reset_yes_no);
			builder.setPositiveButton(R.string.alert_sure,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						defaultSetting();
						Toast.makeText(AppBookMasterPreferenceActivity.this, R.string.setting_reset_success, Toast.LENGTH_SHORT).show();
					}
				});
			builder.setNegativeButton(R.string.alert_cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			builder.create().show();
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == textSize) {
			setTextSizeSummary(newValue.toString());
		} else if (preference == contentBgColor) {
			setContentBgSummary(newValue.toString());
		} else if (preference == nightMode) {
			if (Boolean.valueOf(newValue.toString())) {
				nightSetting();
			} else {
				defaultSetting();
			}
		}
		return true;
	}

	private void showSettings() {
        SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE);  
        String textSizeValue = prefs.getString("textSize", "0");
		if (textSizeValue.equals("0")) {
			textSize.setSummary("正常");
		} else if (textSizeValue.equals("1")) {
			textSize.setSummary("小");
		} else if (textSizeValue.equals("2")) {
			textSize.setSummary("中");
		} else if (textSizeValue.equals("3")) {
			textSize.setSummary("大");
		}
		
		String contentBgColorValue = prefs.getString("contentBgColor", "0");
		if (contentBgColorValue.equals("0")) {
			contentBgColor.setSummary("默认");
		} else if (contentBgColorValue.equals("1")) {
			contentBgColor.setSummary("深色");
		} else if (contentBgColorValue.equals("2")) {
			contentBgColor.setSummary("蓝色");
		} else if (contentBgColorValue.equals("3")) {
			contentBgColor.setSummary("嫩灰色");
		} else if (contentBgColorValue.equals("4")) {
			contentBgColor.setSummary("淡紫色");
		} else if (contentBgColorValue.equals("5")) {
			contentBgColor.setSummary("玫瑰");
		} else if (contentBgColorValue.equals("6")) {
			contentBgColor.setSummary("暗黄色");
		} else if (contentBgColorValue.equals("7")) {
			contentBgColor.setSummary("芥末");
		} else if (contentBgColorValue.equals("8")) {
			contentBgColor.setSummary("薄荷绿");
		} else if (contentBgColorValue.equals("9")) {
			contentBgColor.setSummary("海泡石");
		} else if (contentBgColorValue.equals("10")) {
			contentBgColor.setSummary("幽暗黄昏");
		} else if (contentBgColorValue.equals("11")) {
			contentBgColor.setSummary("桃花心木色");
		} else if (contentBgColorValue.equals("12")) {
			contentBgColor.setSummary("茄紫色");
		}
	}
	
	private void defaultSetting() {
		SharedPreferences.Editor editor = nightMode.getEditor();
        editor.putBoolean(nightMode.getKey(), false);
        nightMode.setChecked(false);
        editor.commit();
        
		editor = textSize.getEditor();
        editor.putString(textSize.getKey(), "0");
        textSize.setSummary("正常");
        textSize.setValue("0");
        editor.commit();
        
        editor = contentBgColor.getEditor();
        editor.putString(contentBgColor.getKey(), "0");
        contentBgColor.setSummary("默认");
        contentBgColor.setValue("0");
        editor.commit();
        
        editor = colorpicker.getEditor();
        editor.putInt(colorpicker.getKey(), Color.BLACK);
        ((TextView) findViewById(R.id.summary)).setTextColor(Color.BLACK);
        editor.commit();
	}
	
	private void nightSetting() {
		SharedPreferences.Editor editor = nightMode.getEditor();
        editor.putBoolean(nightMode.getKey(), true);
        nightMode.setChecked(true);
        editor.commit();
        
		editor = textSize.getEditor();
        editor.putString(textSize.getKey(), "0");
        textSize.setSummary("正常");
        textSize.setValue("0");
        editor.commit();
        
        editor = contentBgColor.getEditor();
        editor.putString(contentBgColor.getKey(), "1");
        contentBgColor.setSummary("深色");
        contentBgColor.setValue("1");
        editor.commit();
        
        editor = colorpicker.getEditor();
        editor.putInt(colorpicker.getKey(), 0X802BD5FF);
        ((TextView) findViewById(R.id.summary)).setTextColor(0X802BD5FF);
        editor.commit();
	}
}
