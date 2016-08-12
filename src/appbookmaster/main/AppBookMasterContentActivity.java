package appbookmaster.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import longghoststory2.main.R;
import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import appbookmaster.base.AppBookMasterPreferenceActivity;
import appbookmaster.base.BaseActivity;
import appbookmaster.bean.CataLog;
import appbookmaster.bean.Content;
import appbookmaster.common.Constant;
import appbookmaster.oauth.OAuthActivity;
import appbookmaster.util.StringUtils;
import appbookmaster.view.ScrollTextView;

public class AppBookMasterContentActivity extends BaseActivity {
	private ScrollTextView content = null;
	private TextView status = null;
	private Content con = null;
	private Integer chapterid = null;
	private CataLog catalog = null;
	private LinearLayout contentLayout = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);
		init();
	}
	
	private void init() {
		((ImageView) findViewById(R.id.content_chapters)).setOnClickListener(onImageViewClickListener);
		((ImageView) findViewById(R.id.content_home)).setOnClickListener(onImageViewClickListener);
		((ImageView) findViewById(R.id.content_last_chapter)).setOnClickListener(onImageViewClickListener);
		((ImageView) findViewById(R.id.content_next_chapter)).setOnClickListener(onImageViewClickListener);
		
		contentLayout = (LinearLayout) findViewById(R.id.content_layout);
		content = (ScrollTextView) findViewById(R.id._content);
		status = (TextView) findViewById(R.id._status);
		
		chapterid = Integer.parseInt(getIntent().getStringExtra("chapterid"));
		catalog = bs.getCatalogById(chapterid);
		((TextView) findViewById(R.id._curcatalog)).setText(catalog.getTitle() + " " + catalog.getCatalog());
		
		content.setStatus(status);
		content.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		con = bs.getBookContentByChapterid(chapterid);
		content.setText(getNiceString(con.getContent()));
		
		String _scrollto_status = getIntent().getStringExtra("_scrollto_status");
		// 设置初始滚动状态
		if (StringUtils.isNotEmpty(_scrollto_status)) {
			String[] strs = _scrollto_status.split(":");
			content.scrollTo(0, Integer.parseInt(strs[0]));
			status.setText("阅读到 " + strs[1]);
		} else {
			content.scrollTo(0, con.getScrollto());
			status.setText("阅读到 " + con.getStatus());
		}
		
		// ad
		AdManager.init(this, Constant.AD_APP_ID, Constant.AD_APP_PASSWORD, Constant.AD_INTERVAL, Constant.AD_IS_DEBUG_MODE);
		LinearLayout adViewLayout = (LinearLayout) findViewById(R.id.adViewLayout);
		adViewLayout.addView(new AdView(this), new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		showSettings();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		bs.updateBookContent(content.getScrollY(), getStatus(), con.getId());
		bs.insertOrUpdateLastRead(chapterid, content.getScrollY(), getStatus());
	}
	
	private String getStatus() {
		return content.getStatus().getText().toString().replace("阅读到 ", "");
	}
	
	private OnClickListener onImageViewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ImageView img = (ImageView) v;
			Intent intent = null;
			switch (img.getId()) {
				case R.id.content_chapters:
					intent = new Intent(AppBookMasterContentActivity.this, AppBookMasterChapterActivity.class);
					intent.putExtra("pid", String.valueOf(bs.getPidByChapterid(chapterid)));
					startActivity(intent);
					break;
				case R.id.content_home:	
					startActivity(new Intent(AppBookMasterContentActivity.this, AppBookMasterCatlogActivity.class));
					break;
				case R.id.content_last_chapter:
					intent = new Intent(AppBookMasterContentActivity.this, AppBookMasterContentActivity.class);
					intent.putExtra("chapterid", String.valueOf(bs.getLastChapterId(chapterid)));
					startActivity(intent);
					break;
				case R.id.content_next_chapter:
					intent = new Intent(AppBookMasterContentActivity.this, AppBookMasterContentActivity.class);
					intent.putExtra("chapterid", String.valueOf(bs.getNextChapterId(chapterid)));
					startActivity(intent);
					break;
				default:
					break;
			}
		}
	};
	
	@Override  
    public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.content_last_chapter).setIcon(R.drawable.menu_last);
        menu.add(0, 2, 2, R.string.content_chapters).setIcon(R.drawable.menu_showtype_list);
        menu.add(0, 3, 3, R.string.content_next_chapter).setIcon(R.drawable.menu_next);
        menu.add(0, 4, 4, R.string.content_favorite).setIcon(R.drawable.menu_favorite);
        menu.add(0, 5, 5, R.string.alert_exit).setIcon(R.drawable.menu_exit);
        SubMenu sm = menu.addSubMenu(0, 6, 6, R.string.content_more).setIcon(R.drawable.menu_more);
	    sm.add(6, 7, 7, R.string.content_home);
	    sm.add(6, 8, 8, R.string.content_all_favorite);
	    sm.add(6, 9, 9, R.string.content_setting);
	    sm.add(6, 10, 10, R.string.content_advice);
	    sm.add(6, 11, 11, R.string.content_about);
	    sm.add(6, 12, 12, "分享到新浪微博");
        return super.onCreateOptionsMenu(menu);
    }
  
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = null;
    	AlertDialog.Builder builder = null;
    	switch (item.getItemId()) {
			case 1:
				// 上一章
				intent = new Intent(AppBookMasterContentActivity.this, AppBookMasterContentActivity.class);
				intent.putExtra("chapterid", String.valueOf(bs.getLastChapterId(chapterid)));
				startActivity(intent);
				break;
			case 2:
				// 目录
				intent = new Intent(AppBookMasterContentActivity.this, AppBookMasterChapterActivity.class);
				intent.putExtra("pid", String.valueOf(bs.getPidByChapterid(chapterid)));
				startActivity(intent);
				break;
			case 3:
				// 下一章
				intent = new Intent(AppBookMasterContentActivity.this, AppBookMasterContentActivity.class);
				intent.putExtra("chapterid", String.valueOf(bs.getNextChapterId(chapterid)));
				startActivity(intent);
				break;
			case 4:
				// 添加书签
				builder = new Builder(this);
				builder.setTitle(R.string.content_favorite);
				final EditText text = new EditText(this);
				text.setBackgroundResource(R.drawable.bookdes_bg);
				builder.setView(text);
		        builder.setPositiveButton(R.string.alert_sure, new android.content.DialogInterface.OnClickListener() {
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {
		            	String bookmark = text.getText().toString();
		            	if (bookmark.length() > 20) {
		            		Toast.makeText(AppBookMasterContentActivity.this, "添加失败，书签长度不能超过 20 个字符", Toast.LENGTH_SHORT).show();
		            	} else {
		            		bs.insertBookMark(chapterid, catalog.getCatalog(), catalog.getTitle(), bookmark, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), getStatus(), content.getScrollY());
		            	}
		            }
		        });
		        builder.setNegativeButton(R.string.alert_cancel, new android.content.DialogInterface.OnClickListener() {
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                dialog.dismiss();
		            }
		        });
		        builder.create().show();
				break;
			case 5:
				// 退出
				appExitNoAlert();
				break;
			case 7:
				// 主页
				startActivity(new Intent(AppBookMasterContentActivity.this, AppBookMasterCatlogActivity.class));
				break;
			case 8:
				// 查看书签
				startActivity(new Intent(AppBookMasterContentActivity.this, AppBookMasterBookMarkActivity.class));
				break;
			case 9:
				// 设置
				intent = new Intent(AppBookMasterContentActivity.this, AppBookMasterPreferenceActivity.class);  
	            startActivityForResult(intent, 1);
				break;
			case 10:
				// 意见
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.ADVICE_URL)));
				break;
			case 11:
				// 关于
				builder = new Builder(this);
				builder.setTitle(R.string.content_about);
				TextView about = new TextView(this);
				about.setText(info.getAbout());
				about.setTextSize(14f);
				builder.setView(about);
		        builder.setPositiveButton(R.string.alert_sure, new android.content.DialogInterface.OnClickListener() {
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                dialog.dismiss();
		            }
		        });
		        builder.create().show();
				break;
			case 12:
	        	startActivity(new Intent(AppBookMasterContentActivity.this, OAuthActivity.class));
			default:
				break;
		}
        return super.onOptionsItemSelected(item);
    }
      
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        showSettings();  
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(AppBookMasterContentActivity.this, AppBookMasterChapterActivity.class);
			intent.putExtra("pid", String.valueOf(bs.getPidByChapterid(chapterid)));
			startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
	}

	private void showSettings() {
        SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE);  
        boolean nightMode = prefs.getBoolean("nightMode", false);
        if (nightMode) {
        	content.setTextColor(0X000080FF);
        	contentLayout.setBackgroundColor(0X111111FF);
        } else {
        	content.setTextColor(0X111111FF);
        	contentLayout.setBackgroundColor(0XDDF0E4FF);
        }
        
        String textSize = prefs.getString("textSize", "0");  
        if (textSize.equals("0")) {
        	content.setTextSize(16f);  
        } else if (textSize.equals("1")) {
        	content.setTextSize(12f);  
        } else if (textSize.equals("2")) {
        	content.setTextSize(20f);
        } else if (textSize.equals("3")) {
        	content.setTextSize(24f);
        }

        String contentBgColorValue = prefs.getString("contentBgColor", "0");
        if (contentBgColorValue.equals("0")) {
        	contentLayout.setBackgroundColor(0XDDF0E4FF);
        } else if (contentBgColorValue.equals("1")) {
        	contentLayout.setBackgroundColor(0X111111FF);
		} else if (contentBgColorValue.equals("2")) {
			contentLayout.setBackgroundColor(0XB2D5EEFF);
		} else if (contentBgColorValue.equals("3")) {
			contentLayout.setBackgroundColor(0XF5F5F5FF);
		} else if (contentBgColorValue.equals("4")) {
			contentLayout.setBackgroundColor(0XCAB6EBFF);
		} else if (contentBgColorValue.equals("5")) {
			contentLayout.setBackgroundColor(0XF5C0D0FF);
		} else if (contentBgColorValue.equals("6")) {
			contentLayout.setBackgroundColor(0XF6DC76FF);
		} else if (contentBgColorValue.equals("7")) {
			contentLayout.setBackgroundColor(0XAFD76BFF);
		} else if (contentBgColorValue.equals("8")) {
			contentLayout.setBackgroundColor(0XDDF0E4FF);
		} else if (contentBgColorValue.equals("9")) {
			contentLayout.setBackgroundColor(0X91DCDFFF);
		} else if (contentBgColorValue.equals("10")) {
			contentLayout.setBackgroundColor(0X55688AFF);
		} else if (contentBgColorValue.equals("11")) {
			contentLayout.setBackgroundColor(0X380E00FF);
		} else if (contentBgColorValue.equals("12")) {
			contentLayout.setBackgroundColor(0X6A627BFF);
		}
        
        int textColor = prefs.getInt("colorpiker", Color.BLACK);
        content.setTextColor(textColor);
	}
}