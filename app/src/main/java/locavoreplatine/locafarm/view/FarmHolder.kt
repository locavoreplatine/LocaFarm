package locavoreplatine.locafarm.view

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.farm_card_row.view.*
import locavoreplatine.locafarm.model.FarmModel

/**
 * Created by sparow on 14/01/18.
 */

class FarmHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
    //2
    private var view: View = v
    private var farm: FarmModel? = null

    //3
    init {
        v.setOnClickListener(this)
    }

    //4
    override fun onClick(v: View) {
//            val context = itemView.context
//            val showFarmIntent = Intent(context, FarmActivity::class.java)
//            showFarmIntent.putExtra(PHOTO_KEY, farm)
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