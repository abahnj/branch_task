package dev.abahnj.branchtask.ui.threadList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.abahnj.branchtask.data.model.ChatListResponse
import dev.abahnj.branchtask.databinding.MeMessageBinding
import dev.abahnj.branchtask.databinding.OtherMessageBinding

class ChatAdapter:
    ListAdapter<ChatListResponse, RecyclerView.ViewHolder>(ChatsDiffCallback()) {
    private val viewTypeMessageSent = 1
    private val viewTypeMessageReceived = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            viewTypeMessageSent ->SentChatsViewHolder(MeMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
           else ->ReceivedChatsViewHolder(OtherMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder) {
            when (itemViewType) {
                viewTypeMessageSent -> (this as SentChatsViewHolder).bind(item)
                viewTypeMessageReceived -> (this as ReceivedChatsViewHolder).bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.agent_id == null) viewTypeMessageReceived else  viewTypeMessageSent
    }


    inner class ReceivedChatsViewHolder(binding: OtherMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        private val userId: TextView = binding.userIdOther
        private var currentChat: ChatListResponse? = null
        private val body: TextView = binding.textMessageOther
        private val timeStamp : TextView = binding.dateOther

        fun bind(chat: ChatListResponse) {
            currentChat = chat
            userId.text = chat.id.toString()
            body.text = chat.body
            timeStamp.text = chat.timestamp

        }
    }

    inner class SentChatsViewHolder(binding: MeMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        private val userId: TextView = binding.userIdMe
        private var currentChat: ChatListResponse? = null
        private val body: TextView = binding.textMessageMe
        private val timeStamp : TextView = binding.dateMe

        fun bind(chat: ChatListResponse) {
            currentChat = chat
            userId.text = chat.id.toString()
            body.text = chat.body
            timeStamp.text = chat.timestamp

            with(itemView) {
                tag = currentChat
                setOnClickListener { itemView ->
                    val item = itemView.tag as ChatListResponse
                    val action = ThreadsFragmentDirections.actionChatListFragmentToChatDetailFragment(item.id)

                    findNavController().navigate(action)

                }

            }
        }
    }

}

class ChatsDiffCallback : DiffUtil.ItemCallback<ChatListResponse>() {
    override fun areItemsTheSame(oldItem: ChatListResponse, newItem: ChatListResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatListResponse, newItem: ChatListResponse): Boolean {
        return oldItem == newItem
    }
}
