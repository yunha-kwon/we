package com.we.presentation.schedule

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.presentation.component.custom.showCustomDropDownMenu
import com.we.model.ScheduleData
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.component.adapter.ScheduleCalendarAdapter
import com.we.presentation.component.adapter.ScheduleTodoAdapter
import com.we.presentation.databinding.FragmentScheduleBinding
import com.we.presentation.schedule.model.ScheduleUiState.CalendarSet
import com.we.presentation.schedule.model.ScheduleUiState.Loading
import com.we.presentation.schedule.model.toScheduleUpdateParam
import com.we.presentation.schedule.viewmodel.ScheduleViewModel
import com.we.presentation.util.DropDownMenu
import com.we.presentation.util.functionStatusBarColor
import com.we.presentation.util.toYearMonth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(R.layout.fragment_schedule) {

    private val scheduleViewModel: ScheduleViewModel by viewModels()

    private lateinit var scheduleCalendarAdapter: ScheduleCalendarAdapter
    private lateinit var scheduleTodoAdapter: ScheduleTodoAdapter

    override fun initView() {
        initScheduleCalendarAdapter()
        initScheduleTodoAdapter()
        initClickEventListener()
        observeScheduleUiState()

    }

    override fun onResume() {
        super.onResume()
        functionStatusBarColor(requireActivity(), R.color.we_light_pink)
        scheduleViewModel.checkDate()
        Timber.tag("스케쥴 onResume").d("체크")
    }

    private fun initScheduleCalendarAdapter() {
        scheduleCalendarAdapter = ScheduleCalendarAdapter()
        binding.rvScheduleCalendar.apply {
            adapter = scheduleCalendarAdapter
            itemAnimator = null
        }

    }

    private fun initScheduleTodoAdapter() {
        scheduleTodoAdapter = ScheduleTodoAdapter()
        binding.rvScheduleTodo.apply {
            adapter = scheduleTodoAdapter
            itemAnimator = null
        }

    }

    private fun initClickEventListener() {
        binding.apply {
            ivPlus.setOnClickListener {
                navigateDestination(R.id.action_scheduleFragment_to_schedule_register_nav_graph)
            }
            ivScheduleRight.setOnClickListener {
                scheduleViewModel.plusMinusMonth(true)
            }
            ivScheduleLeft.setOnClickListener {
                scheduleViewModel.plusMinusMonth(false)
            }
            scheduleCalendarAdapter.setScheduleClickListener { calendarItem ->
                scheduleViewModel.setSelectedItem(calendarItem)
                scheduleViewModel.clickDays(calendarItem)
            }
            scheduleTodoAdapter.setOnItemClickListener { scheduleData ->
                scheduleViewModel.updateScheduleToggle(scheduleData.scheduleId)
            }
            scheduleTodoAdapter.setOnMenuClickListener { data, view ->
                initDropDownMenu(data, view)
            }
        }
    }

    private fun initDropDownMenu(data: ScheduleData, view: View) {
        showCustomDropDownMenu(
            requireActivity(),
            view,
            DropDownMenu.getDropDown(listOf(0, 1), requireActivity()),
            action = { value ->
                when (value.type) {
                    DropDownMenu.DELETE -> {
                        scheduleViewModel.deleteSchedule(data.scheduleId)
                    }

                    DropDownMenu.UPDATE -> {
                        navigateDestination(
                            R.id.action_scheduleFragment_to_schedule_register_nav_graph,
                            bundleOf("scheduleUpdateParam" to data.toScheduleUpdateParam())
                        )
                    }
                }
            }
        )
    }

    private fun observeScheduleUiState() {
        scheduleViewModel.scheduleUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is Loading -> {

                    }

                    is CalendarSet -> {
                        val date = it.date.toYearMonth()
                        binding.apply {
                            year = getString(R.string.schedule_year, date.first)
                            month = getString(R.string.schedule_month, date.second)
                        }
                        scheduleCalendarAdapter.submitList(it.calendarItem)

                        if (it.calendarItem.isNotEmpty()) { // 할일 넣기
                            Timber.tag("할일").d("할일 체크 ${it.calendarItem}")
                            val list = scheduleViewModel.findScheduleDate(it.calendarItem)
                            scheduleTodoAdapter.submitList(list)

                        }
                    }

                    else -> {

                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        functionStatusBarColor(requireActivity(), R.color.we_ivory)
    }
}
