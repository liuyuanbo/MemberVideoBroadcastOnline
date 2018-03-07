package lyb.membervideoisbroadcastonline

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.platform_home_page_activity.*


/**
 * Created by yuanboliu on 18/2/23.
 * Use for
 */
class PlatformHomePageActivity: AppCompatActivity(), View.OnClickListener {
    private var mTitle = ""
    private var mUrl = ""
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.mPlayTv ->{
                var intent = Intent(this, PlayVideoActivity::class.java)
                intent.putExtra("url", mUrl)
                intent.putExtra("title", mTitle)
                startActivity(intent)
            }
            R.id.mBackLayout -> finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.platform_home_page_activity)
        initWebThings()
        initListeners()
    }
    private fun initListeners(){
        mBackLayout.setOnClickListener(this)
        mPlayTv.setOnClickListener(this)
        mTitleTv.setOnClickListener(this)
    }
    private fun initWebThings(){
        val settings = mWebView.settings
        settings.javaScriptEnabled = true
        //屏幕自适应
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        webViewFit()
//        mWebView.addJavascriptInterface(JsInterface(), "hello")
//        settings.setAppCacheEnabled(true)
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.databaseEnabled = true
//        settings.domStorageEnabled = true//开启DOM缓存，关闭的话H5自身的一些操作是无效的
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        val ua = settings.userAgentString
//        settings.userAgentString = ua + "; zlzq_android /" + getAppVersion("com.bm.zlzq")
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                mProgressBar.progress = newProgress
                if (newProgress == 100) {
                    mProgressBar.visibility = View.GONE
                }
            }
        }
        mWebView.webViewClient = object : WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("tag", "onPageFinished:         " + url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                url?.let { mUrl = it }
                Log.e("tag", "onPageStarted:         " + url)
            }
        }
        mTitle = intent.getStringExtra("title")
        mTitleTv.text = mTitle
        mUrl = intent.getStringExtra("url")
        mWebView.loadUrl(mUrl)
    }

    /**
     * 这里需要根据不同的分辨率设置不同的比例,比如
     * 5寸手机设置190  屏幕宽度 > 650   180
     * 4.5寸手机设置170  屏幕宽度>  500 小于 650  160
     * 4寸手机设置150  屏幕宽度>  450 小于 550  150
     * 3 屏幕宽度>  300 小于 450  120
     * 小于 300  100
     * 320×480  480×800 540×960 720×1280
     */
    private fun webViewFit() {
        val wm = this@PlatformHomePageActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = wm.defaultDisplay.width
        when {
            width > 650 -> this.mWebView.setInitialScale(190)
            width > 520 -> this.mWebView.setInitialScale(160)
            width > 450 -> this.mWebView.setInitialScale(140)
            width > 300 -> this.mWebView.setInitialScale(120)
            else -> this.mWebView.setInitialScale(100)
        }
    }
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return if (KeyEvent.KEYCODE_BACK == keyCode){
            /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
            when {
                mWebView.canGoBack() -> mWebView.goBack()
                else -> finish()
            }
            true
        }else{
            super.onKeyUp(keyCode, event)
        }
    }
}