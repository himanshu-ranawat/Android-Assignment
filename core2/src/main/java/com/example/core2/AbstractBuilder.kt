package com.example.core2

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

/**
 * [AbstractBuilder] :
 *
 * Main **builder** class for Activity/Fragment that is used as *Model* to hold data used in abstract activity/fragment class.
 *
 * Check out variables:
 *   1. [AbstractBuilder.contentView]
 *   2. [AbstractBuilder.abstractBinding]
 *
 */
class AbstractBuilder {
    /**
     * Variable that hold layout `resource id` of Activity
     */
    @LayoutRes
    var contentView: Int? = null

    /**
     * Variable to be set if used **Data-Binding** to provide Generic logic for [AbstractBinding].
     */
    var abstractBinding: AbstractBinding<out ViewDataBinding>? = null

    var isCacheFragment: Boolean = false
}