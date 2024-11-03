package com.we.presentation.transfer

import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.component.ShareData
import com.we.presentation.databinding.FragmentTransferEasyPasswordBinding
import com.we.presentation.transfer.viewmodel.TransferViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executor


@AndroidEntryPoint
class TransferEasyPasswordFragment :
    BaseFragment<FragmentTransferEasyPasswordBinding>(R.layout.fragment_transfer_easy_password) {

    private val transferViewModel: TransferViewModel by hiltNavGraphViewModels(R.id.transfer_nav_graph)

    private lateinit var buttonList: MutableList<Button>
    private lateinit var passwordList: MutableList<View>

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun initView() {
        initButtonList()
        initPasswordList()
        initClickEvent()
        observeEasyPassword()
        observeTransferSuccess()
        biometricPrompt = setBiometricPrompt()
        promptInfo = setPromptInfo()

        authenticateToEncrypt()  // 생체 인증 가능 여부 확인
    }

    private fun initButtonList() {
        buttonList = mutableListOf()
        val randomNumbers = (0..9).shuffled().take(10)

        for (i in 0 until binding.tlEasyPassword.childCount) {
            val tableRow = binding.tlEasyPassword.getChildAt(i) as TableRow
            for (j in 0 until tableRow.childCount) {
                if (i == 3 && (j == 0 || j == 2)) continue
                val button = tableRow.getChildAt(j) as Button
                buttonList.add(button)
            }
        }
        buttonList.forEachIndexed { index, button ->
            button.text = randomNumbers[index].toString()
        }
    }

    private fun initPasswordList() {
        passwordList = mutableListOf()
        val clPassword = binding.icPassword.clPassword

        clPassword.children.forEach {
            passwordList.add(it)
        }
    }


    private fun initClickEvent() {
        binding.apply {
            ivBack.setOnClickListener {
                transferViewModel.clearEasyPasswordAndCheck()
                navigatePopBackStack()
            }
            buttonList.forEach { button ->
                button.setOnClickListener {
                    setBtnSelected(button)
                }
            }
            binding.btnTen.setOnClickListener {

            }
            binding.btnTwelve.setOnClickListener {
                transferViewModel.addRemoveEasyPassword(false)
            }
        }
    }

    private fun setBtnSelected(button: Button) {
        transferViewModel.addRemoveEasyPassword(true, button.text.toString())
        val randomButtons = buttonList.shuffled().take(2).toMutableList()
        val pair = Pair(randomButtons[0], randomButtons[1])
        viewLifecycleOwner.lifecycleScope.launch {
            pair.first.isSelected = true
            pair.second.isSelected = true
            button.isSelected = true
            delay(200L)
            pair.first.isSelected = false
            pair.second.isSelected = false
            button.isSelected = false
        }
    }

    private fun observeEasyPassword() {
        transferViewModel.easyPassword.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                setUpEasyPassWord(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setUpEasyPassWord(list: List<String>) {
        val count = list.size - 1
        for (i in 0..5) {
            passwordList[i].isSelected = i <= count
        }
        if (list.size == 6) {
            transferViewModel.postTransfer(ShareData.legId)
            showLoading()
        }
    }

    private fun observeTransferSuccess() {
        transferViewModel.transferSuccess.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                delay(1000L)
                dismissLoading()
                if (it) {
                    Timber.tag("이체").d("성공")
                    navigateDestination(R.id.action_transferEasyPasswordFragment_to_transferSuccessFragment, bundleOf("result" to it))
                } else {
                    Timber.tag("이체").d("실패")
                    navigateDestination(R.id.action_transferEasyPasswordFragment_to_transferSuccessFragment, bundleOf("result" to it))
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setPromptInfo(): BiometricPrompt.PromptInfo {
        val promptBuilder: BiometricPrompt.PromptInfo.Builder =
            androidx.biometric.BiometricPrompt.PromptInfo.Builder()

        promptBuilder.setTitle("생체인증을 진행합니다")
        promptBuilder.setNegativeButtonText("취소")

        return promptBuilder.build()
    }

    private fun setBiometricPrompt(): BiometricPrompt {
        executor = ContextCompat.getMainExecutor(requireContext())

        return BiometricPrompt(
            this@TransferEasyPasswordFragment,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(),
                        "지문 인식 ERROR [ errorCode: $errorCode, errString: $errString ]",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    transferViewModel.setBioEasyPassword("99999999")
                    transferViewModel.postTransfer(ShareData.legId)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(requireContext(), "지문 인식 실패", Toast.LENGTH_SHORT).show()
                }
            })
    }


    fun authenticateToEncrypt() = with(binding) {

        var textStatus = ""
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {

            // 생체 인증 가능
            BiometricManager.BIOMETRIC_SUCCESS -> textStatus =
                "App can authenticate using biometrics."

            // 기기에서 생체 인증을 지원하지 않는 경우
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> textStatus =
                "No biometric features available on this device."

            // 현재 생체 인증을 사용할 수 없는 경우
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> textStatus =
                "Biometric features are currently unavailable."

            // 생체 인식 정보가 등록되어 있지 않은 경우
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                textStatus = "Prompts the user to create credentials that your app accepts."

                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder
                    .setTitle("나의앱")
                    .setMessage("지문 등록이 필요합니다. 지문등록 설정화면으로 이동하시겠습니까?")
                    .setPositiveButton("확인") { dialog, which -> }
                    .setNegativeButton("취소") { dialog, which -> dialog.cancel() }
                dialogBuilder.show()
            }

            // 기타 실패
            else -> textStatus = "Fail Biometric facility"
        }


        // 인증 실행하기
        goAuthenticate()
    }

    // 생체 인식 인증 실행
    private fun goAuthenticate() {

        promptInfo?.let {
            biometricPrompt.authenticate(it)  // 인증 실행
        }
    }

}