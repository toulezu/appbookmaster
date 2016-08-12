package appbookmaster.main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import appbookmaster.base.BaseActivity;
import appbookmaster.bean.CataLog;
import longghoststory2.main.R;

@SuppressWarnings("unchecked")
public class AppBookMasterChapterActivity extends BaseActivity {
	private SimpleAdapter simpleAdapter = null;
	private ListView chapterlistview = null;
	private Integer pid = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chapter);
		init();
	}
	
	private void refreshListView() {
		simpleAdapter = new SimpleAdapter(this, getChapterData(pid), R.layout.chapterlistview, new String[] { "_id", "_titlecatalog", "_des" }, new int[] { R.id._id, R.id._titlecatalog, R.id._des });
		chapterlistview.setAdapter(simpleAdapter);
	}
	
	private void init() {
		pid = Integer.parseInt(getIntent().getStringExtra("pid"));
		
		((TextView) findViewById(R.id.title)).setText(bs.getCatalogById(pid).getTitle());
		
		chapterlistview = (ListView) findViewById(R.id.chapterlistview);
		refreshListView();
		chapterlistview.setOnItemClickListener(onItemClickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshListView();
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> aView, View view, int arg2, long arg3) {
			Intent intent = new Intent(AppBookMasterChapterActivity.this, AppBookMasterContentActivity.class);
			intent.putExtra("chapterid", ((Map<String, String>) simpleAdapter.getItem(arg2)).get("_id"));
			startActivity(intent);
		}
	};

	private LinkedList<Map<String, String>> getChapterData(Integer pid) {
		LinkedList<Map<String, String>> list = new LinkedList<Map<String, String>>();
		Map<String, String> map = null;
		for (CataLog catalog : bs.getChapterListByPid(pid)) {
			map = new HashMap<String, String>();
			map.put("_id", catalog.getId().toString());
			map.put("_titlecatalog", catalog.getTitle() + "    " + catalog.getCatalog());
			map.put("_des", "已经阅读到 " + catalog.getStatus());
			list.add(map);
		}
		return list;
	}
	
	@Override  
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.content_last_volume).setIcon(R.drawable.menu_last);
        menu.add(0, 2, 2, R.string.content_home).setIcon(R.drawable.menu_homepage);
        menu.add(0, 3, 3, R.string.content_next_volume).setIcon(R.drawable.menu_next);
        return super.onCreateOptionsMenu(menu);  
    }  
  
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = null;
    	switch (item.getItemId()) {
			case 1:
				intent = new Intent(AppBookMasterChapterActivity.this, AppBookMasterChapterActivity.class);
				intent.putExtra("pid", String.valueOf(bs.getLastVolumeId(pid)));
				startActivity(intent);
				break;
			case 2:
				startActivity(new Intent(AppBookMasterChapterActivity.this, AppBookMasterCatlogActivity.class));
				break;
			case 3:
				intent = new Intent(AppBookMasterChapterActivity.this, AppBookMasterChapterActivity.class);
				intent.putExtra("pid", String.valueOf(bs.getNextVolumeId(pid)));
				startActivity(intent);
				break;
			default:
				break;
		}
        return super.onOptionsItemSelected(item);  
    }
	
}
