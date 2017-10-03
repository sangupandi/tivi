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
 */

package me.banes.chris.tivi.calls

import android.arch.paging.LivePagedListProvider
import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.trakt5.TraktV2
import com.uwetrottmann.trakt5.entities.TrendingShow
import com.uwetrottmann.trakt5.enums.Extended
import io.reactivex.Maybe
import io.reactivex.Single
import me.banes.chris.tivi.data.TiviShow
import me.banes.chris.tivi.data.TiviShowDao
import me.banes.chris.tivi.data.TrendingDao
import me.banes.chris.tivi.data.TrendingEntry
import me.banes.chris.tivi.extensions.toRxSingle
import me.banes.chris.tivi.util.AppRxSchedulers
import me.banes.chris.tivi.util.DatabaseTxRunner
import javax.inject.Inject

class TrendingCall @Inject constructor(
        databaseTxRunner: DatabaseTxRunner,
        showDao: TiviShowDao,
        private val trendingDao: TrendingDao,
        tmdb: Tmdb,
        trakt: TraktV2,
        schedulers: AppRxSchedulers)
    : PaginatedTraktCall<TrendingShow>(databaseTxRunner, showDao, tmdb, trakt, schedulers) {

    override fun createPagedListProvider(): LivePagedListProvider<Int, TiviShow> {
        return trendingDao.trendingPagedList()
    }

    override fun networkCall(page: Int, pageSize: Int): Single<List<TrendingShow>> {
        return trakt.shows()
                .trending(page + 1, pageSize, Extended.NOSEASONS)
                .toRxSingle()
    }

    override fun filterResponse(response: TrendingShow): Boolean {
        return response.show.ids.tmdb != null
    }

    override fun loadShow(response: TrendingShow): Maybe<TiviShow> {
        return showFromTmdb(response.show.ids.tmdb, response.show.ids.trakt)
    }

    override fun lastPageLoaded(): Single<Int> {
        return trendingDao.getLastTrendingPage()
    }

    override fun deleteEntries() {
        trendingDao.deleteTrendingShows()
    }

    override fun deletePage(page: Int) {
        trendingDao.deleteTrendingShowsPageSync(page)
    }

    override fun saveEntry(show: TiviShow, page: Int, order: Int) {
        assert(show.id != null)
        val entry = TrendingEntry(showId = show.id!!, page = page, pageOrder = order)
        trendingDao.insertTrending(entry)
    }

}