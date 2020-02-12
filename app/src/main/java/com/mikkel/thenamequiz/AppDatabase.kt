package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.DAO.PersonDAO
import com.example.myapplication.DAO.ScoreDao
import com.example.myapplication.Entity.PersonEntity
import com.example.myapplication.Entity.ScoreEntity
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(PersonEntity::class, ScoreEntity::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDAO
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `ScoreEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , `name` TEXT NOT NULL, " +
                        "`score` INTEGER NOT NULL)")
            }
        }

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
//                    .addCallback(AppDatabaseCallback(scope))
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}


