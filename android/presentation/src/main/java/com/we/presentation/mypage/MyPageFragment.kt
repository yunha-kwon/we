package com.we.presentation.mypage

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.we.model.BankData
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.component.adapter.HomeViewPagerAccountAdapter
import com.we.presentation.databinding.FragmentMyPageBinding
import com.we.presentation.mypage.viewmodel.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myPageViewModel: MyPageViewModel by viewModels()
    private lateinit var homeAdapter: HomeViewPagerAccountAdapter
    override fun initView() {
        setUpAccountViewPager()
    }

    private fun setUpAccountViewPager() {
        homeAdapter = HomeViewPagerAccountAdapter(
            accountClickListener = { idx, account ->
                if (idx == homeAdapter.currentList.lastIndex) {
                    navigateDestination(R.id.action_homeFragment_to_accountFragment)
                } else {
                    navigateDestination(R.id.action_homeFragment_accountCheckFragment)
                }
            },
            accountRemittance = {
                navigateDestination(R.id.action_fragment_home_to_remittanceFragment)
            },
            typeCheck = true,
            moreVertClickListener = {view, account, bankName ->

            }
        )

        binding.apply {
            vpMyAccount.adapter = homeAdapter
            vpTotalAccountDotsIndicator.attachTo(vpMyAccount)
        }


        myPageViewModel.accountList.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { list ->
                Timber.d("list flowWithLifecycle: $list")
                homeAdapter.submitList(list)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
//        lifecycleScope.launch {
//            myPageViewModel.accountList.flowWithLifecycle(viewLifecycleOwner.lifecycle)
//                .onEach {list ->
//                    adapter = HomeViewPagerAccountAdapter(
//                        list.toMutableList(),
//                        accountClickListener = {
//                            Toast.makeText(requireContext(), "허용하지 않음", Toast.LENGTH_SHORT).show()
//                        },
//                        accountRemittance = {
//                            Toast.makeText(requireContext(), "허용하지 않음", Toast.LENGTH_SHORT).show()
//                        },
//                        true,
//                        moreVertClickListener = {}
//                    )
//
//                    binding.apply {
//                        vpMyAccount.adapter = adapter
//                        vpTotalAccountDotsIndicator.attachTo(vpMyAccount)
//                    }
//                }
//                .launchIn(viewLifecycleOwner.lifecycleScope)


//        }
    }
}