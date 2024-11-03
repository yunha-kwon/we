package com.we.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.we.presentation.component.custom.CustomDialog

abstract class BaseFragment<T : ViewDataBinding>(private val layoutResId: Int) : Fragment() {
    private var _binding: T? = null
    val binding get() = _binding!!

    protected var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun initView()

    //Navigation 이동
    fun navigateDestination(@IdRes action: Int) {
        findNavController().navigate(action)
    }

    fun navigateDestination(@IdRes action: Int, bundle: Bundle) {
        findNavController().navigate(action, bundle)
    }

    //popBackstack
    fun navigatePopBackStack() {
        findNavController().popBackStack()
    }

    protected fun showLoading() {
        loadingDialog = CustomDialog(requireActivity()).progressDialog()
    }

    protected fun dismissLoading() {
        if (loadingDialog != null)
            loadingDialog!!.dismiss()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
