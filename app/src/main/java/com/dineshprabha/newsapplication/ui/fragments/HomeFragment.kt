package com.dineshprabha.newsapplication.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dineshprabha.newsapplication.Data.model.Article
import com.dineshprabha.newsapplication.R
import com.dineshprabha.newsapplication.adapter.NewsAdapter
import com.dineshprabha.newsapplication.databinding.FragmentHomeBinding
import com.dineshprabha.newsapplication.ui.MainActivity
import com.dineshprabha.newsapplication.viewmodel.NewsViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsAdapter: NewsAdapter
    lateinit var newsViewModel: NewsViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRV()
        newsViewModel.fetchNews()
        setUpObserver()

        // Set up click listener for filter icon
        binding.filter.setOnClickListener {
            // Trigger sorting of articles
            newsViewModel.toggleSortingOrder()
        }

    }

    private fun setUpObserver() {
        newsViewModel.articleList.observe(viewLifecycleOwner, Observer {
            val articleList: List<Article>? = it
            newsAdapter.differ.submitList(newsViewModel.getSortedArticles(articleList))
        })
    }

    private fun setUpRV() {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = newsAdapter
        }
    }
}