package com.revolut.recyclerkit.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/*
 * Copyright (C) 2019 Revolut
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
private typealias RecyclerDelegate = RecyclerViewDelegate<ListItem, RecyclerView.ViewHolder>

abstract class AbsRecyclerDelegatesAdapter(
    val delegatesManager: DelegatesManager = DelegatesManager()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegatesManager.getDelegateFor(viewType).onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        getDelegateFor(viewType = holder.itemViewType)
            .onBindViewHolder(
                holder = holder,
                data = getItem(position),
                pos = position,
                payloads = payloads
            )
    }

    @Suppress("unchecked_cast")
    private fun getDelegateFor(viewType: Int): RecyclerDelegate = delegatesManager.getDelegateFor(viewType = viewType) as RecyclerDelegate

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        this.onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        getDelegateFor(viewType = holder.itemViewType).onViewRecycled(holder = holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        getDelegateFor(viewType = holder.itemViewType).onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        getDelegateFor(viewType = holder.itemViewType).onViewDetachedFromWindow(holder)
    }

    override fun getItemViewType(position: Int): Int = delegatesManager.getViewTypeFor(position, getItem(position))

    abstract fun getItem(position: Int): ListItem

}
