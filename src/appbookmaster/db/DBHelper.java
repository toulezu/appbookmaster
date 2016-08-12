package appbookmaster.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import appbookmaster.common.Constant;

public class DBHelper {
	private final String DATABASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constant.DATABASE_PATH_NAME;
	private final Activity activity;
	private final String DATABASE_FILENAME;

	public DBHelper(Context context) {
		DATABASE_FILENAME = Constant.DATABASE_FILENAME;
		activity = (Activity) context;
	}

	public SQLiteDatabase openDatabase() {
		File file = new File(DATABASE_PATH + File.separator + DATABASE_FILENAME);
		byte[] buffer = new byte[1024 * 1024 * 2];
		int count = 0;
		InputStream fis = null;
		FileOutputStream fos = null;
		try {
			if (file.exists()) {
				return SQLiteDatabase.openOrCreateDatabase(file, null);
			} else {
				file.getParentFile().mkdirs();
				AssetManager am = activity.getAssets();
				String[] files = am.list(Constant.DATABASE_DBS);
				fos = new FileOutputStream(file);
				for (String f : files) {
					fis = am.open(Constant.DATABASE_DBS + File.separator + f);
					while ((count = fis.read(buffer)) > 0) {
						fos.write(buffer, 0, count);
					}
				}
				fos.flush();
				return SQLiteDatabase.openOrCreateDatabase(file, null);
			}
		} catch (Exception e) {
			Log.e(DBHelper.class.getName(), "get db occurrence error");
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}