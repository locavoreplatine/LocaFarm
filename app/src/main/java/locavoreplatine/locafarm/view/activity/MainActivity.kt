package locavoreplatine.locafarm.view.activity

import android.Manifest
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_content.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.util.CheckUtility.requestLocation
import locavoreplatine.locafarm.util.replaceFragment
import locavoreplatine.locafarm.view.fragment.FavoritesFragment
import locavoreplatine.locafarm.view.fragment.FinderFragment
import org.jetbrains.anko.AnkoLogger

import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity(),  EasyPermissions.PermissionCallbacks, AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_bottom_nav_view.inflateMenu(R.menu.activity_main_bottom_menu)
        activity_main_bottom_nav_view.setOnNavigationItemSelectedListener {
            val fragment: Fragment =
                    when (it.itemId) {
                        R.id.activity_main_bottom_menu_fav -> {
                            FavoritesFragment()
//                        HomeFragment()
                        }
                        else -> {
                            FinderFragment()
                        }
                    }
            replaceFragment(fragment, activity_main_fragment_container.id)
            true
        }
        activity_main_bottom_nav_view.setOnNavigationItemReselectedListener {
            val fragment: Fragment =
                    when (it.itemId) {
                        R.id.activity_main_bottom_menu_fav -> {
                            FavoritesFragment()
//                        HomeFragment()
                        }
                        else -> {
                            FinderFragment()
                        }
                    }
            replaceFragment(fragment, activity_main_fragment_container.id)
        }


        // Request enable location
        requestLocation(this)

        val fragment = FinderFragment()
        replaceFragment(fragment, activity_main_fragment_container.id)


        checkPermissions()
    }

    private fun checkPermissions(): Boolean {
        if (!EasyPermissions.hasPermissions(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
            EasyPermissions.requestPermissions(this, "Geolocation and writing permissions are necessary for the proper functioning of the application", REQUEST_CODE_FINE_LOCATION ,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount == 1) {
            finish()
        } else {
            if (backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }
        }
    }

    companion object {

        const val MY_PREFS_NAME = "MyPrefsFile"
        private const val REQUEST_CODE_FINE_LOCATION = 101
        private const val REQUEST_CODE_COARSE_LOCATION = 102

    }

}