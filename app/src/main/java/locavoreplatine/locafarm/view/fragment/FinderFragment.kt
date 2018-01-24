package locavoreplatine.locafarm.view.fragment

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arlib.floatingsearchview.FloatingSearchView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.fragment_finder.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.di.Injection
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.util.OnFarmItemClickListener
import locavoreplatine.locafarm.util.replaceFragment
import locavoreplatine.locafarm.view.viewAdapter.FinderRecyclerViewAdapter
import locavoreplatine.locafarm.viewModel.FarmProfileViewModel
import locavoreplatine.locafarm.viewModel.FinderViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit



class FinderFragment : Fragment(), LifecycleOwner,OnMapReadyCallback, AnkoLogger {

    private lateinit var viewFinderModel: FinderViewModel

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var disposable: Disposable


    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    private lateinit var onFarmItemClickListener: OnFarmItemClickListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finder, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelFactory = Injection.provideViewModelFactory(activity!!.application)
        viewFinderModel = ViewModelProviders.of(this, viewModelFactory).get(FinderViewModel::class.java)

        onFarmItemClickListener = object : OnFarmItemClickListener {
            override fun onItemClick(item: FarmModel) {
                val mActivity = activity as AppCompatActivity
                val fragment=FarmProfileFragment()
                val bundle = Bundle()
                bundle.putLong("id",item.farmId)
                fragment.arguments=bundle
                mActivity.replaceFragment(fragment,mActivity.activity_main_fragment_container.id)
            }
        }

        //Finder RecyclerView
        fragment_finder_recycler_view.layoutManager = LinearLayoutManager(context)
        fragment_finder_recycler_view.itemAnimator = DefaultItemAnimator()
        fragment_finder_recycler_view.adapter = FinderRecyclerViewAdapter(getSearchResult(),onFarmItemClickListener)

        //MapsView
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        var mapViewBundle: Bundle? = null

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mapview.onCreate(mapViewBundle)
        mapview.getMapAsync(this)
    }

    private fun getSearchResult(): ArrayList<Any> {
        val viewFarmModel = ViewModelProviders.of(this, viewModelFactory).get(FarmProfileViewModel::class.java)
        val items: ArrayList<Any> = ArrayList()

        items.addAll(viewFarmModel.all())
        info(viewFarmModel.all())

        return ArrayList(items.sortedWith(compareBy({ it.javaClass.toString() })))
    }

    override fun onStart() {
        super.onStart()
        //SearchView
        disposable = rxSearchView(fragment_finder_floating_search_view).
                debounce(300, TimeUnit.MILLISECONDS)
                .filter { t: String -> t.length > 1 && !(t.startsWith(' ')) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    info("New word "+it)
                }
                .delay(700, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .map { it ->
                    info("Finder ViewModel " + it + " Length " + it.length)
                    viewFinderModel.findFarmsNames(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    info("Show result")
                    showResult(viewFinderModel.getFarms())
                }
        mapview.onStart()
    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        if(mapview != null){
            mapview.onStop()
        }
        super.onStop()
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        if(mapview != null) {
            mapview.onDestroy();
        }
        super.onDestroy()
    }

    private fun rxSearchView(searchView: FloatingSearchView): Observable<String> {

        val subject = PublishSubject.create<String>()

        searchView.setOnQueryChangeListener { oldQuery, newQuery ->
            //get suggestions based on newQuery
            if (!newQuery.isEmpty()) {
                info("New query" + newQuery)
                subject.onNext(newQuery)
            } else {
                fragment_finder_recycler_view.adapter = FinderRecyclerViewAdapter(getSearchResult(),onFarmItemClickListener)
            }
        }

        return subject
    }

    private fun showResult(result: LiveData<List<FarmModel>>) {

        result.observe(this, Observer<List<FarmModel>> { farms ->
            if (farms != null && farms.isNotEmpty()) {
                context!!.toast(result.value!![0].toString())
                val itemsArray: ArrayList<Any> = ArrayList()
                itemsArray.addAll(result.value!!.sortedWith(compareBy({ it.javaClass.toString() })))
                fragment_finder_recycler_view.adapter = FinderRecyclerViewAdapter(itemsArray,onFarmItemClickListener)
            }else{
                context!!.toast("No result found !!!")
            }
        })
    }

    override fun onMapReady(map: GoogleMap) {
        map.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Marker"))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }

        if(mapview != null){
            mapview.onSaveInstanceState(mapViewBundle)
        }
    }


}