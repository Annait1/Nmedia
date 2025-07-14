package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.formatCount


class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.view()


        viewModel.data.observe(this) { post ->

            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published


                likesCount.text = formatCount(post.likes)
                sharesCount.text = formatCount(post.shares)
                viewsCount.text = formatCount(post.views)

                root.setOnClickListener {
                    println("root")
                }

                if (post.likeByMe) {
                    likesButton.setImageResource(R.drawable.baseline_favorite_24)
                }


                binding.likesButton.setOnClickListener {
                    viewModel.like()
                }
                binding.sharesButton.setOnClickListener {
                    viewModel.share()
                }

            }
        }
    }
}

