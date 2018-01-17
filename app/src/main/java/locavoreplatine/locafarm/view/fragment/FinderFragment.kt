package locavoreplatine.locafarm.view.fragment

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arlib.floatingsearchview.FloatingSearchView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_finder.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.di.Injection
import locavoreplatine.locafarm.model.UserModel
import locavoreplatine.locafarm.view.viewAdapter.FinderRecyclerViewAdapter
import locavoreplatine.locafarm.viewModel.FinderViewModel
import locavoreplatine.locafarm.viewModel.UserProfileViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

class FinderFragment : Fragment(), LifecycleOwner, AnkoLogger {

    private lateinit var viewFinderModel: FinderViewModel

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var disposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finder, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelFactory = Injection.provideViewModelFactory(activity!!.application)
        viewFinderModel = ViewModelProviders.of(this, viewModelFactory).get(FinderViewModel::class.java)

        //RecyclerView
        fragment_finder_recycler_view.layoutManager = LinearLayoutManager(context)
        fragment_finder_recycler_view.hasFixedSize()
        fragment_finder_recycler_view.itemAnimator = DefaultItemAnimator()
        fragment_finder_recycler_view.adapter = FinderRecyclerViewAdapter(getSampleArrayList())
    }

    private fun getSampleArrayList(): ArrayList<Any> {
        val viewUserModel = ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel::class.java)
        val items: ArrayList<Any> = ArrayList()
        items.addAll(viewUserModel.all())
        info(viewUserModel.all())
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
                    info("new word")
                }
                .delay(700, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .map { it ->
                    info("finder viewModel " + it + "length " + it.length)
                    viewFinderModel.findUsersNames(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    info("show result")
                    showResult(viewFinderModel.getUsers())
                }
    }

    override fun onStop() {
        super.onStop()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun rxSearchView(searchView: FloatingSearchView): Observable<String> {

        val subject = PublishSubject.create<String>()

        searchView.setOnQueryChangeListener { oldQuery, newQuery ->
            //get suggestions based on newQuery
            if (!newQuery.isEmpty()) {
                info("new query" + newQuery)
                subject.onNext(newQuery)
            } else {
                fragment_finder_recycler_view.adapter = FinderRecyclerViewAdapter(getSampleArrayList())
            }
        }

        return subject
    }

    private fun showResult(result: LiveData<List<UserModel>>) {

        result.observe(this, Observer<List<UserModel>> { users ->
            if (users != null && users.isNotEmpty()) {
                context!!.toast(result.value!![0].toString())
                val itemsArray: ArrayList<Any> = ArrayList()
                itemsArray.addAll(result.value!!.sortedWith(compareBy({ it.javaClass.toString() })))
                fragment_finder_recycler_view.adapter = FinderRecyclerViewAdapter(itemsArray)
            }
        })
    }

}