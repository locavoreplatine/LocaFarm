package locavoreplatine.locafarm.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction, name:String) {
    beginTransaction().func().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(name).commit()
    executePendingTransactions()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    val backStateName = fragment::class.java.name
    val tag = backStateName
    val fragmentPopped = supportFragmentManager.popBackStackImmediate(backStateName, 0)
    if (!fragmentPopped && supportFragmentManager.findFragmentByTag(backStateName) == null) {
        supportFragmentManager.inTransaction({replace(frameId, fragment, tag)},backStateName)
        supportFragmentManager.executePendingTransactions()
    }
}