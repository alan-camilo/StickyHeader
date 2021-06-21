# Why this fork?
The original implementation has a `StickyAdapter` class which inherits from `RecyclerView.Adapter`, 
despite not using any of the `RecyclerView.Adapter` functions. This choice prohibits using others 
adapters, e.g. `PagingDataAdapter`. This fork has instead an interface named `StickyHeaderAdapter`, 
along with a sample app using `PagingDataAdapter` from Paging 3.

# StickyHeader

The StickyHeader was created mainly because most popular libraries for sticky headers are too complicated or offer much more features than needed. Some of these libraries also break DiffUtils. For performance reason, a header view is created only once while view types and click listeners are not supported.

Also, by customizing this view via supplied adapter, a new sticky header will replace the previous one.

<img src="/images/small_demo.gif" alt="Sample" width="300px" />

Usage
-----

1. Add mavenCentral() to repositories block in the gradle file.
2. Add `implementation 'com.github.shuhart:stickyheader:1.1.0` to the dependencies.
3. Look into the sample for additional details on how to use and configure the library.

An adapter that implements [StickyHeaderAdapter](stickyheader/src/main/kotlin/com/shuhart/stickyheader/StickyHeaderAdapter.kt) 
is necessary for the StickyHeaderItemDecorator that is used to create and bind sticky headers:

```kotlin
override fun getHeaderPositionForItem(itemPosition: Int): Int {
    // Return a position of a header that represents an item at adapter position.
    // For the header itself return the same itemPosition.
}

override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, headerPosition: Int) {
    // Update a header content here.
}

override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    // Create a ViewHolder for a header (called only once).
}
 ```


Then, attach it to the RecyclerView:

```kotlin
val decorator = StickyHeaderItemDecorator(adapter)
decorator.attachToRecyclerView(recyclerView)
```

How it works
-----
* A ViewHolder is created by the adapter to reuse and bind every header.
* A header view is drawn on top of the RecyclerView using onDrawOver() callback of ItemDecoration.

License
=======

    Copyright 2018 Bogdan Kornev.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
