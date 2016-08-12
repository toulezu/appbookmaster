package appbookmaster.base;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;

public class AppBookMasterApplication extends Application {
	private List<Activity> activityList = new LinkedList<Activity>();
	private Map<String, Object> dataobj = new LinkedHashMap<String, Object>();
	private static AppBookMasterApplication instance;

	private AppBookMasterApplication() {
	}

	public static AppBookMasterApplication getInstance() {
		if (null == instance) {
			instance = new AppBookMasterApplication();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	public void addObj(String key, Object obj) {
		dataobj.put(key, obj);
	}
	
	public Object getObj(String key) {
		return dataobj.get(key);
	}

	public void exit() {
		dataobj.clear();
		
		for (Activity activity : activityList) {
			activity.finish();
		}
		activityList.clear();
		System.exit(0);
	}
}
