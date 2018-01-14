package locavoreplatine.locafarm.view

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.farm_card_row.view.*
import locavoreplatine.locafarm.model.FarmModel
import android.widget.TextView



/**
 * Created by sparow on 14/01/18.
 */

class DefaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    var label: TextView? = null

    init {
        label = itemView.findViewById<View>(android.R.id.text1) as TextView
    }
}