package locavoreplatine.locafarm.view.viewAdapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_farm_item_product.view.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.model.ProductModel
import locavoreplatine.locafarm.view.viewHolder.ProductViewHolder


/**
 * Created by toulouse on 23/01/18.
 */
class FarmRecyclerViewAdapter(private val items:  List<ProductModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_farm_item_product,parent,false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val mHolder = holder as ProductViewHolder
        mHolder.itemView.image.setImageResource(R.mipmap.ic_launcher_round)
        mHolder.itemView.txtView.text=items[position].name
    }
}