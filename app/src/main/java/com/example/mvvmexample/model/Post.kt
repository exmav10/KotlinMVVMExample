package com.example.mvvmexample.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity // Room için ekledi, datayı kaydediyor - Saves data to database
data class Post(val userId: Int, @field:PrimaryKey val id: Int, val title: String, val body: String)