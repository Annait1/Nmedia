package ru.netology.nmedia.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractorListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.databinding.FragmentSinglePostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class SinglePostFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSinglePostBinding.inflate(inflater, container, false)
        val postId = requireArguments().getLong("postId")
        val card = binding.post


        val listener = object : OnInteractorListener {
            override fun onOpen(post: Post) {


            }

            override fun onLike(post: Post) = viewModel.like(post.id)
            override fun onShare(post: Post) = viewModel.share(post.id)
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
                findNavController().navigateUp()
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(


                    R.id.action_singlePostFragment_to_newPostFragment,
                    Bundle().apply {
                        putLong("postId", post.id)
                        putString("textAgrs", post.content)
                    }


                )
            }

            override fun onOpenVideo(url: String) {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                } catch (e: ActivityNotFoundException) {

                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            posts.firstOrNull { it.id == postId }?.let { post ->
                PostViewHolder.bind(card, post, listener)


            }
        }
        return binding.root
    }
}
