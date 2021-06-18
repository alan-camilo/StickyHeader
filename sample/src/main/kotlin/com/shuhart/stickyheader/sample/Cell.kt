package com.shuhart.stickyheader.sample

sealed class Cell(val type: Int) {

    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }

    class Item(var header: Header?) : Cell(ITEM)
    class Header : Cell(HEADER)
}
