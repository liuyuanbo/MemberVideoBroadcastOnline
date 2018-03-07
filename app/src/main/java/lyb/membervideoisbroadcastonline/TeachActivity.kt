package lyb.membervideoisbroadcastonline

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.teach_activity.*

/**
 * Created by yuanboliu on 18/2/25.
 * Use for
 */
class TeachActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teach_activity)
        mBackLayout.setOnClickListener {
            finish()
        }
    }
}