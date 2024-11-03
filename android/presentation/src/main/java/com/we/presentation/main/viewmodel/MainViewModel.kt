package com.we.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.request.RequestToken
import com.data.repository.FcmRepository
import com.data.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fcmRepository: FcmRepository
) : ViewModel(){

    fun postToken(token : String){
        viewModelScope.launch {
            fcmRepository.postToken(requestToken = token).collectLatest {
                when(it){
                    is ApiResult.Success -> {
                        Timber.tag("토큰등록").d("Token Success")
                    }
                    is ApiResult.Error -> {
                        Timber.tag("토큰등록").d("Token Fail ${it.exception}")
                    }
                }
            }
        }
    }
}