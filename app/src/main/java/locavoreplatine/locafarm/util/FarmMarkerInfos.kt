package locavoreplatine.locafarm.util

import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import android.app.Activity
import kotlinx.android.synthetic.main.fragment_finder_marker_infos.view.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.model.FarmModel


/**
 * Created by aoudia on 30/01/18.
 */


class FarmMarkerInfos(private val context: Context?): GoogleMap.InfoWindowAdapter{



    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

    override fun getInfoContents(p0: Marker?): View {

        val view = (context as Activity).layoutInflater.inflate(R.layout.fragment_finder_marker_infos, null)

        view.farm_marker_name.text = p0?.title
        view.farm_marker_address.text = p0?.snippet

        val infoWindowData = p0?.tag as FarmModel

        view.farm_marker_producer.text = infoWindowData.producerName
        view.farm_marker_description.text = infoWindowData.description

        return view
    }


}