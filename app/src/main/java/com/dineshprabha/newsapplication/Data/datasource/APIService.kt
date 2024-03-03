package com.dineshprabha.newsapplication.Data.datasource

import android.util.Log
import com.dineshprabha.newsapplication.Data.model.ArticleList
import com.dineshprabha.newsapplication.Utils.Constants.BASE_URL
import com.dineshprabha.newsapplication.Utils.Constants.TAG
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class APIService {

    fun getNews(): ArticleList? {
        //using thread to run on background

        val connection = URL(BASE_URL).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "application/json")
        connection.connectTimeout = 10000
        connection.readTimeout = 10000

        return try {
            val reader = InputStreamReader(connection.inputStream)
            reader.use { input ->

                val response = StringBuilder()
                val bufferedReader = BufferedReader(input)

                bufferedReader.forEachLine {
                    response.append(it.trim())
                }

                Log.d(TAG, "Success: ${response.toString()}")

                // Parse JSON using Gson
                val gson = Gson()
                gson.fromJson(response.toString(), ArticleList::class.java)
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error: ${e.localizedMessage}")
            null
        } finally {
            connection.disconnect()
        }
    }
}