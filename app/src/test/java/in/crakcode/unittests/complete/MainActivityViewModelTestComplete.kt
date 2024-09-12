package `in`.crakcode.unittests.complete

import `in`.crakcode.unittests.repo.HomeRepo
import `in`.crakcode.unittests.viewmodel1.MainActivityViewModel
import `in`.crakcode.unittests.User
import org.junit.Test
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before


class MainActivityViewModelTestComplete {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var repo: HomeRepo

    @Before
    fun setup() {
        repo = mockk()
        viewModel = MainActivityViewModel(repo)
    }

    @Test
    fun testSumOfTwoNumber_Normal() {
        val result = viewModel.sumOfTwoNumber(3, 5)
        assertEquals(8, result)
    }


    @Test
    fun testSumOfTwoNumber_Mock() {

        val mockViewModel = mockk<MainActivityViewModel>(relaxed = true)
        every { mockViewModel.sumOfTwoNumber(10,12) } returns 12

        val result1 = mockViewModel.sumOfTwoNumber(10, 12)
        val result2 = mockViewModel.sumOfTwoNumber(50, 12)
        assertEquals(12, result1)
        assertEquals(0, result2)
    }


    @Test
    fun testMultiplicationOfTwoNumber_Mock() {
        val mockViewModel = mockk<MainActivityViewModel>(relaxed = true)

        every { mockViewModel.multiplicationOfTwoNumber(2, 3) } returns 6

        val result = mockViewModel.multiplicationOfTwoNumber(2, 3)

        assertEquals(6, result)

        verify { mockViewModel.multiplicationOfTwoNumber(2, 3) }
    }

    @Test
    fun testDivideTwoNumbers_Stub() {
        val spyViewModel = spyk(viewModel)

        every { spyViewModel.divideTwoNumbers(10, 2) } returns 5

        val result = spyViewModel.divideTwoNumbers(10, 2)

        assertEquals(5, result)

        // Verify it was called
        verify { spyViewModel.divideTwoNumbers(10, 2) }
    }


    @Test
    fun testGetUserDetails_Spy() {

        val spyRepo = spyk(repo)
        val spyViewModel = MainActivityViewModel(spyRepo)


        val mockUser = User("UserId", "Vinit", 27)
        every { spyRepo.getUserData() } returns mockUser

        val result = spyViewModel.getUserDetails()

        assertEquals(mockUser, result)

        verify { spyRepo.getUserData() }
    }


}