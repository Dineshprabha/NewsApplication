package com.dineshprabha.newsapplication.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dineshprabha.newsapplication.Data.model.Article
import com.dineshprabha.newsapplication.databinding.ArticleListItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding : ArticleListItemBinding):
            RecyclerView.ViewHolder(binding.root){


        init {
            // Set up click listener for share icon
            binding.btnShare.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val article = differ.currentList[position]
                    shareArticle(article, itemView.context)
                }
            }

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val article = differ.currentList[position]
                    article.url?.let { openArticleUrl(it, itemView.context) }
                }
            }
        }

        fun bind(article: Article?) {
            article?.let {
                binding.apply {
                    tvTitle.text = it.title
                    tvSource.text = it.source.name

                    // Parse and format the published date string to display only the time
                    val publishedTime = parseAndFormatDate(it.publishedAt)
                    tvPublishedDate.text = publishedTime

                    Glide.with(itemView).load(it.urlToImage).into(imgNews)

                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ArticleListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    private fun openArticleUrl(url: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    // Function to parse and format the date string to display only the time
    private fun parseAndFormatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = dateFormat.parse(dateString)

        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    }

    private fun shareArticle(article: Article, context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, article.title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, article.url)
        context.startActivity(Intent.createChooser(shareIntent, "Share article"))
    }
}