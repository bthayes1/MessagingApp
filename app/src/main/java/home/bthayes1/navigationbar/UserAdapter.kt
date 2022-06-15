package home.bthayes1.navigationbar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    val context: Context,
    val userList: MutableList<User>,
    val onImageClickListener: imageClickListener
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    interface imageClickListener {
        fun onImageClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(context)
        val view = ViewHolder(inflator.inflate(R.layout.user_item, parent, false))
        return view
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = userList[position]
        holder.bind(item)
        holder.ivProfilePic.setOnClickListener {
            onImageClickListener.onImageClick(position)
        }
    }

    override fun getItemCount() = userList.size

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        private val tvEmail = itemview.findViewById<TextView>(R.id.tvEmail)
        private val tvPassword = itemview.findViewById<TextView>(R.id.tvPassword)
        val ivProfilePic = itemview.findViewById<ImageView>(R.id.ivProfilePic)
        fun bind(item: User) {
            tvEmail.text = item.email
            tvPassword.text = item.password
            if (item.uri != null) {
                ivProfilePic.setImageURI(item.uri)
            } else {
                ivProfilePic.setImageResource(R.drawable.profile_pic)
            }
        }
    }
}
