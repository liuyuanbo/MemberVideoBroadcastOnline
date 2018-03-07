package lyb.membervideoisbroadcastonline.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import lyb.membervideoisbroadcastonline.R
import lyb.membervideoisbroadcastonline.bean.SourceBean

/**
 * Created by yuanboliu on 18/2/24.
 * Use for
 */
class SourceAdapter(private var mList: MutableList<SourceBean>): RecyclerView.Adapter<SourceAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.source_item, parent, false))

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bind(mList[position])
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val itemBtn: Button = itemView.findViewById(R.id.source_btn) as Button
        fun bind(entity: SourceBean) {
            itemBtn.text = entity.name
            if (entity.isChecked){
                itemBtn.setBackgroundResource(R.drawable.cornor_btn)
                itemBtn.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }else{
                itemBtn.setBackgroundResource(R.drawable.cornor_btn0)
                itemBtn.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            }
        }
    }
}