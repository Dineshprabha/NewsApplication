package com.dineshprabha.newsapplication

import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dineshprabha.newsapplication.Data.model.Article
import com.dineshprabha.newsapplication.Data.viewmodel.NewsViewModel
import com.dineshprabha.newsapplication.Utils.NetworkChecker
import com.dineshprabha.newsapplication.adapter.NewsAdapter
import com.dineshprabha.newsapplication.databinding.ActivityMainBinding

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    lateinit var newsViewModel: NewsViewModel

    private val networkChecker by lazy {
        NetworkChecker(getSystemService(ConnectivityManager::class.java ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRV()

        newsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NewsViewModel::class.java)

        newsViewModel.fetchNews()

        setUpObserver()

    }

    private fun setUpObserver() {
        newsViewModel.articleList.observe(this, Observer {
            val articleList: List<Article>? = it
            newsAdapter.differ.submitList(articleList)
        })
    }

    private fun setUpRV() {
        newsAdapter = NewsAdapter()
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = newsAdapter
        }
    }
}