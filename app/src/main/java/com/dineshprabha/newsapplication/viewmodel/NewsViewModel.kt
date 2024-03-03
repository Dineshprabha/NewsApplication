package com.dineshprabha.newsapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshprabha.newsapplication.Data.datasource.APIService
import com.dineshprabha.newsapplication.Data.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(application: Application) : AndroidViewModel(application = application)  {

    private val apiService = APIService()

    private var isAscendingOrder = true // Flag to track sorting order

    private val _articleList = MutableLiveData<List<Article>>()
    val articleList : LiveData<List<Article>> = _articleList

    fun fetchNews(){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                apiService.getNews()
            }
            _articleList.value = result?.articles
        }
    }

    // Other code

    fun toggleSortingOrder() {
        isAscendingOrder = !isAscendingOrder
        // Refresh the article list to apply sorting changes
        fetchNews()
    }
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