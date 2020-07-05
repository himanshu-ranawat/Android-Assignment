package com.example.assignment.feed.view

import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.assignment.BR
import com.example.assignment.R
import com.example.assignment.databinding.ActivityFeedBinding
import com.example.assignment.feed.model.FeedInfoDto
import com.example.core2.AbstractBinding
import com.example.core2.adapters.BindingRecyclerAdapter

/*
 * [FeedsActivityBinder] : This binder binds list of `Feeds` to `RecyclerView` and handles swipe refresh states.
 */

class FeedsActivityBinder() : AbstractBinding<ActivityFeedBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    /**
     * Variable as Lambda method expression to handle swipe refresh callback
     */
    var onRefreshCallback: (() -> Unit)? = null
    /**
     * Observable field handles state of `SwipeRefreshLayout` with the pair of Booleans.
     */
    val swipeProgress: ObservableField<Pair<Boolean?, Boolean?>> =
        ObservableField(Pair(first = true, second = false))
    /**
     * Adapter class object for `RecyclerView`
     */
    var feedsAdapter: BindingRecyclerAdapter? = null

    override fun onCreated() {
        binding?.data = this
        setUpRecyclerView()
    }

    /**
     * Method basically setup new `RecyclerView` & it's `Adapter`
     */
    private fun setUpRecyclerView() {
        feedsAdapter = BindingRecyclerAdapter.Builder()
            .setLayoutResId(R.layout.item_feed_layout)
            .onBindViewHolderCallback { holder, _, adapter ->
                val feed = adapter.list[holder.adapterPosition] as? FeedInfoDto
                holder.binding.setVariable(BR.feed, feed)
            }
            .build()
        binding?.rvFeeds?.adapter = feedsAdapter
    }

    /**
     * Set [RecyclerView.LayoutManager] passed from input [layoutManager] on `RecyclerView`
     *
     * @param layoutManager as type of [RecyclerView.LayoutManager] to be set
     */
    fun setRecyclerLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        binding?.rvFeeds?.layoutManager = layoutManager
    }

    /**
     * Decide to whether shoe/hide `SwipeRefreshLayout` based on [show] flag
     *
     * @param show flag to set current state of `RefreshLayout`, default value is **true** meaning it's showing progress.
     */
    fun showSwipeProgress(show: Boolean = true) {
        swipeProgress.set(Pair(first = true, second = show))
    }

    override fun onRefresh() {
        onRefreshCallback?.invoke()
    }

    override fun onDestroy() {
        binding?.data = null
    }
}