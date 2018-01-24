package locavoreplatine.locafarm.view.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.fragment_finder_card_row_farm.view.*
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.util.OnFarmItemClickListener
import locavoreplatine.locafarm.view.viewAdapter.FinderRecyclerViewAdapter
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

    }

    fun bindFarm(farm: FarmModel, listener: OnFarmItemClickListener) {
        view.farm_name.text = farm.name
        view.setOnClickListener {
            listener.onItemClick(farm)
        }
    }

    companion object {
        //5
        private val FARM_KEY = "FARM"
    }
}