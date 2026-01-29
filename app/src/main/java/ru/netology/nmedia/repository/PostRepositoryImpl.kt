package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.toEntity

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {
    override fun getAll(): LiveData<List<Post>> {
        return dao.getAll().map { list ->
            list.map {
                it.toDto()
            }

        }
    }

    override fun likebyId(id: Long) {
        dao.likeById(id)
    }

    override fun share(id: Long) {
        dao.share(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        /* dao.save(PostEntity.fromDto(post))*/
        dao.save(post.toEntity())
    }
}