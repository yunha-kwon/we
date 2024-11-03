package com.we.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil.inflate
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<VB : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    // 바인딩 인플레이터 추상 프로퍼티 정의
    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 바인딩 인플레이터를 사용하여 바인딩 초기화
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    // 필요한 경우 setupViews를 제공
    open fun setupViews() {
        // 하위 클래스에서 구현
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    // Navigation 이동 메서드들
    fun navigateDestination(@IdRes action: Int) {
        findNavController().navigate(action)
    }

    fun navigatePopBackStack() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}