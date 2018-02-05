package locavoreplatine.locafarm.view.fragment

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arlib.floatingsearchview.FloatingSearchView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.fragment_fav.*
import kotlinx.android.synthetic.main.fragment_fav_content_map.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.di.Injection
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.util.FarmMarkerInfos
import locavoreplatine.locafarm.util.OnFarmItemClickListener
import locavoreplatine.locafarm.util.replaceFragment
import locavoreplatine.locafarm.view.viewAdapter.FinderRecyclerViewAdapter
import locavoreplatine.locafarm.viewModel.FarmProfileViewModel
import locavoreplatine.locafarm.viewModel.FavViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit


class FavFragment : Fragment(), LifecycleOwner,OnMapReadyCallback, AnkoLogger {

    private lateinit var favViewModel: FavViewModel

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var disposable: Disposable

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    private lateinit var farmsMap: GoogleMap

    private lateinit var onFarmItemClickListener: OnFarmItemClickListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_fav_floating_search_view.setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener {
            override fun onFocus() {
                Log.d("1", "onFocus()")
                fragment_fav_mapview.visibility=View.GONE
            }

            override fun onFocusCleared() {
                Log.d("1", "onFocusCleared()")
                fragment_fav_mapview.visibility=View.VISIBLE
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelFactory = Injection.provideViewModelFactory(activity!!.application)
        favViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavViewModel::class.java)


        onFarmItemClickListener = object : OnFarmItemClickListener {
            override fun onItemClick(item: FarmModel) {
                val mActivity = activity as AppCompatActivity

                val fragment=FarmProfileFragment()
                val bundle = Bundle()
                bundle.putLong("farmId",item.farmId)
                fragment.arguments=bundle

                mActivity.replaceFragment(fragment,mActivity.activity_main_fragment_container.id)
            }
        }

        //Finder RecyclerView
        fragment_fav_recycler_view.layoutManager = LinearLayoutManager(context)
        fragment_fav_recycler_view.itemAnimator = DefaultItemAnimator()
        fragment_fav_recycler_view.adapter = FinderRecyclerViewAdapter(getSearchResult(),onFarmItemClickListener)

        //MapsView
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        var mapViewBundle: Bundle? = null

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        fragment_fav_mapview.onCreate(mapViewBundle)
        fragment_fav_mapview.getMapAsync(this)
    }

    private fun getSearchResult(): List<FarmModel> {
        val farmViewModel = ViewModelProviders.of(this, viewModelFactory).get(FarmProfileViewModel::class.java)
        val items: MutableList<FarmModel> = mutableListOf()
        items.addAll(farmViewModel.all())
        return items
    }

    override fun onStart() {
        super.onStart()
        //SearchView
        disposable = rxSearchView(fragment_fav_floating_search_view).
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
                    favViewModel.findFav()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    info("Show result")
                    showResult(favViewModel.getFarms())
                }
        fragment_fav_mapview.onStart()
    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        if(fragment_fav_mapview != null){
            fragment_fav_mapview.onStop()
        }
        super.onStop()
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        if(fragment_fav_mapview != null) {
            fragment_fav_mapview.onDestroy()
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
                fragment_fav_recycler_view.adapter = FinderRecyclerViewAdapter(getSearchResult(),onFarmItemClickListener)
                updateFarmMarkers(getSearchResult())
            }
        }

        return subject
    }

    private fun showResult(result: LiveData<List<FarmModel>>) {
        result.observe(this, Observer<List<FarmModel>> { farms ->
            if (farms != null && farms.isNotEmpty()) {
                //context!!.toast(result.value!![0].toString())
                val itemsArray: ArrayList<Any> = ArrayList()
                itemsArray.addAll(result.value!!.sortedWith(compareBy({ it.javaClass.toString() })))
                fragment_fav_recycler_view.adapter = FinderRecyclerViewAdapter(itemsArray,onFarmItemClickListener)
                    updateFarmMarkers(farms)
            }else{
                //context!!.toast("No result found !!!")
            }
        })
    }

    private fun updateFarmMarkers(farms: List<FarmModel>){
        if(::farmsMap.isInitialized) {
            farmsMap.clear()
            farms.forEach {
                val customInfoWindow = FarmMarkerInfos(context)
                farmsMap.setInfoWindowAdapter(customInfoWindow)
                val markerOptions = MarkerOptions().position(LatLng(it.Latitude, it.Longitude)).title(it.name).snippet(it.addr)
                val m: Marker = farmsMap.addMarker(markerOptions)
                m.tag = it
                m.showInfoWindow()
            }
        }
    }

    override fun onMapReady(mapM: GoogleMap) {
        farmsMap = mapM
        updateFarmMarkers(getSearchResult())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }

        if(fragment_fav_mapview != null){
            fragment_fav_mapview.onSaveInstanceState(mapViewBundle)
        }
    }


}