package com.we.presentation.home

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.component.adapter.HomeViewPagerAccountAdapter
import com.we.presentation.component.adapter.HomeViewPagerBannerAdapter
import com.we.presentation.databinding.FragmentHomeBinding
import com.we.presentation.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

private const val TAG = "HomeFragment_싸피"

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var homeAdapter: HomeViewPagerAccountAdapter

    override fun initView() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getCoupleData()
            binding.tvHomeRemainingDays.visibility = View.VISIBLE
            initDDay()
        }
        setUpAccountViewPager()
        setUpBannerViewPager()
        initClickEventListener()
        memberData()
    }

    private fun memberData() {
        binding.apply {
            homeViewModel.members.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach {
                    tvHomeUserName.text = it.name + "님 환영합니다"
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }


    private fun initDDay() {
        binding.apply {

            homeViewModel.coupleInfo.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach {

                    if (it.DDay == 0) {
                        tvHomeRemainingDays.text = "결혼을 축하드립니다!!"
                    } else if (it.DDay!! > 0) {
                        tvHomeRemainingDays.text = "결혼식까지" + it.DDay + "일"
                        tvHomeRemain.text = "남았습니다."
                    }
                    else{
                        tvHomeRemainingDays.text = "결혼식을 한지 " + (it.DDay!! * -1)  + "일"
                        tvHomeRemain.text = "지났습니다."
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

        }
    }

    private fun setUpAccountViewPager() {

        homeAdapter = HomeViewPagerAccountAdapter(
            accountClickListener = { idx, account ->
                if (idx == homeAdapter.currentList.lastIndex) {
                    navigateDestination(R.id.action_homeFragment_to_accountFragment)
                } else {
                    navigateDestination(
                        R.id.action_homeFragment_accountCheckFragment,
                        bundleOf("account" to account)
                    )
                }
            },
            accountRemittance = { account ->
                navigateDestination(
                    R.id.action_homeFragment_to_remittance_gragh,
                    bundle = bundleOf("account" to account)
                )
            },
            typeCheck = false,
            moreVertClickListener = { resultView, account, bankName ->
                val popupMenu = PopupMenu(requireContext(), resultView)
                popupMenu.menuInflater.inflate(R.menu.menu_account_register, popupMenu.menu)

                // 메뉴 아이템 클릭 리스너 설정
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.representative -> {
                            // 대표 계좌 등록 동작
                            homeViewModel.postPriorAccount(account)
                            true
                        }

                        R.id.couple -> {
                            // 커플 계좌 등록 동작
                            homeViewModel.postCoupleAccount(account, bankName)
                            true
                        }

                        else -> false
                    }
                }
                // 팝업 메뉴 표시
                popupMenu.show()
            }
        )

        binding.apply {
            vpHomeAccount.adapter = homeAdapter
            vpHomeTotalAccountDotsIndicator.attachTo(vpHomeAccount)

            viewLifecycleOwner.lifecycleScope.launch {
                delay(700)
                vpHomeAccount.setCurrentItem(0, false)
            }

            vpHomeAccount.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    tvAccountInfo.text = homeAdapter.currentList[position].accountInfo
                }
            })
        }

        homeViewModel.accountList.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { list ->
                Timber.d("list flowWithLifecycle: $list")
                homeAdapter.submitList(list)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setUpBannerViewPager() {
        val test = arrayListOf(
            R.drawable.image_25,
            R.drawable.ic_banner_two,
            R.drawable.ic_banner_three,
            R.drawable.ic_banner_four
        )

        val adapter = HomeViewPagerBannerAdapter(test)
        binding.apply {
            vpHomeBanner.adapter = adapter
            vpHomeTotalBannerDotsIndicator.attachTo(vpHomeBanner)

            val handler = Handler(Looper.getMainLooper())
            val runnable = object : Runnable {
                override fun run() {
                    val nextItem =
                        if (vpHomeBanner.currentItem == vpHomeBanner.adapter?.itemCount?.minus(1)) 0 else vpHomeBanner.currentItem + 1
                    vpHomeBanner.setCurrentItem(nextItem, true)
                    handler.postDelayed(this, 3000) // 3초마다 다음 페이지로 전환
                }
            }

            vpHomeAccount.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // 사용자 상호작용 후 자동 스크롤 재설정
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, 3000)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getAccountList()
    }


    private fun initClickEventListener() {
        binding.apply {
            icInvitation.flContent.setOnClickListener {
                navigateDestination(R.id.action_homeFragment_to_invitationFragment)
            }

            icQrCode.flContent.setOnClickListener {
                navigateDestination(R.id.action_homeFragment_to_guestBookFragment)
            }

            icHomeButton.flContent.setOnClickListener {
                navigateDestination(R.id.action_homeFragment_to_mealTicketFragment)
            }
        }
    }
}