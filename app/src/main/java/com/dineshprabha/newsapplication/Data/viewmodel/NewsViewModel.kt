package com.dineshprabha.newsapplication.Data.viewmodel

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
}