package locavoreplatine.locauser.view

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.user_card_row.view.*
import locavoreplatine.locafarm.model.UserModel

/**
 * Created by sparow on 14/01/18.
 */

class UserHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
    //2
    private var view: View = v
    private var user: UserModel? = null

    //3
    init {
        v.setOnClickListener(this)
    }

    //4
    override fun onClick(v: View) {
//            val context = itemView.context
//            val showUserIntent = Intent(context, UserActivity::class.java)
//            showUserIntent.putExtra(PHOTO_KEY, user)
//            context.startActivity(showUserIntent)
    }

    fun bindUser(user: UserModel) {

        view.user_name.text = user.firstName
    }

    companion object {
        //5
        private val USER_KEY = "USER"
    }
}