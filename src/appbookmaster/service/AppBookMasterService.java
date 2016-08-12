package appbookmaster.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import appbookmaster.bean.BookMark;
import appbookmaster.bean.CataLog;
import appbookmaster.bean.Content;
import appbookmaster.bean.Info;
import appbookmaster.bean.LastRead;
import appbookmaster.db.DBHelper;

public class AppBookMasterService {
	private SQLiteDatabase db = null;
	public AppBookMasterService(Context context) {
		DBHelper dbHelper = new DBHelper(context);
		db = dbHelper.openDatabase();
	}
	
	public List<CataLog> getChapterListByPid(Integer pid) {
		List<CataLog> catalogs = new ArrayList<CataLog>();
		Cursor cursor = db.rawQuery("select cat._id, cat._catalog, cat._title, tc._status from tb_catalog cat, tb_content tc where cat._id = tc._chapterid and cat._pid = ?", new String[] { String.valueOf(pid) });
		while (cursor.moveToNext()) {
			catalogs.add(new CataLog(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
		}
		return catalogs;
	}
	
	public List<CataLog> getCatalogList() {
		List<CataLog> catalogs = new ArrayList<CataLog>();
		Cursor cursor = db.rawQuery("select _id, _catalog, _title from tb_catalog where _pid = 0", null);
		while (cursor.moveToNext()) {
			catalogs.add(new CataLog(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
		}
		return catalogs;
	}
	
	public CataLog getCatalogById(Integer id) {
		Cursor cursor = db.rawQuery("select * from tb_catalog where _id = ?", new String[] { String.valueOf(id) });
		while (cursor.moveToNext()) {
			return new CataLog(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
		}
		return null;
	}
	
	public Content getBookContentByChapterid(Integer chapterid) {
		Cursor cursor = db.rawQuery("select * from tb_content where _chapterid = ?",  new String[] { String.valueOf(chapterid) });
		if (cursor.moveToNext()) {
			return new Content(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
		}
		return null;
	}
	
	public String getStatusByChapterid(Integer chapterid) {
		Cursor cursor = db.rawQuery("select _status from tb_content where _chapterid = ?",  new String[] { String.valueOf(chapterid) });
		if (cursor.moveToNext()) {
			return cursor.getString(0);
		}
		return null;
	}
	
	public void updateBookContent(Integer scrollto, String status, Integer id) {
		db.execSQL("update tb_content set _scrollto = ?, _status = ? where _id = ?", new Object[] { scrollto, status, id });
	}
	
	public void insertOrUpdateLastRead(Integer chapterid, Integer scrollto, String status) {
		if (getLastRead() != null) {
			db.execSQL("update tb_lastread set _chapterid = ?, _scrollto = ?, _status = ? where _id = 1", new Object[] { chapterid, scrollto, status });
		} else {
			db.execSQL("insert into tb_lastread (_id, _chapterid, _scrollto, _status) values (1, ?, ?, ?)", new Object[] { chapterid, scrollto, status });
		}
	}
	
	public Integer getChapterCount(Integer id) {
		Cursor cursor = db.rawQuery("select count(1) from tb_catalog where _pid in (?)", new String[] { String.valueOf(id) });
		if (cursor.moveToNext()) {
			return cursor.getInt(0);
		}
		return null;
	}
	
	public LastRead getLastRead() {
		Cursor cursor = db.rawQuery("select * from tb_lastread where _id = 1", null);
		if (cursor.moveToNext()) {
			return new LastRead(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3));
		}
		return null;
	}
	
	public Integer getLastChapterId(Integer curid) {
		Cursor cursor = db.rawQuery("select min(_chapterid) from tb_content", null);
		if (cursor.moveToNext()) {
			if ((curid - cursor.getInt(0)) > 0) {
				return curid - 1;
			} else {
				return curid;
			}
		}
		return null;
	}
	
	public Integer getNextChapterId(Integer curid) {
		Cursor cursor = db.rawQuery("select max(_chapterid) from tb_content", null);
		if (cursor.moveToNext()) {
			if ((cursor.getInt(0) - curid) > 0) {
				return curid + 1;
			} else {
				return curid;
			}
		}
		return null;
	}
	
	public Integer getLastVolumeId(Integer curid) {
		Cursor cursor = db.rawQuery("select min(_id) from tb_catalog where _pid = 0", null);
		if (cursor.moveToNext()) {
			if ((curid - cursor.getInt(0)) > 0) {
				return curid - 1;
			} else {
				return curid;
			}
		}
		return null;
	}
	
	public Integer getNextVolumeId(Integer curid) {
		Cursor cursor = db.rawQuery("select max(_id) from tb_catalog where _pid = 0", null);
		if (cursor.moveToNext()) {
			if ((cursor.getInt(0) - curid) > 0) {
				return curid + 1;
			} else {
				return curid;
			}
		}
		return null;
	}
	
	public Info getInfo() {
		Cursor cursor = db.rawQuery("select _name, _author, _description, _about from tb_info where _id = 1", null);
		if (cursor.moveToNext()) {
			return new Info(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
		}
		return null;
	}
	
	public Integer getPidByChapterid(Integer chapterid) {
		Cursor cursor = db.rawQuery("select _pid from tb_catalog where _id = ?", new String[] { String.valueOf(chapterid) });
		if (cursor.moveToNext()) {
			return cursor.getInt(0);
		}
		return null;
	}
	
	public void insertBookMark(Integer chapterid, String catalog, String title, String bookmark, String datetime, String status, Integer scrollto) {
		db.execSQL("insert into tb_bookmark (_chapterid, _catalog, _title, _bookmark, _datetime, _status, _scrollto) values (?, ?, ?, ?, ?, ?, ?)", new Object[] { chapterid, catalog, title, bookmark, datetime, status, scrollto });
	}
	
	public List<BookMark> getBookMarks() {
		List<BookMark> bookmarks = new ArrayList<BookMark>();
		Cursor cursor = db.rawQuery("select * from tb_bookmark order by _id desc", null);
		while (cursor.moveToNext()) {
			bookmarks.add(new BookMark(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7)));
		}
		return bookmarks;
	}
	
	public void deleteBookMark(Integer id) {
		db.execSQL("delete from tb_bookmark where _id = ?", new Object[] { id });
	}
	
	public void deleteAllBookMark() {
		db.execSQL("delete from tb_bookmark");
	}
	
}
