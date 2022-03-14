package dev.abahnj.branchtask.ui.threadList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import dev.abahnj.branchtask.data.model.NewChatRequest
import dev.abahnj.branchtask.databinding.FragmentItemDetailBinding
import dev.abahnj.branchtask.util.getViewModelFactory

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ChatListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ChatDetailFragment : Fragment() {

    private val  viewModel  by activityViewModels<ThreadViewModel> { getViewModelFactory()  }

    private var _binding: FragmentItemDetailBinding? = null
    private val adapter = ChatAdapter()

    private val binding get() = _binding!!

    private val args: ChatDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView: RecyclerView = binding.itemList
        viewModel.getChatsByThreadId(args.chatId)

        binding.sendButton.setOnClickListener {
            val editText = binding.chatTextField.editText!!
        if (editText.text.isNotEmpty())
            viewModel.createChat(NewChatRequest(args.chatId, editText.text.toString()))
            editText.setText("")
        }

        setupRecyclerView(recyclerView)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView
    ) {
        viewModel.threadIdChats.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}