package locavoreplatine.locafarm.view.viewAdapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.UserModel
import android.view.LayoutInflater
import locavoreplatine.locafarm.util.OnFarmItemClickListener
import locavoreplatine.locafarm.view.viewHolder.DefaultViewHolder
import locavoreplatine.locafarm.view.viewHolder.FarmViewHolder

/**
 * Created by sparow on 14/01/18.
 */
class FavRecyclerViewAdapter(private var items:  List<FarmModel>, private val onFarmItemClickListener: OnFarmItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        val v = inflater.inflate(R.layout.fragment_finder_card_row_farm, viewGroup, false)
        viewHolder = FarmViewHolder(v)

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val vh = viewHolder as FarmViewHolder
        val farm = items[position]
        Log.e("onBind","$position")
        vh.bindFarm(farm,onFarmItemClickListener)
    }


    override fun getItemCount() = items.size

}