package com.example.dailyevents

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // Aplica espaçamento entre os itens, neste caso, apenas na parte inferior
        outRect.bottom = space
    }
}
