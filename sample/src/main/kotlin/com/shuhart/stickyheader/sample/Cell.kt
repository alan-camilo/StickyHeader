package com.shuhart.stickyheader.sample

sealed class Cell(val type: Int, val value: Int) {

    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }

    class Item(value: Int, var header: Header?) : Cell(ITEM, value)
    class Header(value: Int) : Cell(HEADER, value)
}