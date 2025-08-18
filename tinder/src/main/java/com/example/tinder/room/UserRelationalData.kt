package com.example.tinder.room

import androidx.room.Embedded
import androidx.room.Relation


data class UserWithCurrentUsers(
    @Embedded val user: UserProfileEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val currentUsers: List<CurrentUsers>
)

data class UserWithReviews(
    @Embedded val user: UserProfileEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val reviews: List<UserReviews>
)

data class UserFullProfile(
    @Embedded val user: UserProfileEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val currentUsers: List<CurrentUsers>,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val reviews: List<UserReviews>
)
