package dev.abahnj.branchtask.ui.threadList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.abahnj.branchtask.R
import dev.abahnj.branchtask.util.getViewModelFactory

class ChatList : Fragment() {

    companion object {
        fun newInstance() = ChatList()
    }

    private val  viewModel  by viewModels<ThreadViewModel> { getViewModelFactory()  }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_list_fragment, container, false)
    }


}