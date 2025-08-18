package com.example.tinder.room

//
//suspend fun populateDummyData(db: UserDatabase) {
//
//    db.clearAllTables()
//
//    val dummyUsersFull = listOf(
//        DummyUserFull(2, "Adam Doe", 25, "New York", R.drawable.adam, "I love coding", 1998, "2025-08-17", 2, 5, 1),
//        DummyUserFull(3, "Adam Smith", 30, "Los Angeles", R.drawable.adam01, "Music fan", 1993, "2025-08-17", 0, 3, 0),
//        DummyUserFull(4, "Adam Johnson", 47, "Chicago", R.drawable.adam02, "Traveler", 1976, "2025-08-17", 1, 2, 1),
//        DummyUserFull(5, "Adam Brown", 16, "Houston", R.drawable.adam03, "High school student", 2007, "2025-08-17", 0, 0, 0),
//        DummyUserFull(6, "Adam Wilson", 12, "Miami", R.drawable.adam04, "Loves soccer", 2011, "2025-08-17", 0, 1, 0),
//        DummyUserFull(7, "Adam Lee", 19, "San Francisco", R.drawable.adam05, "College student", 2004, "2025-08-17", 1, 1, 0),
//        DummyUserFull(8, "Adam Davis", 11, "Seattle", R.drawable.adam06, "Plays video games", 2012, "2025-08-17", 0, 0, 0),
//        DummyUserFull(9, "Adam Taylor", 32, "Boston", R.drawable.adam07, "Coffee enthusiast", 1991, "2025-08-17", 3, 4, 2),
//        DummyUserFull(10, "Adam Clark", 31, "Dallas", R.drawable.adam08, "Loves reading", 1992, "2025-08-17", 2, 3, 1),
//        DummyUserFull(11, "Adam Garcia", 27, "Denver", R.drawable.adam09, "Fitness fan", 1996, "2025-08-17", 1, 2, 0),
//        DummyUserFull(12, "Adam Martinez", 26, "Atlanta", R.drawable.adam10, "Traveler", 1997, "2025-08-17", 0, 3, 1),
//        DummyUserFull(13, "Adam Hernandez", 42, "Phoenix", R.drawable.adam11, "Chef", 1981, "2025-08-17", 2, 1, 0),
//        DummyUserFull(14, "Adam Rodriguez", 29, "San Diego", R.drawable.adam12, "Nature lover", 1994, "2025-08-17", 0, 2, 0),
//        DummyUserFull(15, "Adam Lee", 37, "Philadelphia", R.drawable.adam13, "Tech enthusiast", 1986, "2025-08-17", 1, 2, 0),
//        DummyUserFull(16, "Adam Moore", 23, "San Jose", R.drawable.adam14, "Gamer", 2002, "2025-08-17", 0, 1, 0),
//        DummyUserFull(17, "Adam Hall", 21, "Austin", R.drawable.adam15, "Music producer", 2004, "2025-08-17", 1, 3, 1),
//        DummyUserFull(18, "Adam Turner", 51, "Portland", R.drawable.adam16, "Retired engineer", 1974, "2025-08-17", 2, 1, 0),
//        DummyUserFull(19, "Adam Scott", 45, "Orlando", R.drawable.adam17, "Theme park lover", 1980, "2025-08-17", 1, 2, 0),
//        DummyUserFull(20, "Adam Adams", 9, "Washington", R.drawable.adam18, "Student", 2016, "2025-08-17", 0, 0, 0),
//        DummyUserFull(21, "Adam Baker", 22, "Nashville", R.drawable.adam19, "Musician", 2003, "2025-08-17", 1, 1, 0),
//        DummyUserFull(22, "Adam Nelson", 35, "Charlotte", R.drawable.adam20, "Entrepreneur", 1990, "2025-08-17", 2, 4, 1),
//        DummyUserFull(23, "Adam Carter", 32, "Indianapolis", R.drawable.adam21, "Runner", 1993, "2025-08-17", 0, 3, 0),
//        DummyUserFull(24, "Adam Sanchez", 33, "Columbus", R.drawable.adam22, "Artist", 1992, "2025-08-17", 1, 2, 0),
//        DummyUserFull(25, "Adam Perez", 34, "Detroit", R.drawable.adam23, "Coffee lover", 1991, "2025-08-17", 0, 2, 1)
//    )
//
//
//
//    val currentUserId = db.userDao().insertCurrentUser(
//        CurrentUsers(joinDate = System.currentTimeMillis().toString())
//    ).toInt()
//
//    dummyUsersFull.forEach { user ->
//        db.userDao().insertUserProfile(
//            UserProfileEntity(
//                profileId = 0,
//                userId = currentUserId,
//                userName = user.name,
//                userAge = user.age,
//                city = user.city,
//                aboutMe = user.aboutMe,
//                dateOfBirth = user.dateOfBirth,
//                photos = user.photo.toString(),
//                createdTime = System.currentTimeMillis().toString(),
//                boosts = user.boosts,
//                likes = user.likes,
//                superLikes = user.superLikes
//            )
//        )
//
//        db.userDao().insertUserReview(
//            UserReviews(
//                userId = currentUserId,
//                review = "This is a dummy review for ${user.name}",
//                rating = (3..5).random(),
//                blocks = 0
//            )
//        )
//    }
//}
//
//data class DummyUserFull(
//    val id: Int,
//    val name: String,
//    val age: Int,
//    val city: String,
//    val photo: Int,
//    val aboutMe: String,
//    val dateOfBirth: Int,
//    val createdTime: String,
//    val boosts: Int,
//    val likes: Int,
//    val superLikes: Int
//)
