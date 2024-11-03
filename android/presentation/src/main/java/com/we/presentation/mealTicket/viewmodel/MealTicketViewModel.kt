package com.we.presentation.mealTicket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.LedgersRepository
import com.data.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MealTicketViewModel @Inject constructor(
    private val ledgersRepository: LedgersRepository
) : ViewModel(){



    private val _qrCodeUrl = MutableStateFlow<String>("")
    val qrCodeUrl: Flow<String> get() = _qrCodeUrl


    private fun setQrCodeUrl(url: String) {
        _qrCodeUrl.update { url }
    }

    fun getQrCode() {
        viewModelScope.launch {
            ledgersRepository.getMyMealTicket().collectLatest {
                when(it){
                    is ApiResult.Success -> {
                        Timber.d("qr load 성공 " + it.data)
                        setQrCodeUrl(it.data.url)
                    }
                    is ApiResult.Error -> {
                        Timber.d("qr load fail " + it.exception.message)

                    }
                }
            }
        }
    }
}