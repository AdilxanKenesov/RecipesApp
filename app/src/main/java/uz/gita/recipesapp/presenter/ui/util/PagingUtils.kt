package uz.gita.recipesapp.presenter.ui.util

import androidx.paging.LoadState
import uz.gita.recipesapp.presenter.ui.components.PagingFooterState

fun LoadState.toFooterState(): PagingFooterState =
    when (this) {
        is LoadState.Loading -> PagingFooterState.Loading
        is LoadState.Error -> PagingFooterState.Error
        is LoadState.NotLoading -> if (endOfPaginationReached) PagingFooterState.End else PagingFooterState.Idle
    }
