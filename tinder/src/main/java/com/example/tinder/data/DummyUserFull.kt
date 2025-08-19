package com.example.tinder.data

data class DummyUserFull(
    val id: Int,
    val name: String,
    val age: Int,
    val city: String,
    val photoUris: List<PhotoSource>,
    val aboutMe: String,
    val dateOfBirth: Int,
    val lastActive: String,
    val likes: Int,
    val superLikes: Int,
    val boosts: Int
)


