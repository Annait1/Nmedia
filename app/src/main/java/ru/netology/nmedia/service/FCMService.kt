package ru.netology.nmedia.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import kotlin.random.Random


class FCMService : FirebaseMessagingService() {
    /* private val action = "action"
     private val content = "content"*/
    private val gson = Gson()
    private val channelId = "remote"


    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(token: String) {
        Log.i("fcm_token", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val actionValue = message.data["action"]
        if (actionValue.isNullOrBlank()) {
            Log.w("FCM", getString(R.string.missing_action, message.data.toString()))
            return
        }

        val action = try {
            Action.valueOf(actionValue.uppercase())
        } catch (_: IllegalArgumentException) {
            Action.UNKNOWN
        }

        when (action) {
            Action.LIKE -> {
                val raw = message.data["content"]
                val like = try {
                    gson.fromJson(raw, ActionLike::class.java)
                } catch (e: Exception) {
                    Log.e("FCM", getString(R.string.content_error, raw))
                    return
                }
                handleLike(like)
            }

            Action.NEW_POST -> {
                val raw = message.data["content"]
                val post = try {
                    gson.fromJson(raw, Post::class.java)
                } catch (e: Exception) {
                    Log.e("FCM", getString(R.string.content_error, raw))
                    return
                }
                handleNewPost(post)
            }

            Action.UNKNOWN -> {
                Log.w("FCM", getString(R.string.unknown_action, actionValue))
                return
            }
        }
    }

    private fun handleLike(like: ActionLike) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    like.userName,
                    like.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        )
            return
        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleNewPost(post: Post) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_new_post_title, post.author
                )
            )
            .setStyle(NotificationCompat.BigTextStyle().bigText(post.content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        NotificationManagerCompat.from(this)
            .notify(post.id.toInt(), notification)
    }
}

enum class Action {
    LIKE,
    UNKNOWN,
    NEW_POST
}


data class ActionLike(
    val userId: Int,
    val userName: String,
    val postId: Int,
    val postAuthor: String,
)


