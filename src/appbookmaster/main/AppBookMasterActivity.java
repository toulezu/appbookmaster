package appbookmaster.main;

import longghoststory2.main.R;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import appbookmaster.base.BaseActivity;
import appbookmaster.bean.LastRead;

public class AppBookMasterActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	private void init() {
		((TextView) findViewById(R.id._init_name)).setText(info.getName());
		((TextView) findViewById(R.id._author)).setText(info.getAuthor());
		
		TimerTask task = new TimerTask() {
			public void run() {
				message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		};

		Timer timer = new Timer(true);
		timer.schedule(task, 1000);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					final LastRead lastread = bs.getLastRead();
					if (lastread != null) {
						Intent intent = new Intent(AppBookMasterActivity.this, AppBookMasterContentActivity.class);
						intent.putExtra("chapterid", lastread.getChapterid().toString());
						startActivity(intent);
					} else {
						startActivity(new Intent(AppBookMasterActivity.this, AppBookMasterCatlogActivity.class));
					}
					break;
			}
			super.handleMessage(msg);
		}
	};
	
}