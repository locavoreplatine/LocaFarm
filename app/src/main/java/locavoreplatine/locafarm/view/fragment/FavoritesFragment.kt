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
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.fragment_fav.*
import kotlinx.android.synthetic.main.fragment_fav_content_map.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.di.Injection
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.util.*
import locavoreplatine.locafarm.view.viewAdapter.FinderRecyclerViewAdapter
import locavoreplatine.locafarm.viewModel.FavoriteViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.util.concurrent.TimeUnit


class FavoritesFragment : Fragment(), LifecycleOwner,OnMapReadyCallback, AnkoLogger {

    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var farmsMap: GoogleMap

    private lateinit var onFarmItemClickListener: OnFarmItemClickListener

    private lateinit var disposable: CompositeDisposable
    private lateinit var locationProvider: ReactiveLocationProvider
    private val request = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER)
            .setNumUpdates(5)
            .setInterval(100)


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
        disposable = CompositeDisposable()
        locationProvider = ReactiveLocationProvider(context)
        viewModelFactory = Injection.provideViewModelFactory(activity!!.application)
        favoriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoriteViewModel::class.java)


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

        fragment_fav_mapview.onCreate(savedInstanceState)
        fragment_fav_mapview.getMapAsync(this)
    }

    private fun getSearchResult(): List<FarmModel> {
        val favViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoriteViewModel::class.java)
        val items: MutableList<FarmModel> = mutableListOf()
        items.addAll(favViewModel.favoritesSingle())
        return items
    }

    override fun onStart() {
        fragment_fav_mapview.onStart()
        super.onStart()
        //SearchView
        disposable.add(rxSearchView(fragment_fav_floating_search_view).
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
                    favoriteViewModel.findFav()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    info("Show result")
                    showResult(favoriteViewModel.getFarms())
                })

        if (CheckUtility.checkFineLocationPermission(context!!.applicationContext) && CheckUtility.canGetLocation(context!!.applicationContext)) {
            disposable.add(locationProvider.getUpdatedLocation(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        farmsMap.uiSettings.isMyLocationButtonEnabled = false
                        error("Android reactive location error" + it.toString())
                    }
                    .subscribe { t ->
                        farmsMap.isMyLocationEnabled = true
                        farmsMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(t.latitude, t.longitude), DEFAULT_ZOOM))
                        farmsMap.uiSettings.isMyLocationButtonEnabled = true
                    }
            )}
    }


    override fun onPause() {

            val mgr = MapStateManager(activity!!.baseContext, MAPS_NAME)
            mgr.saveMapState(farmsMap)

        super.onPause()

        if(fragment_fav_mapview != null){
            fragment_fav_mapview.onPause()
        }
    }


    override fun onResume() {
        if(fragment_fav_mapview != null){
            fragment_fav_mapview.onResume()
        }

        super.onResume()

        val activity = activity as AppCompatActivity?

        if (activity != null) {
            activity.supportActionBar!!.hide()
        }
    }


    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }

        super.onStop()

        if(fragment_fav_mapview != null){
            fragment_fav_mapview.onStop()
        }
    }


    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }

        super.onDestroy()

        if(fragment_fav_mapview != null) {
            fragment_fav_mapview.onDestroy()
        }

    }

    override fun onDestroyView() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroyView()

        if(fragment_fav_mapview != null) {
            fragment_fav_mapview.onDestroy()
        }
    }


    override fun onLowMemory() {
        super.onLowMemory()
        fragment_fav_mapview.onLowMemory()
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





    override fun onMapReady(mapM: GoogleMap) {
        farmsMap = mapM

        val mgr = MapStateManager(context!!.applicationContext, MAPS_NAME)
        val position = mgr.savedCameraPosition

        if (position != null) {
            val update = CameraUpdateFactory.newCameraPosition(position)
            farmsMap.moveCamera(update)
            farmsMap.mapType = mgr.savedMapType
        }

        updateFarmMarkers(getSearchResult())

        farmsMap.setOnMapClickListener {
            if (fragment_fav_floating_search_view!= null){
                fragment_fav_floating_search_view.visibility = View.GONE
            }

        }

        farmsMap.setOnCameraIdleListener{
            if (fragment_fav_floating_search_view!= null){
            fragment_fav_floating_search_view.visibility = View.VISIBLE
            }
        }


    }

    companion object {

        private val MAPVIEW_BUNDLE_KEY = "FAV_MAP"
        private const val MAPS_NAME = "FVORITES"
        private const val DEFAULT_ZOOM = 0.0f
    }

}