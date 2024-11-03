package com.we.presentation.guestbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.databinding.FragmentGuestBookBinding
import com.we.presentation.guestbook.viewmodel.GuestBookViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class GuestBookFragment : BaseFragment<FragmentGuestBookBinding>(R.layout.fragment_guest_book) {
    private val guestBookViewModel: GuestBookViewModel by viewModels()

    override fun initView() {
        initClickEventListener()
        getQrCodeFail()
        initQrCodeSetting()
    }

    private fun getQrCodeFail() {
        guestBookViewModel.getQrCode() {
            binding.flQrCode.visibility = View.VISIBLE
        }
    }

    private fun initQrCodeSetting() {
        binding.apply {
            guestBookViewModel.qrCodeUrl.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach {
                    Timber.d("qr url : $it")
                    if (guestBookViewModel.qrCodeUrl.first() != "") {
                        flQrCode.visibility = View.GONE
                        ivQrCode.visibility = View.VISIBLE
                        Glide.with(this@GuestBookFragment).load(it).into(ivQrCode)
//                        tvRegisterAccount.visibility = View.VISIBLE
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun initClickEventListener() {
        binding.apply {
            ivQrCodeBack.setOnClickListener {
                navigatePopBackStack()
            }

            flQrCode.setOnClickListener {
                guestBookViewModel.postQrCode()
            }
        }
    }
}