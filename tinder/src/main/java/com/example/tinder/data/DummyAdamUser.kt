package com.example.tinder.data

import com.example.tinder.R


sealed class PhotoSource {
    data class DrawableRes(val resId: Int) : PhotoSource()
    data class UriString(val uri: String) : PhotoSource()
}
object DummyAdamUser {
    val dummyUsersFull = mutableListOf<DummyUserFull>()

    init {
        if (dummyUsersFull.none { it.id == 1 }) {
            dummyUsersFull.add(
                createUser(
                    id = 1,
                    name = "Adam",
                    age = 25,
                    city = "New York",
                    photoRes = R.drawable.adam23,
                    bottomText = "1999"
                )
            )
        }
    }

    fun createUser(
        id: Int,
        name: String,
        age: Int,
        city: String,
        photoRes: Int,
        bottomText: String,
        photoUris: List<PhotoSource> = listOf(PhotoSource.DrawableRes(photoRes))
    ): DummyUserFull {
        return DummyUserFull(
            id = id,
            name = name,
            age = age,
            city = city,
            photoUris = photoUris,
            aboutMe = "Hi, I'm $name",
            dateOfBirth = bottomText.toIntOrNull() ?: 0,
            lastActive = System.currentTimeMillis().toString(),
            likes = 0,
            superLikes = 0,
            boosts = 0
        )
    }

    fun saveOrUpdateUser(newUser: DummyUserFull) {
        val userIndex = dummyUsersFull.indexOfFirst { it.id == newUser.id }
        if (userIndex != -1) {
            val existing = dummyUsersFull[userIndex]
            dummyUsersFull[userIndex] = newUser.copy(
                id = existing.id,
                photoUris = if (newUser.photoUris.isNotEmpty()) newUser.photoUris else existing.photoUris
            )
        } else {
            dummyUsersFull.add(newUser)
        }
    }

    fun updateUserStat(stat: String, amount: Int = 1) {
        val userIndex = dummyUsersFull.indexOfFirst { it.id == 1 }
        if (userIndex != -1) {
            val user = dummyUsersFull[userIndex]
            val updatedUser = when (stat) {
                "likes" -> user.copy(likes = (user.likes + amount).coerceAtMost(100))
                "boosts" -> user.copy(boosts = user.boosts + amount)
                "superLikes" -> user.copy(superLikes = user.superLikes + amount)
                else -> user
            }
            dummyUsersFull[userIndex] = updatedUser
        }
    }

    fun getUserById(id: Int): DummyUserFull? {
        return dummyUsersFull.find { it.id == id }
    }
}
