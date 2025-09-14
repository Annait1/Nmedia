package ru.netology.nmedia.activity

import android.content.Intent
import android.widget.Toast
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractorListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.formatCount
import ru.netology.nmedia.util.AndroidUtils


class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*        viewModel.view()*/



        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { content ->
            content ?: return@registerForActivityResult
            viewModel.changeContent(content)
            viewModel.save()
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch(Unit)
        }

        val editPostLauncher = registerForActivityResult(EditPostResultContract()) { newContent ->
            newContent ?: return@registerForActivityResult
            viewModel.changeContent(newContent.trim())
            viewModel.save()
        }


        val adapter = PostAdapter(object : OnInteractorListener {
            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                /*viewModel.share(post.id)*/
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
                editPostLauncher.launch(post.content)
            }

        }

        )

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val isNew = posts.size != adapter.itemCount
            adapter.submitList(posts) {
                if (isNew) {
                    binding.list.smoothScrollToPosition(0)

                }
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
    }

}
