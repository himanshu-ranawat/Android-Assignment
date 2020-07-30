package com.example.assignment.feed.view

import android.os.Bundle
import com.example.assignment.R
import com.example.core2.AbstractBaseActivity
import com.example.core2.absBuilder

/*
 * [FeedsActivity] : Activity class that loads feeds from network/local database with the help of ViewModel
 * and binds it to UI with the help of binder class.
 *
 * Provides feeds whether from local database if available as cached else performs network call to fetch feeds
 *
 * Also handles screen rotation to maintain data across configuration changes.
 */

class FeedsActivity : AbstractBaseActivity() {

    override fun setUpBuilder() = absBuilder {
        contentView = R.layout.activity_feed
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
    }
}
