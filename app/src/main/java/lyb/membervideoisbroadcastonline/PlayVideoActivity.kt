package lyb.membervideoisbroadcastonline

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import android.widget.Button
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.play_video_activity.*
import lyb.membervideoisbroadcastonline.adapter.SourceAdapter
import lyb.membervideoisbroadcastonline.bean.SourceBean
import lyb.membervideoisbroadcastonline.callback.OnRecyclerItemClickListener
import lyb.membervideoisbroadcastonline.widget.GridSpacingItemDecoration
import lyb.membervideoisbroadcastonline.widget.NoAdWebViewClient


/**
 * Created by yuanboliu on 18/2/23.
 * Use for
 */
class PlayVideoActivity : AppCompatActivity(), View.OnClickListener {
    /**
     * 传递过来的地址，需要拼接才可以播放
     */
    private var mUrl: String = ""

    /**
     * 当前选中的播放源，用来刷新的时候使用
     */
    private lateinit var mBtn: Button
    private var mCustomView: View? = null
    /** 视频全屏参数 */
    private var mCallBack: IX5WebChromeClient.CustomViewCallback? = null
    private val mSourceNameArray = arrayOf("百域阁", "石头", "接口1", "接口2", "接口3", "接口4", "接口5", "接口6", "接口7", "接口8", "接口9", "接口10")
    private val mSourceUrlArray = arrayOf("http://api.baiyug.cn/vip/index.php?url=", "http://jiexi.071811.cc/jx.php?url=",
            "http://jiexi.92fz.cn/player/vip.php?url=", "http://www.sjzvip.com/jiexi2.php?url=",
            "http://www.sjzvip.com/jiexi3.php?url=", "http://www.sjzvip.com/jiexi4.php?url=",
            "http://www.sjzvip.com/jiexi5.php?url=", "http://www.sjzvip.com/jiexi6.php?url=",
            "http://www.sjzvip.com/jiexi7.php?url=", "http://www.sjzvip.com/jiexi8.php?url=",
            "http://www.sjzvip.com/jiexi9.php?url=", "http://www.sjzvip.com/jiexi10.php?url=")
    /**
     * 资源数据
     */
    private val mList = mutableListOf<SourceBean>()
    private var mAdapter: SourceAdapter? = null
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.mRefreshTv ->{
                mProgressBar.progress = 0
                mProgressBar.visibility = View.VISIBLE
                mList.forEach stop@{
                    if (it.isChecked){
                        mWebView.loadUrl(it.url + mUrl)
                        return@stop
                    }
                }
            }
            R.id.mBackLayout ->{
                finish()
            }
        }

        if (p0 is Button){
            mBtn = p0
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.play_video_activity)
        mUrl = intent.getStringExtra("url")
        addData()
        initListeners()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        initWebThings()
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * 添加资源数据
     */
    private fun addData(){
        var sourceBean: SourceBean
        for (i in mSourceNameArray.indices){
            sourceBean = if (i == 0){
                SourceBean(i, mSourceNameArray[i], mSourceUrlArray[i], true)
            }else{
                SourceBean(i, mSourceNameArray[i], mSourceUrlArray[i], false)
            }
            mList.add(sourceBean)
        }
        initRecyclerView()
    }
    private fun initRecyclerView(){
        mAdapter = SourceAdapter(mList)
        mSourceRecyclerView.adapter = mAdapter
        mSourceRecyclerView.addItemDecoration(GridSpacingItemDecoration(5, 40, true))
        mSourceRecyclerView?.addOnItemTouchListener(object : OnRecyclerItemClickListener(mSourceRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
//                mWebView.stopLoading()
                mWebView.clearCache(true)
                mWebView.clearHistory()
                mProgressBar.progress = 0
                mProgressBar.visibility = View.VISIBLE
                val position = vh.adapterPosition
                mList.forEach {
                    it.isChecked = false
                }
                val bean: SourceBean = mList[position]
                bean.isChecked = true
                var url = bean.url+ mUrl
                mWebView.loadUrl(url)
                Log.e("url", url)
                mAdapter?.notifyDataSetChanged()
            }

            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }
    private fun initListeners(){
        mBackLayout.setOnClickListener(this)
        mRefreshTv.setOnClickListener(this)
    }
    //    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initWebThings(){
        val settings = mWebView.settings
        //屏幕自适应，关键点
        settings.useWideViewPort = true
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH) // 提高渲染的优先级
        settings.cacheMode = WebSettings.LOAD_NO_CACHE // 不加载缓存内容
        settings.allowFileAccess = true // 允许访问文件
        settings.setAppCacheEnabled(true)
        settings.pluginState = WebSettings.PluginState.ON
        settings.setSupportZoom(true) // 支持缩放
        settings.loadWithOverviewMode = true;
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        settings.databaseEnabled = true
        settings.pluginsEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
        }
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                mProgressBar.progress = newProgress
                if (newProgress == 100) {
                    mProgressBar.visibility = View.GONE
                }
            }

            override fun onShowCustomView(view: View?, callback: IX5WebChromeClient.CustomViewCallback?) {
                if (mCustomView == null){
                    mCustomView = view
                    mCallBack = callback
                    fullScreen()
                    playVideo()
                }
                super.onShowCustomView(view, callback)
            }

            override fun onHideCustomView() {
                fullScreen()
                stopVideo()
                super.onHideCustomView()
            }
        }
//        mWebView.webViewClient = object : WebViewClient(){
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                view.loadUrl(url)
//                return true
//            }
//        }
        mWebView.webViewClient = NoAdWebViewClient(this@PlayVideoActivity, mWebView)

        settings.javaScriptEnabled = true
        mBackTv.text = intent.getStringExtra("title")
        mUrl = intent.getStringExtra("url")
        var url = mSourceUrlArray[0] + mUrl
        mWebView.loadUrl(url)
    }
    fun playVideo(){
        mWebView.visibility = View.GONE
        mVideoContainer.visibility = View.VISIBLE
        mVideoContainer.addView(mCustomView)
    }
    /** 隐藏视频全屏 */
    fun stopVideo(){
        if (mCallBack != null){
            mCallBack?.onCustomViewHidden()
        }
        mWebView.visibility = View.VISIBLE
        mVideoContainer.removeAllViews()
        mVideoContainer.visibility = View.GONE
        mCustomView = null
    }
    /** 视频播放全屏 **/
    private fun fullScreen() {
        requestedOrientation = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause() // 暂停网页中正在播放的视频
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return if (KeyEvent.KEYCODE_BACK == keyCode){
            /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
            when {
                mVideoContainer.visibility == View.VISIBLE -> {
                    fullScreen()
                    stopVideo()
                }
                mWebView.canGoBack() -> mWebView.goBack()
                else -> finish()
            }
            true
        }else{
            super.onKeyUp(keyCode, event)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        //video_fullView.removeAllViews();
        mWebView.loadUrl("about:blank")
        mWebView.stopLoading()
        mWebView.webChromeClient = null
        mWebView.webViewClient = null
        mWebView.destroy()
    }
//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        //切换为竖屏
//
//        if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            fullScreen()
//            playVideo()
//        }
//
////切换为横屏
//
//        else if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            fullScreen()
//            stopVideo()
//        }
//    }
}