package com.shuhart.stickyheader.sample

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shuhart.stickyheader.StickyHeaderItemDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val adapter = CellAdapter()
    private val stateFlow = MutableStateFlow<PagingData<Cell>?>(null)
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            adapter = this@MainActivity.adapter
        }

        val stickyDecorator = StickyHeaderItemDecorator(this.adapter)
        stickyDecorator.attachToRecyclerView(recyclerView)
        lifecycleScope.launch {
            stateFlow.collect {
                it?.let { adapter.submitData(it) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { getData() }
    }

    private suspend fun getData() {
        val list = List(100) { index ->
            Cell.Item(index, null)
        }

        return Pager(
            // Configure how data is loaded by passing additional properties to
            // PagingConfig, such as prefetchDistance.
            PagingConfig(pageSize = 20)
        ) {
            CellDataSource(list)
        }.flow
            .map { pagingData ->
                pagingData.insertSeparators { before: Cell.Item?, after: Cell.Item? ->
                    // last page
                    if (after == null) {
                        null
                    } else {
                        // Show a header every 10 cells
                        if (after.value % 10 == 0) {
                            Cell.Header(after.value / 10).also {
                                after.header = it
                            }
                        } else {
                            after.header = before?.header ?: Cell.Header(0)
                            null
                        }
                    }
                }
            }.collectLatest { stateFlow.value = it }
    }
}