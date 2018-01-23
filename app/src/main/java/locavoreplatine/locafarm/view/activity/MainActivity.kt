package locavoreplatine.locafarm.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_content.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.util.replaceFragment
import locavoreplatine.locafarm.view.fragment.FarmProfileFragment
import locavoreplatine.locafarm.view.fragment.FinderFragment
import org.jetbrains.anko.AnkoLogger


class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
        //todo remove when put in production
//        AppDatabase.TEST_MODE=true

        val fragment = FinderFragment()
        replaceFragment(fragment, activity_main_fragment_container.id)
//        val fragment=FarmProfileFragment()
//        val bundle = Bundle()
//        bundle.putInt("id",0)
//        fragment.arguments=bundle
//        replaceFragment(fragment,activity_main_fragment_container.id)
        permissionCheck()
    }



    private fun permissionCheck() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertFine()
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_FINE_LOCATION)

            }
        }
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AlertCoarse()
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_COARSE_LOCATION)

            }
        }
    }

    private fun AlertCoarse() {
        val alertdialog = AlertDialog.Builder(this).create()
        alertdialog.setTitle("Permission Access_Coarse_Location")
        alertdialog.setMessage("Cette permission est nécessaire pour prendre la géolocalisation et au bon fonctionnement de l'application")
        alertdialog.setButton(AlertDialog.BUTTON_NEGATIVE, "non") { dialog, which ->
            dialog.dismiss()
            finish()
        }
        alertdialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok ") { dialog, which ->
            dialog.dismiss()
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), MainActivity.REQUEST_CODE_COARSE_LOCATION)
        }
        alertdialog.show()
    }


    private fun AlertFine() {
        val alertdialog = AlertDialog.Builder(this).create()
        alertdialog.setTitle("Permission Access_Fine_Location")
        alertdialog.setMessage("Cette permission est nécessaire pour prendre la géolocalisation et au bon fonctionnement de l'application")
        alertdialog.setButton(AlertDialog.BUTTON_NEGATIVE, "non") { dialog, which ->
            dialog.dismiss()
            finish()
        }
        alertdialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok ") { dialog, which ->
            dialog.dismiss()
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MainActivity.REQUEST_CODE_FINE_LOCATION)
        }
        alertdialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_FINE_LOCATION -> {
                run {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
                        if (showRationale) {
                            AlertFine()
                        } else {
                            finish()
                        }
                    }
                }
                run {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
                        if (showRationale) {
                            AlertCoarse()
                        } else {
                            finish()
                        }
                    }
                }
            }
        }
    }


    companion object {

        const val MY_PREFS_NAME = "MyPrefsFile"
        private const val REQUEST_CODE_FINE_LOCATION = 101
        private const val REQUEST_CODE_COARSE_LOCATION = 102
    }




}
