package com.example.assignment.api

import com.example.assignment.feed.model.FeedListDto
import retrofit2.Call
import retrofit2.http.GET

/*
Feeds API interface provides method for Retrofit endpoint connection as RESTFul APIs
 */
interface FeedsApi {
    @GET("s/2iodh4vg0eortkl/facts.json")
    fun getFeedsDetails(): Call<FeedListDto?>
}