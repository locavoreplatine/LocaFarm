package locavoreplatine.locafarm.view.viewAdapter

import android.support.v7.widget.RecyclerView
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
class FinderRecyclerViewAdapter(private val items:  List<Any>, private val onFarmItemClickListener: OnFarmItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val FARM = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        when (viewType) {
            FARM -> {
                val v2 = inflater.inflate(R.layout.fragment_finder_card_row_farm, viewGroup, false)
                viewHolder = FarmViewHolder(v2)
            }
            else -> {
                val v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false)
                viewHolder = DefaultViewHolder(v)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            FARM -> {
                val vh2 = viewHolder as FarmViewHolder
                configureFarmViewHolder(vh2, position)
            }
            else -> {
                val vh = viewHolder as DefaultViewHolder
                configureDefaultViewHolder(vh, position)
            }
        }
    }

    override fun getItemCount() = items.size


    override fun getItemViewType(position: Int): Int {
        if (items[position] is FarmModel) {
            return FARM
        }
        return -1
    }

    private fun configureDefaultViewHolder(vh: DefaultViewHolder, position: Int) {
        vh.label!!.text = items[position] as CharSequence
    }

    private fun configureFarmViewHolder(vFarm: FarmViewHolder, position: Int) {
        val farm = items[position] as FarmModel
        vFarm.bindFarm(farm,onFarmItemClickListener)
    }







}