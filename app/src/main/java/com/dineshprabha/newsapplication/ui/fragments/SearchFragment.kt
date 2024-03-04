package com.dineshprabha.newsapplication.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dineshprabha.newsapplication.Data.model.Article
import com.dineshprabha.newsapplication.R
import com.dineshprabha.newsapplication.adapter.NewsAdapter
import com.dineshprabha.newsapplication.databinding.FragmentSearchBinding
import com.dineshprabha.newsapplication.ui.MainActivity
import com.dineshprabha.newsapplication.viewmodel.NewsViewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
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
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRV()
        setUpObserver()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterArticles(newText)
                return true
            }
        })
    }


    private fun setUpObserver() {
        newsViewModel.articleList.observe(viewLifecycleOwner, Observer { result ->
            // Update the adapter with the new list of articles
            if (result.data != null){
                val articleList: List<Article>? = result.data
                newsAdapter.differ.submitList(newsViewModel.getSortedArticles(articleList))
            }
        })
    }

    private fun setUpRV() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = newsAdapter
        }
    }
    private fun filterArticles(query: String?) {
        newsViewModel.getSortedArticles(query).let { filteredList ->
            newsAdapter.differ.submitList(filteredList)
        }
    }

}