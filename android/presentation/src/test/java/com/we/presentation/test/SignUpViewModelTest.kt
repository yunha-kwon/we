package com.we.presentation.test

import com.data.repository.SignRepository
import com.we.model.SignUpParam
import com.we.presentation.sign.model.SignUpUiState
import com.we.presentation.sign.viewmodel.SignUpViewModel
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SignUpViewModelTest {

    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var signRepository: SignRepository


    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        signRepository = mockk()
        signUpViewModel = SignUpViewModel(signRepository)
    }

    @Test
    fun `이메일,비밀번호,닉네임 다 있는 경우`() = runTest {
        //given : 이메일, 패스워드, 닉네임 다 있는 경우
        signUpViewModel.setEmail("123")
        signUpViewModel.setEmailName("1424")
        signUpViewModel.setPassword("14242")
        signUpViewModel.setNickName("242424")


        //when : 다음 버튼이 활성화 되어야 한다.
        signUpViewModel.isSignUpParamValid(signUpViewModel.signUpParam.value)
        val checkNextButton = signUpViewModel.nextButtonActivate.first()

        //then : 다음 버튼 활성화가 true로 변경되었는지 확인
        assertEquals(checkNextButton, true)
    }

    @Test
    fun `비밀번호,닉네임만 있는 경우`() = runTest {
        //given : 패스워드, 닉네임만 있는 경우
        signUpViewModel.setPassword("14242")
        signUpViewModel.setNickName("242424")


        //when : 다음 버튼이 비활성화 되어야 한다.
        signUpViewModel.isSignUpParamValid(signUpViewModel.signUpParam.value)
        val checkNextButton = signUpViewModel.nextButtonActivate.first()

        //then : 다음 버튼 활성화가 false로 변경되었는지 확인
        assertEquals(checkNextButton, false)
    }

    @Test
    fun `이메일,닉네임 만 있는 경우`() = runTest {
        //given : 이메일, 닉네임 만 있는 경우
        signUpViewModel.setEmail("123")
        signUpViewModel.setEmailName("1424")
        signUpViewModel.setNickName("242424")


        //when : 다음 버튼이 비활성화 되어야 한다.
        signUpViewModel.isSignUpParamValid(signUpViewModel.signUpParam.value)
        val checkNextButton = signUpViewModel.nextButtonActivate.first()

        //then : 다음 버튼 활성화가 false로 변경되었는지 확인
        assertEquals(checkNextButton, false)
    }

    @Test
    fun `닉네임이 빈 경우,이메일,비밀번호만 있는 경우`() = runTest {
        //given : 이메일, 닉네임 만 있는 경우
        signUpViewModel.setEmail("123")
        signUpViewModel.setEmailName("1424")
        signUpViewModel.setPassword("242424")


        //when : 다음 버튼이 비활성화 되어야 한다.
        signUpViewModel.isSignUpParamValid(signUpViewModel.signUpParam.value)
        val checkNextButton = signUpViewModel.nextButtonActivate.first()

        //then : 다음 버튼 활성화가 false로 변경되었는지 확인
        assertEquals(checkNextButton, false)
    }

    @Test
    fun `회원가입 뒤로가기시 기존 Parameter 초기화`() = runTest {
        //given : 이메일, 닉네임, 비밀번호 데이터 넣기
        signUpViewModel.apply {
            setEmail("123")
            setEmailName("1244")
            setPassword("2424")
        }

        //when : 뒤로가기 버튼 클릭시 초기화
        signUpViewModel.clearSignUpParam()

        //then : 전체 데이터가 초기화되어야 함 그리고 버튼 비활성화
        val data = signUpViewModel.signUpParam.value
        signUpViewModel.isSignUpParamValid(signUpViewModel.signUpParam.value)
        val buttonStatus = signUpViewModel.nextButtonActivate.first()


        assertEquals(data, SignUpParam.DEFAULT)
        assertEquals(buttonStatus, false)
    }

    @Test
    fun `간편 비밀번호 입력시에 리스트에 하나씩 추가`() {
        //given : 빈 리스트
        val list = listOf("2")

        //when
        signUpViewModel.addRemoveEasyPassword(true, "2")

        //then
        val data = signUpViewModel.signUpParam.value.easyPassword
        assertEquals(data, list)
    }

    @Test
    fun `간편 비밀번호 하나씩 삭제`() {
        //given : 리스트에 2,4,6이 들어간 상태
        val list = listOf("2", "4")
        signUpViewModel.apply {
            addRemoveEasyPassword(true, "2")
            addRemoveEasyPassword(true, "4")
            addRemoveEasyPassword(true, "6")
        }

        //when : 비밀 번호 삭제
        signUpViewModel.addRemoveEasyPassword(false)

        //then
        val data = signUpViewModel.signUpParam.value.easyPassword
        assertEquals(data, list)
    }

    @Test
    fun `간편 비밀번호 및 간편 비밀번호 확인 값 비교`() = runTest {
        //given : 간편 비밀 번호, 간편 비밀 번호 확인 값 입력
        val list = listOf("1", "2", "3", "4", "5")
        list.forEach {
            signUpViewModel.addRemoveEasyPassword(true, it)
            signUpViewModel.addRemoveEasyPasswordCheck(true, it)
        }

        //when : 값 입력

        signUpViewModel.checkEasyPasswordEquals()
        val uiState = signUpViewModel.signUpUiState.value

        //then : true 값 나오게
        assertEquals(uiState, SignUpUiState.EasyPasswordSuccess)
    }

    @Test
    fun `간편 비밀번호 뒤로가기 시에 기존 설정한 간편 비밀번호 초기화`() = runTest {
        // given : 간편 비밀번호 뒤로가기

        // when : 간편 비밀번호 뒤로가기시

        // then : 간편 비밀번호 초기화
    }

}