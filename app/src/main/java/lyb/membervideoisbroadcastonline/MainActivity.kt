package lyb.membervideoisbroadcastonline

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    /**
     * 各个平台的首页网址
     */
    private var mHomePageUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListeners()
    }
    fun initListeners(){
        mIQY.setOnClickListener(this)
        mTencent.setOnClickListener(this)
        mYouKu.setOnClickListener(this)
        mLeShi.setOnClickListener(this)
        mSouHu.setOnClickListener(this)
        mMangGuo.setOnClickListener(this)
        mCourse.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        var intent = Intent()
        when(v?.id){
            R.id.mIQY ->{
                mHomePageUrl = "https://m.iqiyi.com"
                intent.setClass(this, PlatformHomePageActivity::class.java)
                intent.putExtra("url", "https://m.iqiyi.com")
                intent.putExtra("title", "爱奇艺")
                startActivity(intent)
            }
            R.id.mTencent ->{
                mHomePageUrl = "http://m.v.qq.com"
                intent.setClass(this, PlatformHomePageActivity::class.java)
                intent.putExtra("url", "http://m.v.qq.com")
                intent.putExtra("title", "腾讯")
                startActivity(intent)
            }
            R.id.mYouKu ->{
                mHomePageUrl = "https://www.youku.com"
                intent.setClass(this, PlatformHomePageActivity::class.java)
                intent.putExtra("url", "https://www.youku.com")
                intent.putExtra("title", "优酷")
                startActivity(intent)
            }
            R.id.mLeShi ->{
                mHomePageUrl = "https://m.le.com"
                intent.setClass(this, PlatformHomePageActivity::class.java)
                intent.putExtra("url", "https://m.le.com")
                intent.putExtra("title", "乐视")
                startActivity(intent)
            }
            R.id.mSouHu ->{
                mHomePageUrl = "https://m.tv.sohu.com"
                intent.setClass(this, PlatformHomePageActivity::class.java)
                intent.putExtra("url", "https://m.tv.sohu.com")
                intent.putExtra("title", "搜狐")
                startActivity(intent)
            }
            R.id.mMangGuo ->{
                mHomePageUrl = "https://m.mgtv.com"
                intent.setClass(this, PlatformHomePageActivity::class.java)
                intent.putExtra("url", "https://m.mgtv.com")
                intent.putExtra("title", "芒果Tv")
                startActivity(intent)
            }
            R.id.mCourse ->{
                intent.setClass(this, TeachActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
