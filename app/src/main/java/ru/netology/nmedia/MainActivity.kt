package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.formatCount


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb"
        )

        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published
            likesCount.text = post.likes.toString()

            likesCount.text = formatCount(post.likes)
            sharesCount.text = formatCount(post.shares)
            viewsCount.text = formatCount(post.views)

            root.setOnClickListener {
                println("root")
            }

            if (post.likeByMe) {
                likesButton.setImageResource(R.drawable.baseline_favorite_24)
            }
            likesButton.setOnClickListener {
                println("likes")


                if (!post.likeByMe) {
                    post.likeByMe = true
                    post.likes += 1

                    likesButton.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    post.likeByMe = false
                    post.likes -= 1

                    likesButton.setImageResource(R.drawable.outline_favorite_24)
                }
                likesCount.text = formatCount(post.likes)
            }

            sharesButton.setOnClickListener {
                post.shares +=1
                sharesCount.text = formatCount(post.shares)
            }

            avatar.setOnClickListener {
                println("avatar")
            }



            /*enableEdgeToEdge()
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }*/

        }

    }
}