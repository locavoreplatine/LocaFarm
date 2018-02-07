package locavoreplatine.locafarm

import shortbread.Shortbread
import android.app.Application



/**
 * Created by toulouse on 07/02/18.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Shortbread.create(this)
    }
}