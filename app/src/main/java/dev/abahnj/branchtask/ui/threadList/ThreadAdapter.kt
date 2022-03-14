package dev.abahnj.branchtask.ui.threadList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.abahnj.branchtask.data.model.ChatListResponse
import dev.abahnj.branchtask.databinding.ThreadListContentBinding

class ThreadAdapter:
    ListAdapter<ChatListResponse, ThreadAdapter.ThreadsViewHolder>(ThreadsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadsViewHolder {
        val binding =
            ThreadListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ThreadsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThreadsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    inner class ThreadsViewHolder(binding: ThreadListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        private val userId: TextView = binding.userId
        private var currentChat: ChatListResponse? = null
        private val body: TextView = binding.body
        private val timeStamp : TextView = binding.timestamp



        /* Bind flower name and image. */
        fun bind(chat: ChatListResponse) {
            currentChat = chat
            userId.text = chat.id.toString()
            body.text = chat.body
            timeStamp.text = chat.timestamp

            with(itemView) {
                tag = currentChat
                setOnClickListener { itemView ->
                    val item = itemView.tag as ChatListResponse
                    val action = ThreadsFragmentDirections.actionChatListFragmentToChatDetailFragment(item.threadId)

                    findNavController().navigate(action)

                }

            }
        }
    }

}

class ThreadsDiffCallback : DiffUtil.ItemCallback<ChatListResponse>() {
    override fun areItemsTheSame(oldItem: ChatListResponse, newItem: ChatListResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatListResponse, newItem: ChatListResponse): Boolean {
        return oldItem == newItem
    }
}
