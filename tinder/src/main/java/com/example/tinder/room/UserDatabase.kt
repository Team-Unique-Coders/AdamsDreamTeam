package com.example.tinder.room


//
//@Database(
//    entities = [UserProfileEntity::class, UserReviews::class, CurrentUsers::class],
//    version = 1, // you can keep this as 1 for now
//    exportSchema = false
//)
//abstract class UserDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: UserDatabase? = null
//
//        fun getInstance(context: Context, scope: CoroutineScope): UserDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    UserDatabase::class.java,
//                    "user_database"
//                )
//                    .fallbackToDestructiveMigration() // ✅ wipes + rebuilds when schema mismatches
//                    .addCallback(UserDatabaseCallback(scope))
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//
//        private class UserDatabaseCallback(
//            private val scope: CoroutineScope
//        ) : RoomDatabase.Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                INSTANCE?.let { database ->
//                    scope.launch {
//                        populateDummyData(database)
//                    }
//                }
//            }
//        }
//    }
//}
//
