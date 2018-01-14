package locavoreplatine.locafarm.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_content.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.util.replaceFragment
import locavoreplatine.locafarm.view.fragment.UserProfileFragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.bundleOf


class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
        //todo remove when put in production
//        AppDatabase.TEST_MODE=true
        //todo remove when put in production
        val testUserProfileFragment = UserProfileFragment()
        testUserProfileFragment.arguments = bundleOf(Pair("id",0))

        replaceFragment(testUserProfileFragment,main_activityfragment_container.id)


    }

}
