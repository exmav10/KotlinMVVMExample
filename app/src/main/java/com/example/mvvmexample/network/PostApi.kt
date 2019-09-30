package com.example.mvvmexample.network

import io.reactivex.Observable
import com.example.mvvmexample.model.Post
import retrofit2.http.GET


interface PostApi {
    @GET("/posts")
    fun getPosts(): Observable<List<Post>>
}