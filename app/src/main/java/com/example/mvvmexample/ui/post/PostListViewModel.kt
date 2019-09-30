package com.example.mvvmexample.ui.post

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.mvvmexample.R
import com.example.mvvmexample.base.BaseViewModel
import com.example.mvvmexample.model.Post
import com.example.mvvmexample.model.PostDao
import com.example.mvvmexample.network.PostApi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post_list.view.*
import java.util.*
import javax.inject.Inject

class PostListViewModel(private val postDao: PostDao): BaseViewModel(){
    @Inject
    lateinit var postApi: PostApi

    private lateinit var subscription: Disposable // Kaldirilabilir demek
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadPosts() }
    val postListAdapter: PostListAdapter = PostListAdapter()

    init {
        loadPosts()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose() // Will called when view model is called.
    }

    private fun loadPosts(){
        // Without Database
        /*subscription = postApi.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result -> onRetrievePostListSuccess(result) },
                { onRetrievePostListError() }
            )
         */
        // With Room database
        subscription = Observable.fromCallable { postDao.all }
            .concatMap {
                dbPostList ->
                if (dbPostList.isEmpty()) {
                    // Webden çek
                    postApi.getPosts().concatMap {
                        // Local Database'e ekle
                        apiPostList -> postDao.insertAll(*apiPostList.toTypedArray())
                        // eklediklerini dön
                        Observable.just(apiPostList)
                    }
                } else {
                    // Database'de var onları dön
                    Observable.just(dbPostList)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{onRetrievePostListStart()}
            .doOnTerminate{onRetrievePostListFinish()}
            .subscribe(
                {result -> onRetrievePostListSuccess(result)},
                {onRetrievePostListError()}
            )
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(postList: List<Post>) {
        postListAdapter.updatePostList(postList)
    }

    private fun onRetrievePostListError() {
        errorMessage.value = R.string.post_error
    }
}