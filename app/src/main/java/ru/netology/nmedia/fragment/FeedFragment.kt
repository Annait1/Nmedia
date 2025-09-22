package ru.netology.nmedia.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractorListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel


class FeedFragment : Fragment() {

    val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        /*        viewModel.view()*/


        /* val editPostLauncher = registerForActivityResult(EditPostResultContract()) { newContent ->
             if (newContent == null) {
                 viewModel.cancelEdit()
                 return@registerForActivityResult
             }*/

        /*     val text = newContent.trim()
             if (text.isBlank()) {
                 viewModel.cancelEdit()
                 return@registerForActivityResult
             }
             viewModel.changeContent(newContent.trim())
             viewModel.save()
         }*/


        val adapter = PostAdapter(object : OnInteractorListener {
            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.share(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }


            override fun onEdit(post: Post) {
                viewModel.edit(post)
                /*  editPostLauncher.launch(post.content)*/
                findNavController().navigate(
                R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply {
                    textAgrs = post.content
                    putLong("postId", post.id)
                }
                )
            }


            override fun onOpenVideo(url: String) {
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    //поменяла на requireContext(),было this@MAinActivity
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onOpen(post: Post) {
                val args = Bundle().apply { putLong("postId", post.id) }
                findNavController().navigate(R.id.singlePostFragment, args)
            }

        }

        )

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val isNew = posts.size != adapter.itemCount
            adapter.submitList(posts) {
                if (isNew) {
                    binding.list.smoothScrollToPosition(0)

                }


            }

        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root


    }

    companion object {
        var Bundle.textAgrs by StringArg
    }
}


/* было до viewModel.edited.observe(this) { post ->
      binding.editGroup.visibility =
          if (post.id != 0L) View.VISIBLE else View.GONE
      if (post.id != 0L) {
          binding.content.setText(post.content)
          binding.content.setSelection(binding.content.text.length)
          binding.content.requestFocus()
      } else {
          binding.content.text.clear()
          binding.content.clearFocus()
          AndroidUtils.hideKeyboard(binding.content)

      }
  }*/

/*    with(binding) {
        save.setOnClickListener {
            if (content.text.isNullOrBlank()) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.error_empty_content,
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            viewModel.changeContent(content.text.toString())
            viewModel.save()
            content.setText("")
            content.clearFocus()
            AndroidUtils.hideKeyboard(content)
        }
    }*/

/* binding.cancelEdit.setOnClickListener {
     viewModel.cancelEdit()
     AndroidUtils.hideKeyboard(binding.content)
 }*/



