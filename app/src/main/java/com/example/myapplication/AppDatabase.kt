package com.example.myapplication

import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.DAO.PersonDAO
import com.example.myapplication.Entity.PersonEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(PersonEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDAO

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val personDao = database.personDao()

                    // Add some people
                    personDao.insert(PersonEntity(name = "Donald Trump", picture = BitmapFactory.decodeResource(Application().resources, R.drawable.trump)))
                    personDao.insert(PersonEntity(name = "Donald Trump jr", picture = BitmapFactory.decodeResource(Application().resources, R.drawable.trumpjr)))
                    personDao.insert(PersonEntity(name = "Donald Trump", picture = BitmapFactory.decodeResource(Application().resources, R.drawable.trump)))
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
