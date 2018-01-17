package locavoreplatine.locauser.view

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.fragment_finder_card_row_user.view.*
import locavoreplatine.locafarm.model.UserModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by sparow on 14/01/18.
 */

class UserViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener, AnkoLogger {

    private var view: View = v
    private var user: UserModel? = null

    init {
        v.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        info(USER_KEY)
//            val context = itemView.context
//            val showUserIntent = Intent(context, UserActivity::class.java)
//            showUserIntent.putExtra(PHOTO_KEY, user)
//            context.startActivity(showUserIntent)
    }

    fun bindUser(user: UserModel) {
        view.user_name.text = user.firstName
    }

    companion object {
        private val USER_KEY = "USER"
    }
}