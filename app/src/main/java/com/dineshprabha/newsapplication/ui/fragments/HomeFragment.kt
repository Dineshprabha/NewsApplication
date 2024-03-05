package com.dineshprabha.newsapplication.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dineshprabha.newsapplication.Data.model.Article
import com.dineshprabha.newsapplication.R
import com.dineshprabha.newsapplication.Utils.NetworkChecker
import com.dineshprabha.newsapplication.Utils.NetworkResult
import com.dineshprabha.newsapplication.adapter.NewsAdapter
import com.dineshprabha.newsapplication.databinding.FragmentHomeBinding
import com.dineshprabha.newsapplication.ui.MainActivity
import com.dineshprabha.newsapplication.viewmodel.NewsViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding ?= null
    private val binding get() = _binding!!

    private lateinit var newsAdapter: NewsAdapter
    lateinit var newsViewModel: NewsViewModel
    private lateinit var networkChecker: NetworkChecker

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkChecker = NetworkChecker(requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

        //setup recyclerview to display news articles
        setUpRV()

        //setup observer to article data from api
        setUpObserver()
        checkNetworkAndFetchNews()

        // Set up click listener for filter icon
        binding.filter.setOnClickListener {
            // Trigger sorting of articles
            newsViewModel.toggleSortingOrder()

            // Update icon based on sorting order
            if (newsViewModel.isAscendingOrder) {
                binding.filter.setImageResource(R.drawable.sort_desc) // Use ascending icon
            } else {
                binding.filter.setImageResource(R.drawable.sort_aces) // Use descending icon
            }
        }

        // Set up SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            checkNetworkAndFetchNews()
        }

    }

    private fun setUpObserver() {
        newsViewModel.articleList.observe(viewLifecycleOwner, Observer {
            binding.llNetworkStatus.visibility = View.GONE
            binding.layoutMain.visibility = View.VISIBLE
            when(it){
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvNews.visibility = View.GONE
                }
                is NetworkResult.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.rvNews.visibility = View.VISIBLE
                    if (it.data != null){
                        val articleList: List<Article>? = it.data
                        newsAdapter.differ.submitList(newsViewModel.getSortedArticles(articleList))
                    }
                }
                is NetworkResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvNews.visibility = View.GONE
                }
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkNetworkAndFetchNews() {
        //to verify the internet connection
        if (networkChecker.hasValidInternetConnection()) {
            newsViewModel.fetchNews()
        } else {
            // Show "No Internet Connection" message
            binding.llNetworkStatus.visibility = View.VISIBLE
            binding.layoutMain.visibility = View.GONE
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setUpRV() {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = newsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}