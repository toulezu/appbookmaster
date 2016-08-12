package appbookmaster.base;

import com.baidu.mobstat.StatService;
import com.gfan.sdk.statitistics.GFAgent;

import longghoststory2.main.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import appbookmaster.bean.Info;
import appbookmaster.service.AppBookMasterService;

public class BaseActivity extends Activity {
	protected static final int PROGRESS_DIALOG = 1;
	protected AppBookMasterService bs = null;
	protected AppBookMasterApplication xa = null;
	protected Info info = null;
	protected Message message = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
	
	private void init() {
		showDialog(PROGRESS_DIALOG);
		
		if (xa == null) {
			xa = AppBookMasterApplication.getInstance();
		}
		xa.addActivity(this);
		bs = (AppBookMasterService) xa.getObj("bs"); 
		if (bs == null) {
			bs = new AppBookMasterService(this);
			xa.addObj("bs", bs);
		}
		info = (Info) xa.getObj("info");
		if (info == null) {
			info = bs.getInfo();
			xa.addObj("info", info);
		}
		
		message = new Message();
		message.what = 0;
		handler.sendMessage(message);
	}
	
	protected void appExitWithAlert() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle(R.string.alert_title);
        builder.setMessage(R.string.alert_msg);
        builder.setPositiveButton(R.string.alert_sure, new android.content.DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();
                appExitNoAlert();
            }
        });  
        builder.setNegativeButton(R.string.alert_cancel, new android.content.DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }
        });  
        builder.create().show();
    }
	
	protected void appExitNoAlert() {
		xa.exit();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	  switch(id) {
        case PROGRESS_DIALOG:
        	return ProgressDialog.show(this, null, "数据正在加载,请稍等...", true);
        default: 
        	return super.onCreateDialog(id);
	  }
	}
	
	protected String getNiceString(String content) {
		return content = content.replace("\r\n", "\n");
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					dismissDialog(PROGRESS_DIALOG);
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	protected void onPause() {
		super.onPause();
		GFAgent.onPause(this);
		StatService.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		GFAgent.onResume(this);
		StatService.onResume(this);
	}
}
