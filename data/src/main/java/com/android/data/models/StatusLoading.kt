package com.android.data.models

sealed class StatusLoading {
    object Success : StatusLoading()
    object Loading : StatusLoading()
    object Error : StatusLoading()
}
