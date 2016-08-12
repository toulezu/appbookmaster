package appbookmaster.oauth;

import java.util.SortedSet;

import longghoststory2.main.R;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class OAuthActivity extends Activity {
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private OAuthProvider httpOauthprovider;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauthlayout);
        String consumerKey="3811574189";
        String consumerSecret="15f7ae0d46ee8ba5be9946f8f7fbbe3c";
        String callBackUrl="myapp://AuthActivity";
        try{
        	httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey,consumerSecret);
    		httpOauthprovider = new DefaultOAuthProvider("http://api.t.sina.com.cn/oauth/request_token","http://api.t.sina.com.cn/oauth/access_token","http://api.t.sina.com.cn/oauth/authorize");
    		String authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, callBackUrl);
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
	
	@Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	Uri uri = intent.getData();
    	String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
    	try {
            httpOauthprovider.setOAuth10a(true); 
            httpOauthprovider.retrieveAccessToken(httpOauthConsumer,verifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SortedSet<String> user_id= httpOauthprovider.getResponseParameters().get("user_id");
        String userId=user_id.first();
        String userKey = httpOauthConsumer.getToken();
        String userSecret = httpOauthConsumer.getTokenSecret();

        TextView text=(TextView)findViewById(R.id.oauthtext);
        text.setText("suerId:"+userId+"/userKey:"+userKey+"/userSecret:"+userSecret);
    }
}
