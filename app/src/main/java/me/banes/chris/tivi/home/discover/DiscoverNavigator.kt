/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.banes.chris.tivi.home.discover

import android.support.v4.app.FragmentTransaction
import me.banes.chris.tivi.R
import me.banes.chris.tivi.home.trending.PopularShowsFragment
import me.banes.chris.tivi.home.trending.TrendingShowsFragment

interface DiscoverNavigator {
    fun showPopular()
    fun showTrending()
}

internal class DiscoverNavigatorImpl(private val fragment: DiscoverFragment) : DiscoverNavigator {
    override fun showPopular() {
        fragment.childFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.home_content, PopularShowsFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun showTrending() {
        fragment.childFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.home_content, TrendingShowsFragment())
                .addToBackStack(null)
                .commit()
    }
}