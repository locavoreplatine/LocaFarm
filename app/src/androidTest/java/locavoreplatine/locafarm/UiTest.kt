package locavoreplatine.locafarm


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.arlib.floatingsearchview.FloatingSearchView
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.Favorites
import locavoreplatine.locafarm.view.activity.MainActivity
import locavoreplatine.locafarm.view.viewHolder.FarmViewHolder
import locavoreplatine.locafarm.view.viewHolder.ProductViewHolder
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by damien on 19/02/2018.
 */
@RunWith(AndroidJUnit4::class)
class UiTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    private var lastItemForTest = 0

    @Before
    fun setup() {
        activityTestRule.activity.fragmentManager.beginTransaction()
        val appDatabase = AppDatabase.getInstance(activityTestRule.activity.application)
        appDatabase.favoritesDao().insert(Favorites(appDatabase.farmDao().findFarmByName("Le rayon fruits et légumes du Panier VertAu Panier Vert").blockingFirst()[0].farmId))
        appDatabase.favoritesDao().insert(Favorites(appDatabase.farmDao().findFarmByName("La Flamanderie - Ferme DEMAN").blockingFirst()[0].farmId))
        lastItemForTest = appDatabase.farmProductDao().productByFarm(appDatabase.farmDao().findFarmByName("Le drive fermier de Lomme").blockingFirst()[0].farmId).size
    }

    @Test
    fun testFavSwitch(){
        onView(withId(R.id.activity_main_bottom_menu_fav)).perform(click())
        onView(withId(R.id.fragment_fav_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_fav_floating_search_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_fav_mapview)).check(matches(isDisplayed()))
    }

    @Test
    fun testHomeSwitch() {
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.fragment_finder_floating_search_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_finder_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_finder_mapview)).check(matches(isDisplayed()))
    }
    @Test
    fun testSearch(){
        onView(withId(R.id.activity_main_bottom_menu_fav)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.fragment_finder_floating_search_view)).perform(click())
        onView(withId(R.id.fragment_finder_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_finder_mapview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.fragment_finder_floating_search_view)).perform(setSearchText())
        onView(isRoot()).perform(waitFor(5000))
        onView(withId(R.id.fragment_finder_recycler_view)).check(matches(hasDescendant(withText("La Flamanderie - Ferme DEMAN"))))
        onView(withId(R.id.fragment_finder_recycler_view)).check(matches(not(hasDescendant(withText("Le rayon fruits et légumes du Panier VertAu Panier Vert")))))
    }

    @Test
    fun goToFarm(){
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_fav)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.fragment_finder_recycler_view)).perform(RecyclerViewActions.actionOnItem<FarmViewHolder>(hasDescendant(withText("Le drive fermier de Lomme")), click()))
        onView(withId(R.id.fragment_farm_tv_farmname)).check(matches(isDisplayed()))
    }

    @Test
    fun testFarmSliderBase(){
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_fav)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.fragment_finder_recycler_view)).perform(RecyclerViewActions.actionOnItem<FarmViewHolder>(hasDescendant(withText("Le drive fermier de Lomme")), click()))
        onView(withId(R.id.fragment_farm_arrow_left)).check(matches(not(isDisplayed())))
        onView(withId(R.id.fragment_farm_arrow_right)).check(matches(isDisplayed()) )
    }

    @Test
    fun testFarmSliderMoved(){
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_fav)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.fragment_finder_recycler_view)).perform(RecyclerViewActions.actionOnItem<FarmViewHolder>(hasDescendant(withText("Le drive fermier de Lomme")), click()))
        onView(withId(R.id.fragment_farm_recycler_view)).perform(RecyclerViewActions.scrollToPosition<ProductViewHolder>(2))
        onView(withId(R.id.fragment_farm_arrow_left)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_farm_arrow_right)).check(matches(isDisplayed()))
    }

    @Test
    fun testFarmSliderLast(){
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_fav)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.fragment_finder_recycler_view)).perform(RecyclerViewActions.actionOnItem<FarmViewHolder>(hasDescendant(withText("Le drive fermier de Lomme")), click()))
        onView(withId(R.id.fragment_farm_recycler_view)).perform(RecyclerViewActions.scrollToPosition<ProductViewHolder>(lastItemForTest-1))
        onView(withId(R.id.fragment_farm_arrow_left)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_farm_arrow_right)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testFavorites() {
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_fav)).perform(click())
        onView(withId(R.id.fragment_fav_recycler_view)).check(matches(not(hasDescendant(withText("Le drive fermier de Lomme")))))
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.fragment_finder_recycler_view)).perform(RecyclerViewActions.actionOnItem<FarmViewHolder>(hasDescendant(withText("Le drive fermier de Lomme")), click()))
        onView(withId(R.id.fragment_farm_detail_favorite)).check(matches(not(isChecked())))
        onView(withId(R.id.fragment_farm_detail_favorite)).perform(click())
        onView(withId(R.id.fragment_farm_detail_favorite)).check(matches(isChecked()))
        onView(withId(R.id.activity_main_bottom_menu_fav)).perform(click())
        onView(withId(R.id.fragment_fav_recycler_view)).check(matches(hasDescendant(withText("Le drive fermier de Lomme"))))
    }

    @Test
    fun testFarmToHome() {
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_fav)).perform(click())
        onView(withId(R.id.activity_main_bottom_menu_home)).perform(click())
        onView(withId(R.id.fragment_finder_recycler_view)).perform(RecyclerViewActions.actionOnItem<FarmViewHolder>(hasDescendant(withText("Le drive fermier de Lomme")), click()))
        onView(withContentDescription("Navigate up")).perform(click())
        onView(withId(R.id.fragment_finder_floating_search_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_finder_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_finder_mapview)).check(matches(isDisplayed()))
    }
    fun setSearchText(): ViewAction {
        return  object  : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(FloatingSearchView::class.java)
            }


            override fun getDescription(): String {
                return "whatever"
            }

            override fun perform(uiController: UiController, view: View) {
                val yourCustomView = view as FloatingSearchView
                yourCustomView.setSearchText("Flamanderie")
            }
        }

    }

    fun waitFor(millis: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $millis milliseconds."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadForAtLeast(millis)
            }
        }
    }
}

