package lyb.membervideoisbroadcastonline.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import lyb.membervideoisbroadcastonline.util.ADFilterTool;

/**
 * Created by yuanboliu on 18/2/24.
 * Use for
 */

public class NoAdWebViewClient extends WebViewClient {
	private Context context;
	private WebView webView;
	private boolean isClose;

	public NoAdWebViewClient(Context context,WebView webView) {
		this.context = context;
		this.webView = webView;
	}


	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		if(isClose){ //如果线程正在运行就不用重新开启一个线程了
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				isClose = true;
				while (isClose){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(0x001);
				}
			}
		}).start();
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String js = ADFilterTool.getClearAdDivJs(context);
			Log.v("adJs",js);
			webView.loadUrl(js);
		}
	};

	@Override
	public boolean shouldOverrideUrlLoading(WebView webView, String s) {
		webView.loadUrl(s);
		return true;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		isClose = false;
	}
}
