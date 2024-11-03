package com.we.presentation.schedule


import android.app.Dialog
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.presentation.base.BaseDialogFragment
import com.we.presentation.R
import com.we.presentation.databinding.FragmentLocationRegisterBinding
import com.we.presentation.schedule.viewmodel.ScheduleRegisterViewModel
import com.we.presentation.util.ScheduleRegisterType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class LocationRegisterFragment :
    BaseDialogFragment<FragmentLocationRegisterBinding>(R.layout.fragment_location_register) {
    private val scheduleRegisterViewModel: ScheduleRegisterViewModel by hiltNavGraphViewModels(R.id.schedule_register_nav_graph)

    override fun initCreateDialog(): Dialog = Dialog(requireContext(), theme)

    override fun initView(savedInstanceState: Bundle?) {
        initWebView()
        initClickEventListener()
    }

    private fun initClickEventListener() {
        binding.apply {
            icTitle.ivBack.setOnClickListener {
                navigatePopBackStack()
            }
        }
    }

    private fun initWebView() {
        binding.wvLocation.apply {
            clearCache(true)
            settings.javaScriptEnabled = true
            addJavascriptInterface(BridgeInterface(), "Android")
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    loadUrl("javascript:sample2_execDaumPostcode();")
                }
            }

            loadUrl("https://wewe-a3725.web.app")
        }
    }

    inner class BridgeInterface() {
        @JavascriptInterface
        fun processDATA(fullRoadAddr: String) {
            viewLifecycleOwner.lifecycleScope.launch{
                scheduleRegisterViewModel.setRegisterParam(ScheduleRegisterType.LOCATION, fullRoadAddr)
                navigatePopBackStack()
            }
        }

    }
}