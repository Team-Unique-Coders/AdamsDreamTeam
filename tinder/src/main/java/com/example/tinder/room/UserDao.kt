package com.example.tinder.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow



@Dao
interface UserDao {

    @Query("SELECT * FROM UserProfile")
    fun getAllProfiles(): Flow<List<UserProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentUser(user: CurrentUsers): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserReview(review: UserReviews)
    @Insert
    suspend fun insertProfile(profile: UserProfileEntity)

    @Delete
    suspend fun deleteUserProfile(user: UserProfileEntity)

}
