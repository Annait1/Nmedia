package ru.netology.pusher

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import java.io.FileInputStream


fun main() {
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(FileInputStream("fcm.json")))
        .build()

    FirebaseApp.initializeApp(options)
    val token = ""
    /*val message = Message.builder()
        .putData("action", "share")
        .putData(
            "content", """{
          "userId": 1,
          "userName": "Vasiliy",
          "postId": 2,
          "postAuthor": "Netology"
        }""".trimIndent()
        )
        .setToken(token)
        .build()*/

    val newPost = Message.builder()
        .putData("action", "NEW_POST")
        .putData("content", """{
          "id": 101,
      "author": "Василий",
      "published": "сегодня",
      "content": "Всем привет! Как дела?",
      "likes": 0,
      "likedByMe": false,
      "video": null,
      "shares": 0,
      "views": 0
        }""".trimIndent())
        .setToken(token)
        .build()

    FirebaseMessaging.getInstance().send(newPost)
}