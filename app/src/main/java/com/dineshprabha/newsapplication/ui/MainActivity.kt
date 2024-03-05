package com.dineshprabha.newsapplication.ui

import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dineshprabha.newsapplication.Data.model.Article
import com.dineshprabha.newsapplication.viewmodel.NewsViewModel
import com.dineshprabha.newsapplication.R
import com.dineshprabha.newsapplication.Utils.NetworkChecker
import com.dineshprabha.newsapplication.adapter.NewsAdapter
import com.dineshprabha.newsapplication.databinding.ActivityMainBinding

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val newsViewModel: NewsViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NewsViewModel::class.java)
    }

    private val networkChecker by lazy {
        NetworkChecker(getSystemService(ConnectivityManager::class.java ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.main_fragment)

        NavigationUI.setupWithNavController(binding.btmNav, navController)
    }

}