package com.example.tinder.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class CurrentUsers(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val joinDate: String
)

@Entity(
    tableName = "UserProfile",
    foreignKeys = [
        ForeignKey(
            entity = CurrentUsers::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = false)
    val profileId: Int = 0,
    val userId: Int,
    val userName: String,
    val userAge: Int,
    val city: String,
    val aboutMe: String,
    val dateOfBirth: Int,
    val photos: String,
    val createdTime: String,
    val boosts: Int,
    val likes: Int,
    val superLikes: Int
)

@Entity(
    tableName = "UserReview",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["profileId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class UserReviews(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val review: String,
    val rating: Int,
    val blocks: Int
)
