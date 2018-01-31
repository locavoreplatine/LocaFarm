package locavoreplatine.locafarm.view.fragment

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.fragment_fav.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.util.OnFarmItemClickListener
import locavoreplatine.locafarm.util.replaceFragment
import locavoreplatine.locafarm.view.viewAdapter.FavRecyclerViewAdapter
import locavoreplatine.locafarm.viewModel.FavoritesViewModel
import org.jetbrains.anko.AnkoLogger

/**
 * Created by toulouse on 31/01/18.
 */
class FavFragment : Fragment(), LifecycleOwner, AnkoLogger {

    private lateinit var onFarmItemClickListener: OnFarmItemClickListener

    private lateinit var disposable: CompositeDisposable

    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fav,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoritesViewModel = FavoritesViewModel(activity!!.application)
        favoritesViewModel.setUser(arguments?.getLong("userId")!!)

        val itemList = emptyList<FarmModel>()

        Log.e("je passe ici","")

        onFarmItemClickListener = object : OnFarmItemClickListener {
            override fun onItemClick(item: FarmModel) {
                val mActivity = activity as AppCompatActivity

                val fragment=FarmProfileFragment()
                val bundle = Bundle()
                Log.e("click_listener","${item.farmId}")
                bundle.putLong("farmId",item.farmId)
                //TODO replace with real userId when available
                bundle.putLong("userId",arguments?.getLong("userId")!!)
                fragment.arguments=bundle

                mActivity.replaceFragment(fragment,mActivity.activity_main_fragment_container.id)
            }
        }

        fragment_fav_recycler_view.layoutManager = LinearLayoutManager(context)
        fragment_fav_recycler_view.itemAnimator = DefaultItemAnimator()
        fragment_fav_recycler_view.adapter = FavRecyclerViewAdapter(itemList,onFarmItemClickListener)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposable = CompositeDisposable()
    }

    override fun onStart() {
        super.onStart()
        disposable.add(favoritesViewModel
                .getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    fragment_fav_recycler_view.adapter = FavRecyclerViewAdapter(it!!,onFarmItemClickListener)
                }
        )
    }

    override fun onStop() {
        super.onStop()
        if(!disposable.isDisposed)
            disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!disposable.isDisposed)
            disposable.dispose()
    }



}