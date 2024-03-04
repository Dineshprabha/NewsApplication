package com.dineshprabha.newsapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshprabha.newsapplication.Data.datasource.APIService
import com.dineshprabha.newsapplication.Data.model.Article
import com.dineshprabha.newsapplication.Utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(application: Application) : AndroidViewModel(application = application)  {

    private val apiService = APIService()

    var isAscendingOrder = true // Flag to track sorting order

    private val _articleList = MutableLiveData<NetworkResult<List<Article>>>()
    val articleList : LiveData<NetworkResult<List<Article>>> = _articleList

    fun fetchNews(){
        // Show loading state
        _articleList.value = NetworkResult.Loading ()
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO){
                    apiService.getNews()
                }
                _articleList.value = NetworkResult.Success(result!!.articles)
            }catch (e:Exception){
                _articleList.value = NetworkResult.Error(e.message ?: "An error occurred")
            }
        }
    }

    // Other code
    fun toggleSortingOrder() {
        isAscendingOrder = !isAscendingOrder
        // Refresh the article list to apply sorting changes
        fetchNews()
    }

    //sorting the article based on date
    fun getSortedArticles(articles: List<Article>?): List<Article> {
        return articles?.let {
            if (isAscendingOrder) {
                it.sortedBy { article -> article.publishedAt }
            } else {
                it.sortedByDescending { article -> article.publishedAt }
            }
        } ?: emptyList()
    }
}