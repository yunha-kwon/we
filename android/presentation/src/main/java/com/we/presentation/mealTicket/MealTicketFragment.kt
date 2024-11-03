package com.we.presentation.mealTicket

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
import com.we.presentation.databinding.FragmentMealTicketBinding
import com.we.presentation.mealTicket.viewmodel.MealTicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MealTicketFragment : BaseFragment<FragmentMealTicketBinding>(R.layout.fragment_meal_ticket) {
    private val mealTicketViewModel: MealTicketViewModel by viewModels()
    override fun initView() {
        mealTicketViewModel.getQrCode()
        initQrcode()
    }

    private fun initQrcode() {
        binding.apply {

            mealTicketViewModel.qrCodeUrl.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach {
                    ivQrCode.visibility = View.VISIBLE
                    Glide.with(this@MealTicketFragment).load(it).into(ivQrCode)
                    flQrCode.visibility = View.GONE
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }
}