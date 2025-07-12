package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int = 999,
    var likeByMe: Boolean = false,
    var shares: Int = 200,
    var views: Int = 150_000

)

fun formatCount(count: Int): String {
    return when (count) {
        in 0..999 -> count.toString()
        in 1000..9999 -> {
            val thousands = count / 1000
            val hundreds = (count % 1000) / 100
            "${thousands}.${hundreds}K"
        }

        in 10_000..999_999 -> {
            val thousands = count / 1000
            "${thousands}K"
        }

        else -> {
            val millions = count / 1_000_000
            val hundredThousands = (count % 1_000_000) / 100_000
            "${millions}.${hundredThousands}M"
        }
    }

}




