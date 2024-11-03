package com.we.presentation.sign


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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.we.presentation.R
import com.we.presentation.base.BaseFragment
import com.we.presentation.component.ShareData
import com.we.presentation.databinding.FragmentEasyPasswordRegisterBinding
import com.we.presentation.remittance.viewmodel.RemittanceViewModel
import com.we.presentation.sign.model.SignInUiState
import com.we.presentation.sign.model.SignUpUiState
import com.we.presentation.sign.viewmodel.SignInViewModel
import com.we.presentation.sign.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executor

@AndroidEntryPoint
class EasyPasswordRegisterFragment :
    BaseFragment<FragmentEasyPasswordRegisterBinding>(R.layout.fragment_easy_password_register) {

    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private val signInViewModel: SignInViewModel by viewModels()
    private val remittanceViewModel: RemittanceViewModel by hiltNavGraphViewModels(R.id.remittance_gragh)

    private lateinit var buttonList: MutableList<Button>
    private lateinit var passwordList: MutableList<View>

    private val args: EasyPasswordRegisterFragmentArgs by navArgs()


    // ----------- bio

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun initView() {
        Timber.d("EasyPasswordRegisterFragment initView ${args.easyPasswordType}")
        if (args.easyPasswordType == true) {

            initButtonList()
            initPasswordList()
            initClickEvent()
            observeEasyPassWord()
            observeEasyPasswordCheck()
            observeSignUpUiState()
        } else {

            initButtonList()
            initPasswordList()
            initClickEvent()
            observeEasyPassWord()
            initTransferSetting()
            biometricPrompt = setBiometricPrompt()
            promptInfo = setPromptInfo()

            authenticateToEncrypt()  // 생체 인증 가능 여부 확인
        }
        observeSignIn()


    }

    private fun initTransferSetting() {
        binding.apply {
            tvRegisterEasyPassword.visibility = View.GONE
        }

        signUpViewModel.clearSignUpParam()
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
                signUpViewModel.clearEasyPasswordAndCheck()
                navigatePopBackStack()
            }
            buttonList.forEach { button ->
                button.setOnClickListener {
                    setBtnSelected(button)
                }
            }
            binding.btnTen.setOnClickListener {
                navigateDestination(R.id.action_fragment_easy_password_register_to_fragment_sign_up_success)
            }
            binding.btnTwelve.setOnClickListener {
                signUpViewModel.addRemoveEasyPassword(false)
            }
        }
    }

    private fun setBtnSelected(button: Button) {
        if (signUpViewModel.easyPasswordType.value) {
            signUpViewModel.addRemoveEasyPassword(true, button.text.toString())
        } else {
            signUpViewModel.addRemoveEasyPasswordCheck(true, button.text.toString())
        }
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

    private fun observeEasyPassWord() {
        signUpViewModel.signUpParam.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (signUpViewModel.easyPasswordType.value) {
                    setUpEasyPassWord(it.easyPassword)
                } else {
                    setUpEasyPassWordCheck(it.easyPasswordCheck)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setUpEasyPassWord(list: List<String>) {
        val count = list.size - 1
        for (i in 0..5) {
            passwordList[i].isSelected = i <= count
        }
        if (list.size == 6) {
            if (args.easyPasswordType == true) {
                signUpViewModel.setEasyPasswordType(false)
            } else {
                // 비번 작성 후 송금 로직 작성

                // 일반 송금과 축의금 송금 분기 처리를 해야함
                // true인 경우 일반 송금

                var passwordListToString = ""

                list.forEach { password ->
                    passwordListToString += password
                }

                val qrCode_checker = true
                val ledgers = 100
                // 알번 송금 및 qr 송금 분기 처리
                // qrCode_checker로 분기 처리
                if (qrCode_checker == true) {
                    Timber.d("qrCode_checker : $qrCode_checker")
                    remittanceViewModel.postTransfer(true, passwordListToString, ledgers) {
                        navigateDestination(
                            R.id.action_easyPasswordRegisterFragment_to_remittanceFinishFragment,
                            bundle = bundleOf("remittanceCheck" to it)
                        )
                    }
                }
            }
        }
    }


    private fun setUpEasyPassWordCheck(list: List<String>) {
        val count = list.size - 1
        for (i in 0..5) {
            passwordList[i].isSelected = i <= count
        }
        if (list.size == 6) {
            signUpViewModel.checkEasyPasswordEquals()
        }
    }

    private fun observeEasyPasswordCheck() {
        signUpViewModel.easyPasswordType.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.checkType = it
                clearPasswordListSelected()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun clearPasswordListSelected() {
        for (i in 0..5) {
            passwordList[i].isSelected = false
        }
    }

    private fun clearPasswordList() {
        passwordList = mutableListOf()
    }

    private fun observeSignUpUiState() {
        signUpViewModel.signUpUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is SignUpUiState.SignUpEmpty -> {}
                    is SignUpUiState.SignUpLoading -> {}
                    is SignUpUiState.SignUpSuccess -> {
                        if (ShareData.transferType) {
                            val signUpData = signUpViewModel.signUpParam.value
                            Timber.tag("회원가입 로그인").d("$signUpData")
                            signInViewModel.setSignInParam(true, signUpData.email+"@"+signUpData.emailName)
                            signInViewModel.setSignInParam(false, signUpData.password)
                            signInViewModel.singIn()
                        } else {
                            navigateDestination(R.id.action_fragment_easy_password_register_to_fragment_sign_up_success)
                        }

                    }

                    is SignUpUiState.EasyPasswordSuccess -> {
                        signUpViewModel.signUp()
                    }

                    is SignUpUiState.SignUpError -> {
                        Toast.makeText(requireActivity(), "회원가입 오류 ${it.error}", Toast.LENGTH_SHORT).show()
                        signUpViewModel.setSignUpUiState(SignUpUiState.SignUpEmpty)
                        navigatePopBackStack()
                        Timber.d("회원가입 오류 ${it.error}")
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeSignIn(){
        signInViewModel.signInUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is SignInUiState.SignInSuccess -> {
                        navigateDestination(R.id.action_fragment_easy_password_register_to_fragment_sign_up_success)
                    }

                    is SignInUiState.SignInError -> {

                    }

                    else -> {

                    }
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
            this@EasyPasswordRegisterFragment,
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

                    remittanceViewModel.postTransfer(true, "99999999", -1) {
                        navigateDestination(
                            R.id.action_easyPasswordRegisterFragment_to_remittanceFinishFragment,
                            bundle = bundleOf("remittanceCheck" to it)
                        )
                    }
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