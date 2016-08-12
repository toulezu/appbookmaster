package appbookmaster.main;

import longghoststory2.main.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import appbookmaster.base.BaseActivity;
import appbookmaster.bean.BookMark;

@SuppressWarnings("unchecked")
public class AppBookMasterBookMarkActivity extends BaseActivity {
	private SimpleAdapter simpleAdapter = null;
	private ListView bookmarklistview = null;
	private Integer itemlongclickselected = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark);
		init();
	}
	
	private void refreshListView() {
		simpleAdapter = new SimpleAdapter(this, getBookMarkData(), R.layout.bookmarklistview, new String[] { "_bookmark_id", " _chapterid", "_scrollto_status", "_catalog_title_status", "_bookmark" }, new int[] { R.id._bookmark_id, R.id._chapterid, R.id._scrollto_status, R.id._catalog_title_status, R.id._bookmark });
		bookmarklistview.setAdapter(simpleAdapter);
	}
	
	private void init() {
		bookmarklistview = (ListView) findViewById(R.id.bookmarklistview);
		refreshListView();
		bookmarklistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
				Intent intent = new Intent(AppBookMasterBookMarkActivity.this, AppBookMasterContentActivity.class);
				Map<String, String> data = (Map<String, String>) simpleAdapter.getItem(paramInt);
				intent.putExtra("chapterid", data.get("_chapterid"));
				intent.putExtra("_scrollto_status", data.get("_scrollto_status"));
				startActivity(intent);
			}
		});
		bookmarklistview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
				itemlongclickselected = Integer.parseInt(((Map<String, String>) simpleAdapter.getItem(paramInt)).get("_bookmark_id"));
				return false;
			}
		});
		bookmarklistview.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenuInfo paramContextMenuInfo) {
				paramContextMenu.setHeaderTitle("删除书签");
				paramContextMenu.add(0, 0, 0, "删除当前");
				paramContextMenu.add(0, 1, 0, "删除所有");
			}
		}); 
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 0:
				bs.deleteBookMark(itemlongclickselected);
				refreshListView();
				break;
			case 1:
				bs.deleteAllBookMark();
				refreshListView();
				break;
			default:
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshListView();
	}
	
	@Override  
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.content_home).setIcon(R.drawable.menu_homepage);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override  
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
			case 1:
				startActivity(new Intent(AppBookMasterBookMarkActivity.this, AppBookMasterCatlogActivity.class));
				break;
			default:
				break;
		}
        return super.onOptionsItemSelected(item);  
    }

	private LinkedList<Map<String, String>> getBookMarkData() {
		LinkedList<Map<String, String>> list = new LinkedList<Map<String, String>>();
		Map<String, String> map = null;
		for (BookMark bookmark : bs.getBookMarks()) {
			map = new HashMap<String, String>();
			map.put("_bookmark_id", bookmark.getId().toString());
			map.put("_chapterid", bookmark.getChapterid().toString());
			map.put("_scrollto_status", bookmark.getScrollto() + ":" + bookmark.getStatus());
			map.put("_catalog_title_status", bookmark.getTitle() + "  " + bookmark.getCatalog() + "  " + bookmark.getStatus());
			map.put("_bookmark", (bookmark.getBookmark() + "  " + bookmark.getDatetime()).trim());
			list.add(map);
		}
		return list;
	}
	
}