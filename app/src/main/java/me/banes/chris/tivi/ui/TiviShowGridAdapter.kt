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

package me.banes.chris.tivi.ui

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import me.banes.chris.tivi.R
import me.banes.chris.tivi.data.TiviShow

internal class TiviShowGridAdapter : PagedListAdapter<TiviShow, TiviShowGridViewHolder>(TiviShowDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiviShowGridViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.grid_item, parent, false)
        return TiviShowGridViewHolder(view)
    }

    override fun onBindViewHolder(holder: TiviShowGridViewHolder, position: Int) {
        val show = getItem(position)
        if (show != null) {
            holder.bindShow(show)
        }
    }
}

private object TiviShowDiffCallback : DiffCallback<TiviShow>() {
    override fun areItemsTheSame(oldItem: TiviShow, newItem: TiviShow): Boolean {
        return (oldItem.id != null && oldItem.id == newItem.id)
                || (oldItem.traktId != null && oldItem.traktId == newItem.traktId)
                || (oldItem.tmdbId != null && oldItem.tmdbId == newItem.tmdbId)
    }

    override fun areContentsTheSame(oldItem: TiviShow, newItem: TiviShow): Boolean {
        return oldItem == newItem
    }
}

