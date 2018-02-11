package locavoreplatine.locafarm.view.fragment

import android.arch.lifecycle.*
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
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
import kotlinx.android.synthetic.main.fragment_finder.*
import kotlinx.android.synthetic.main.fragment_finder_content_map.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.di.Injection
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.util.*
import locavoreplatine.locafarm.util.CheckUtility.canGetLocation
import locavoreplatine.locafarm.view.viewAdapter.FinderRecyclerViewAdapter
import locavoreplatine.locafarm.viewModel.FarmProfileViewModel
import locavoreplatine.locafarm.viewModel.FinderViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.util.concurrent.TimeUnit


class FinderFragment : Fragment(), LifecycleOwner, OnMapReadyCallback, AnkoLogger {

    private lateinit var finderViewModel: FinderViewModel

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var farmsMap: GoogleMap

    private var location: Location? = null

    private lateinit var onFarmItemClickListener: OnFarmItemClickListener


    private lateinit var disposable: CompositeDisposable
    private lateinit var locationProvider: ReactiveLocationProvider
    private val request = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER)
            .setNumUpdates(5)
            .setInterval(100)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_finder_floating_search_view.setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener {
            override fun onFocus() {
                Log.d("1", "onFocus()")
                fragment_finder_mapview.visibility = View.GONE
            }

            override fun onFocusCleared() {
                Log.d("1", "onFocusCleared()")
                fragment_finder_mapview.visibility = View.VISIBLE
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disposable = CompositeDisposable()
        locationProvider = ReactiveLocationProvider(context)
        viewModelFactory = Injection.provideViewModelFactory(activity!!.application)
        finderViewModel = ViewModelProviders.of(this, viewModelFactory).get(FinderViewModel::class.java)


        onFarmItemClickListener = object : OnFarmItemClickListener {
            override fun onItemClick(item: FarmModel) {
                val mActivity = activity as AppCompatActivity

                val fragment = FarmProfileFragment()
                val bundle = Bundle()
                bundle.putLong("farmId", item.farmId)
                fragment.arguments = bundle

                mActivity.replaceFragment(fragment, mActivity.activity_main_fragment_container.id)
            }
        }


        fragment_finder_mapview.setOnTouchListener { _, event ->

            info("Touchhhhh")
            when (event.action) {
                MotionEvent.ACTION_BUTTON_PRESS -> {
                    info("GONE")
                    fragment_finder_floating_search_view.visibility = View.GONE
                }
                MotionEvent.ACTION_BUTTON_RELEASE -> {
                    info("Visible")
                    fragment_finder_floating_search_view.visibility = View.VISIBLE
                }
            }
            false
        }


        //Finder RecyclerView
        fragment_finder_recycler_view.layoutManager = LinearLayoutManager(context)
        fragment_finder_recycler_view.itemAnimator = DefaultItemAnimator()
        fragment_finder_recycler_view.adapter = FinderRecyclerViewAdapter(getSearchResult(), onFarmItemClickListener)

        fragment_finder_mapview.onCreate(savedInstanceState)

        fragment_finder_mapview.getMapAsync(this)
    }

    private fun getSearchResult(): List<FarmModel> {
        val farmViewModel = ViewModelProviders.of(this, viewModelFactory).get(FarmProfileViewModel::class.java)
        val items: MutableList<FarmModel> = mutableListOf()
        items.addAll(farmViewModel.all())
        return items
    }


    override fun onStart() {
        super.onStart()

        if (fragment_finder_mapview != null) {
            fragment_finder_mapview.onStart()
        }

        //SearchView
        disposable.add(rxSearchView(fragment_finder_floating_search_view).debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .filter { t: String -> t.length > 1 && !(t.startsWith(' ')) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    info("New word " + it)
                }
                .delay(700, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .map { it ->
                    info("Finder ViewModel " + it + " Length " + it.length)
                    finderViewModel.findFarmsNames(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    info("Show result")
                    showResult(finderViewModel.getFarms())
                }
        )

        if (CheckUtility.checkFineLocationPermission(context!!.applicationContext) && canGetLocation(context!!.applicationContext)) {
            disposable.add(locationProvider.getUpdatedLocation(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        farmsMap.uiSettings.isMyLocationButtonEnabled = false
                        error("Android reactive location error" + it.toString())
                    }
                    .subscribe { t ->

                        location = t
                        farmsMap.isMyLocationEnabled = true
                        farmsMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(t.latitude, t.longitude), DEFAULT_ZOOM))
                        // farmsMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(t.latitude, t.longitude), DEFAULT_ZOOM ))
                        // farmsMap.uiSettings.isMyLocationButtonEnabled = true
                    }
            )
        }
    }


    override fun onResume() {

        if (fragment_finder_mapview != null) {
            fragment_finder_mapview.onResume()
        }

        super.onResume()

        val activity = activity as AppCompatActivity?

        if (activity != null) {
            activity.supportActionBar!!.hide()
        }
    }


    override fun onPause() {

        if (::farmsMap.isInitialized) {
            val mgr = MapStateManager(activity!!.baseContext, MAPS_NAME)
            mgr.saveMapState(farmsMap)
        }
        super.onPause()

        if (fragment_finder_mapview != null) {
            fragment_finder_mapview.onPause()
        }

    }


    override fun onStop() {

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onStop()

        if (fragment_finder_mapview != null) {
            fragment_finder_mapview.onStop()
        }
    }

    override fun onDestroy() {

        if (!disposable.isDisposed) {
            disposable.dispose()
        }

        super.onDestroy()

        if (fragment_finder_mapview != null) {
            fragment_finder_mapview.onDestroy()
        }

    }

    override fun onDestroyView() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }

        super.onDestroyView()

        if (fragment_finder_mapview != null) {
            fragment_finder_mapview.onDestroy()
        }

        info("onDestroy view  A")
    }


    override fun onLowMemory() {
        super.onLowMemory()
        fragment_finder_mapview.onLowMemory()
    }


    private fun rxSearchView(searchView: FloatingSearchView): Observable<String> {

        val subject = PublishSubject.create<String>()

        searchView.setOnQueryChangeListener { oldQuery, newQuery ->
            //get suggestions based on newQuery

            if (!newQuery.isEmpty() && oldQuery != newQuery) {
                info("New query" + newQuery)
                subject.onNext(newQuery)
            } else {

                fragment_finder_recycler_view.adapter = FinderRecyclerViewAdapter(getSearchResult(), onFarmItemClickListener)
                updateFarmMarkers(getSearchResult())
                farmsMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude, location!!.longitude), DEFAULT_ZOOM))
            }
        }

        return subject
    }

    private fun showResult(result: LiveData<List<FarmModel>>) {
        result.observe(this, Observer<List<FarmModel>> { farms ->
            if (farms != null && farms.isNotEmpty()) {
                //context!!.toast(result.value!![0].toString())
                farms.sortedWith(compareBy { it.name })
                val itemsArray: ArrayList<Any> = ArrayList()
                itemsArray.addAll(result.value!!.sortedWith(compareBy({ it.javaClass.toString() })))
                fragment_finder_recycler_view.adapter = FinderRecyclerViewAdapter(itemsArray, onFarmItemClickListener)
                updateFarmMarkers(farms)

                val best = farms.first()
                farmsMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(best.Latitude, best.Longitude), FINDER_ZOOM))

            } else {
                //context!!.toast("No result found !!!")
            }
        })
    }

    private fun updateFarmMarkers(farms: List<FarmModel>) {

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
            if(fragment_finder_floating_search_view!=null) {
                fragment_finder_floating_search_view.visibility = View.GONE
            }
        }

        farmsMap.setOnCameraIdleListener {
            if(fragment_finder_floating_search_view!=null) {
                fragment_finder_floating_search_view.visibility = View.VISIBLE
            }
        }

    }

    companion object {
        private const val DEFAULT_ZOOM = 10.0f
        private const val FINDER_ZOOM = 15.0f
        private const val MAPS_NAME = "FINDER"
        private val MAPVIEW_BUNDLE_KEY = "FINDER_MAP"
    }

}