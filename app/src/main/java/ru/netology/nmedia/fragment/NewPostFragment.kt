package ru.netology.nmedia.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.fragment.FeedFragment.Companion.textAgrs
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.addCallback


class NewPostFragment : Fragment() {

    val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        val prefs = requireContext().getSharedPreferences("draft", Context.MODE_PRIVATE)
        val isEditing = arguments?.containsKey("postId") == true



        arguments?.textAgrs?.let(binding.edit::setText)

        if (!isEditing) {
            val draft = prefs.getString("draft_text", null)


            if (!draft.isNullOrBlank() && binding.edit.text.isNullOrBlank()) {
                binding.edit.setText(draft)

                binding.edit.setSelection(draft.length)
            }
        }


        binding.edit.requestFocus()
        binding.save.setOnClickListener {
            if (binding.edit.text.isNotBlank()) {
                val content = binding.edit.text.toString()
                viewModel.changeContent(content)
                viewModel.save()


                if (!isEditing) {
                    prefs.edit().remove("draft_text").apply()
                }
            }
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!isEditing) {
                val text = binding.edit.text.toString().trim()
                val e = prefs.edit()
                if (text.isNotBlank()) {
                    e.putString("draft_text", text)
                } else {
                    e.remove("draft_text")
                }
                e.apply()
            }
            findNavController().navigateUp()
        }

        return binding.root
    }
}