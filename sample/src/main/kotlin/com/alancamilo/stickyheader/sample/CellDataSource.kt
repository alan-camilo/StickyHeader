package com.alancamilo.stickyheader.sample

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlin.math.min

class CellDataSource(private val list: List<Cell.Item>) : PagingSource<Int, Cell.Item>() {

    private var fromIndex = 0

    override fun getRefreshKey(state: PagingState<Int, Cell.Item>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cell.Item> {
        val nextPageNumber = params.key ?: 1
        val toIndex = min(fromIndex + params.loadSize, list.size)
        val subList = list.subList(fromIndex, toIndex)
        fromIndex += params.loadSize
        val nextKey = if (toIndex < list.size) nextPageNumber + 1 else null
        return LoadResult.Page(subList, null, nextKey)
    }
}