package ru.netology.nmedia.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity(tableName = "Post_Entity")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val video: String? = null,

    val shares: Int = 0,
    val views: Int = 0

) {
    fun toDto() = Post(
        id = id,
        author = author,
        published = published,
        content = content,
        likes = likes,
        likedByMe = likedByMe,
        video = video,
        shares = shares,
        views = views
    )
    companion object{
        fun fromDto(post: Post) = post.run {
          PostEntity(
              id = id,
              author = author,
              published = published,
              content = content,
              likes = likes,
              likedByMe = likedByMe,
              video = video,
              shares = shares,
              views = views
          )
        }
    }
}

fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    published = published,
    content = content,
    likes = likes,
    likedByMe = likedByMe,
    video = video,
    shares = shares,
    views = views
)





