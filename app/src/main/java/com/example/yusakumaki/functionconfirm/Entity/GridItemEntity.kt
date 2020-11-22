package com.example.yusakumaki.functionconfirm.Entity

import com.example.yusakumaki.functionconfirm.R

data class GridItemEntity(
        var id: Int,
        var title: String,
        var image: Int
)

val gridItems = listOf(
        GridItemEntity(0, "タイトル0", R.drawable.ic_projections_pana),
        GridItemEntity(1, "タイトル1", R.drawable.ic_projections_rafiki),
        GridItemEntity(2, "タイトル2", R.drawable.ic_visual_data_cuate))