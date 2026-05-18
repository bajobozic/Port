package com.bajobozic.tv_ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bajobozic.storage.domain.model.TvShow
import com.bajobozic.tv_component.domain.usecase.GetTvShowPagingDataUseCase
import kotlinx.coroutines.flow.Flow

internal class TvShowsViewModel(
    getTvShowPagingDataUseCase: GetTvShowPagingDataUseCase,
) : ViewModel() {
    val tvShowsPagingData: Flow<PagingData<TvShow>> = getTvShowPagingDataUseCase("en-US")
        .cachedIn(viewModelScope)
}
