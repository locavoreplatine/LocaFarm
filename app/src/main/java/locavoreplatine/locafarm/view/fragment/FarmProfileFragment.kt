package locavoreplatine.locafarm.view.fragment

import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_farm.*
import kotlinx.android.synthetic.main.fragment_farm_detail.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.view.viewAdapter.FarmRecyclerViewAdapter
import locavoreplatine.locafarm.viewModel.FarmProfileViewModel
import android.content.Intent
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.fragment_farm_recycler.*
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.contentView


class FarmProfileFragment : Fragment(), LifecycleOwner{

    val name:String = "farmProfileFragment_${arguments?.getLong("farmId")}"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_farm,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_farm_ratingbar.visibility=View.GONE

    }

    override fun onResume() {
        super.onResume()
        val activity = activity as AppCompatActivity?
        activity!!.supportActionBar!!.show()
        activity.supportActionBar!!.title=fragment_farm_tv_farmname.text
        val coordinatorLayout: CoordinatorLayout = activity.contentView as CoordinatorLayout
        coordinatorLayout.fitsSystemWindows=true
        coordinatorLayout.invalidate()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val farmProfileViewModel = FarmProfileViewModel(activity!!.application)
        farmProfileViewModel.init(arguments?.getLong("farmId")!!)

        val farm = farmProfileViewModel.getFarm()
        val product = farmProfileViewModel.getProduct()
        val recyclerViewAdapter = FarmRecyclerViewAdapter(product)
        val horizontalLayoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        val isFavorites = farmProfileViewModel.isFavorite()

        isFavorites.observeForever {
            Log.e("tag","it")
            fragment_farm_detail_favorite.isChecked=it!!
        }

        fragment_farm_recycler_view.layoutManager=horizontalLayoutManager
        fragment_farm_recycler_view.adapter=recyclerViewAdapter
        fragment_farm_image.setImageResource(R.drawable.cover)
        fragment_farm_ratingbar.numStars=farm.rating
        fragment_farm_tv_farmname.text=farm.name
        fragment_farm_tv_addr.text=farm.addr
        fragment_farm_tv_producername.text=farm.producerName
        fragment_farm_tv_desc.text=farm.description

        fragment_farm_navigation.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=${farm.Latitude},${farm.Longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            startActivity(mapIntent)
        }


        fragment_farm_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager = fragment_farm_recycler_view.layoutManager as LinearLayoutManager
                val adapter = fragment_farm_recycler_view.adapter as FarmRecyclerViewAdapter
                val fistVisibleItem = manager.findFirstCompletelyVisibleItemPosition()
                val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
                if (fistVisibleItem==0){
                    fragment_farm_arrow_left.visibility=View.GONE
                }
                else {
                    fragment_farm_arrow_left.visibility=View.VISIBLE
                }
                if(lastVisibleItem==adapter.itemCount-1){
                    fragment_farm_arrow_right.visibility=View.GONE
                }
                else{
                    fragment_farm_arrow_right.visibility=View.VISIBLE
                }
            }
        })

        fragment_farm_detail_favorite.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                farmProfileViewModel.addFavorite()
            }
            else{
                farmProfileViewModel.removeFavorite()
            }
        }

    }
}