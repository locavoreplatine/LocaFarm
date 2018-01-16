package locavoreplatine.locafarm.view.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.farm_card_row.view.*
import locavoreplatine.locafarm.model.FarmModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by sparow on 14/01/18.
 */

class FarmViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener, AnkoLogger {
    //2
    private var view: View = v
    private var farm: FarmModel? = null

    init {
        v.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        info(FARM_KEY)
//            val context = itemView.context
//            val showFarmIntent = Intent(context, FarmActivity::class.java)
//            showFarmIntent.putExtra(FARM_KEY, farm)
//            context.startActivity(showFarmIntent)
    }

    fun bindFarm(farm: FarmModel) {
//            this.farm = farm
//            Picasso.with(view.context).load(farm.url).into(view.itemImage)
//            view.itemDate.text = farm.humanDate
//            view.itemDescription.text = farm.explanation
        view.farm_name.text = farm.name
    }

    companion object {
        //5
        private val FARM_KEY = "FARM"
    }
}