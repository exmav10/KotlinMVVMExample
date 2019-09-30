package com.example.mvvmexample.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvmexample.model.Post
import com.example.mvvmexample.model.PostDao

@Database(entities = arrayOf(Post::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun postDao(): PostDao
}