package `in`.crakcode.unittests.complete

import `in`.crakcode.unittests.HomeRepo
import `in`.crakcode.unittests.User
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import `in`.crakcode.unittests.usecases.UserUseCase
import `in`.crakcode.unittests.viewmodel2.MainActivityViewModelWithUseCaseAndRepo

class MainActivityWithUseCaseAndRepoTestComplete {

    private lateinit var viewModel: MainActivityViewModelWithUseCaseAndRepo
    private lateinit var useCase: UserUseCase
    private lateinit var repo: HomeRepo

    @Before
    fun setup() {
        repo = mockk()
        useCase = mockk()
        viewModel = MainActivityViewModelWithUseCaseAndRepo(useCase)
    }

    // 4. Spy test for getUserDetails
    @Test
    fun testGetUserDetails_Spy() {
        val spyUseCase = spyk(useCase)
        val spyViewModel = MainActivityViewModelWithUseCaseAndRepo(spyUseCase)
        val mockUser = User("234", "John Doe", 70)
        every { spyUseCase.fetchUserData() } returns mockUser

        val result = spyViewModel.getUserDetails()
        assertEquals(mockUser, result)
        verify { spyUseCase.fetchUserData() }
    }

    // 5. Mock test for getUserDetails with UseCase
    @Test
    fun testGetUserDetails_MockWithUseCase() {
        val mockUser = User("jkdfdf", "Jane Doe", 60)
        every { useCase.fetchUserData() } returns mockUser

        val result = viewModel.getUserDetails()

        assertEquals(mockUser, result)
        verify { useCase.fetchUserData() }
    }

    // 6. Stub test for saveUserDetails
    @Test
    fun testSaveUserDetails_Stub() {
        val mockUser = User("09877", "Sidharth", 90)
        every { useCase.updateUserDetails(mockUser) } returns true

        val result = viewModel.saveUserDetails(mockUser)

        assertTrue(result)
        verify { useCase.updateUserDetails(mockUser) }
    }

    // 7. Spy test for saveUserDetails
    @Test
    fun testSaveUserDetails_Spy() {
        val spyRepo = spyk(repo)
        val userUseCase = UserUseCase(spyRepo)
        val spyViewModel = MainActivityViewModelWithUseCaseAndRepo(userUseCase)
        val mockUser = User("8765", "John Doe", 8)

        every { spyRepo.saveUserData(mockUser) } returns true

        val result = spyViewModel.saveUserDetails(mockUser)

        assertTrue(result)
        verify { spyRepo.saveUserData(mockUser) }
    }
}
