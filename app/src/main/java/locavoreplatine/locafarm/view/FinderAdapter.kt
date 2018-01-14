package locavoreplatine.locafarm.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.UserModel
import android.view.LayoutInflater
import locavoreplatine.locauser.view.UserHolder

/**
 * Created by sparow on 14/01/18.
 */
class FinderAdapter(private val items:  ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


//    // The items to display in your RecyclerView
//    private val items: List<Any>? = null

    private val USER = 0
    private val FARM = 1


    override fun getItemCount() = items.size


    override fun getItemViewType(position: Int): Int {
        if (items.get(position) is UserModel) {
            return USER
        } else if (items.get(position) is FarmModel) {
            return FARM
        }
        return -1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        when (viewType) {
            USER -> {
                val v1 = inflater.inflate(R.layout.user_card_row, viewGroup, false)
                viewHolder = UserHolder(v1)
            }
            FARM -> {
                val v2 = inflater.inflate(R.layout.farm_card_row, viewGroup, false)
                viewHolder = FarmHolder(v2)
            }
            else -> {
                val v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false)
                viewHolder = RecyclerViewSimpleTextViewHolder(v)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            USER -> {
                val vh1 = viewHolder as UserHolder
                configureViewHolder1(vh1, position)
            }
            FARM -> {
                val vh2 = viewHolder as FarmHolder
                configureViewHolder2(vh2, position)
            }
            else -> {
                val vh = viewHolder as RecyclerViewSimpleTextViewHolder
                configureDefaultViewHolder(vh, position)
            }
        }
    }

    private fun configureDefaultViewHolder(vh: RecyclerViewSimpleTextViewHolder, position: Int) {
        vh.label!!.text = items[position] as CharSequence
    }

    private fun configureViewHolder1(vUser: UserHolder, position: Int) {
        val user = items[position] as UserModel
        vUser.bindUser(user)
    }

    private fun configureViewHolder2(vFarm: FarmHolder,position: Int) {
        val farm = items[position] as FarmModel
        vFarm.bindFarm(farm)
    }







}