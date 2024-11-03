package com.we.presentation.couple

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.data.model.request.RequestCouple
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.couple.viewmodel.CoupleViewModel
import com.we.presentation.databinding.FragmentCoupleBinding
import com.we.presentation.util.ScheduleRegisterType
import com.we.presentation.util.toYearMonthDay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CoupleFragment : BaseFragment<FragmentCoupleBinding>(R.layout.fragment_couple) {

    private val coupleViewModel: CoupleViewModel by viewModels()

    override fun initView() {
        initClickEventListener()
        initCoupleCode()
    }

    private fun initCoupleCode() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                coupleViewModel.coupleCode.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach {
                        binding.tvCoupleCode.text = it.code
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun initClickEventListener() {
        binding.apply {
            icTitle.ivBack.setOnClickListener {
                navigatePopBackStack()
            }

            tvInputComplete.setOnClickListener {
                coupleViewModel.setCoupleSuccessCode(coupleSuccessCode = RequestCouple(etInputCouple.text.toString(), coupleViewModel.scheduleRegisterParam.value.date.split("T")[0]))
                coupleViewModel.postCouple() {
                    when (it) {
                        true -> {
                            Toast.makeText(requireContext(), "매칭 성공", Toast.LENGTH_SHORT).show()
                        }

                        false -> {
                            Toast.makeText(requireContext(), "매칭 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            tvInputDate.setOnClickListener {
                showDatePicker()
            }
        }
    }

    private fun showDatePicker() {
        val currentDateTime = Calendar.getInstance()
        val selectedDateString = binding.tvInputDate.text.toString()
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        val calendar = if (selectedDateString.isNotEmpty() && selectedDateString != "일정을 선택해주세요") {
            try {
                Calendar.getInstance().apply {
                    time = dateFormat.parse(selectedDateString) ?: Date()
                }
            } catch (e: Exception) {
                currentDateTime
            }
        } else {
            currentDateTime
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val formattedDate = dateFormat.format(selectedDate.time)
            coupleViewModel.setRegisterParam(ScheduleRegisterType.DATE, formattedDate)
        }, year, month, day).show()
    }
}