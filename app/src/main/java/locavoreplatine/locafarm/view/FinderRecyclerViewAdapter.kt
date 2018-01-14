package locavoreplatine.locafarm.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.UserModel
import android.view.LayoutInflater
import locavoreplatine.locauser.view.UserViewHolder

/**
 * Created by sparow on 14/01/18.
 */
class FinderRecyclerViewAdapter(private val items:  ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


//    // The items to display in your RecyclerView
//    private val items: List<Any>? = null

    private val USER = 0
    private val FARM = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        when (viewType) {
            USER -> {
                val v1 = inflater.inflate(R.layout.user_card_row, viewGroup, false)
                viewHolder = UserViewHolder(v1)
            }
            FARM -> {
                val v2 = inflater.inflate(R.layout.farm_card_row, viewGroup, false)
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
            USER -> {
                val vh1 = viewHolder as UserViewHolder
                configureUserViewHolder(vh1, position)
            }
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
        if (items[position] is UserModel) {
            return USER
        } else if (items[position] is FarmModel) {
            return FARM
        }
        return -1
    }

    private fun configureDefaultViewHolder(vh: DefaultViewHolder, position: Int) {
        vh.label!!.text = items[position] as CharSequence
    }

    private fun configureUserViewHolder(vUser: UserViewHolder, position: Int) {
        val user = items[position] as UserModel
        vUser.bindUser(user)
    }

    private fun configureFarmViewHolder(vFarm: FarmViewHolder, position: Int) {
        val farm = items[position] as FarmModel
        vFarm.bindFarm(farm)
    }







}