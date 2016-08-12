package appbookmaster.main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import appbookmaster.base.BaseActivity;
import appbookmaster.bean.CataLog;
import appbookmaster.bean.LastRead;
import longghoststory2.main.R;

@SuppressWarnings("unchecked")
public class AppBookMasterCatlogActivity extends BaseActivity {
	private LastRead lastread = null;
	private Button itemreadcontinuebtn = null;
	
	private SimpleAdapter simpleAdapter = null;
	private ListView cataloglistview = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catalog);
		init();
	}
	
	private void refreshListView() {
		simpleAdapter = new SimpleAdapter(this, getCatalogData(), R.layout.cataloglistview, new String[] { "catalog_img", "_id", "_title", "_catalog" }, new int[] { R.id.catalog_img, R.id._id, R.id._title, R.id._catalog });
		cataloglistview.setAdapter(simpleAdapter);
	}

	private void init() {
		((TextView) findViewById(R.id._name)).setText(info.getName());
		((TextView) findViewById(R.id._des)).setText(getNiceString(info.getDescription()));
		((Button) findViewById(R.id.bookmarkbtn)).setOnClickListener(onClickListener);
		
		cataloglistview = (ListView) findViewById(R.id.cataloglistview);
		refreshListView();
		cataloglistview.setOnItemClickListener(onItemClickListener);
		
		itemreadcontinuebtn = (Button) findViewById(R.id.itemreadcontinuebtn);
		lastread = bs.getLastRead();
		if (lastread != null) {
			System.out.println(lastread.getChapterid());
			itemreadcontinuebtn.setText("继续阅读 (" + bs.getCatalogById(lastread.getChapterid()).getTitle() + " " + lastread.getStatus() + ")");
			itemreadcontinuebtn.setOnClickListener(onClickListener);
		} else {
			itemreadcontinuebtn.setText("没有阅读记录");
			itemreadcontinuebtn.setClickable(false);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
            appExitWithAlert();
        }
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		init();
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> aView, View view, int arg2, long arg3) {
			Intent intent = new Intent(AppBookMasterCatlogActivity.this, AppBookMasterChapterActivity.class);
			intent.putExtra("pid", ((Map<String, String>) simpleAdapter.getItem(arg2)).get("_id"));
			startActivity(intent);
		}
	};

	private LinkedList<Map<String, Object>> getCatalogData() {
		LinkedList<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (CataLog catalog : bs.getCatalogList()) {
			map = new HashMap<String, Object>();
			map.put("catalog_img", getCatalogImg(catalog.getId()));
			map.put("_id", catalog.getId().toString());
			map.put("_title", catalog.getTitle());
			map.put("_catalog", catalog.getCatalog());
			list.add(map);
		}
		return list;
	}
	
	private Object getCatalogImg(Integer id) {
		switch (id) {
			case 1:
				return R.drawable.part_1;
			case 2:
				return R.drawable.part_2;
			case 3:
				return R.drawable.part_3;
			case 4:
				return R.drawable.part_4;
			default:
				return null;
		}
	}
	
	@Override  
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.alert_exit).setIcon(R.drawable.menu_exit);
        return super.onCreateOptionsMenu(menu);  
    }  
  
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
			case 1:
				appExitNoAlert();
				break;
			default:
				break;
		}
        return super.onOptionsItemSelected(item);  
    }
    
    private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Button btn = (Button) arg0;
			switch (btn.getId()) {
				case R.id.bookmarkbtn:
					startActivity(new Intent(AppBookMasterCatlogActivity.this, AppBookMasterBookMarkActivity.class));
					break;
				case R.id.itemreadcontinuebtn:
					Intent intent = new Intent(AppBookMasterCatlogActivity.this, AppBookMasterContentActivity.class);
					intent.putExtra("chapterid", lastread.getChapterid().toString());
					startActivity(intent);
					break;
				default:
					break;
			}
		}
	};
}