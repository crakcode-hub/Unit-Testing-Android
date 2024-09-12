package `in`.crakcode.unittests.complete

import `in`.crakcode.unittests.repo.HomeRepo
import `in`.crakcode.unittests.repo.HomeRepoImplementation
import `in`.crakcode.unittests.User
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import `in`.crakcode.unittests.usecases.UserUseCase
import `in`.crakcode.unittests.viewmodel1.MainActivityViewModelWithUseCase

class MainActivityWithUseCaseAndRepoTestComplete {

    private lateinit var viewModel: MainActivityViewModelWithUseCase
    private lateinit var useCase: UserUseCase
    private lateinit var repo: HomeRepo

    @Before
    fun setup() {
        repo = mockk()
        useCase = mockk()
        viewModel = MainActivityViewModelWithUseCase(useCase)
    }

    // 4. Spy test for getUserDetails
    @Test
    fun testGetUserDetails_Spy() {
        val repoSpy : HomeRepo = spyk<HomeRepoImplementation>()
        val useCase = UserUseCase(repoSpy)

        val result = useCase.fetchUserData()

        assertEquals("123", result?.id)
        assertEquals("Tarun", result?.name)
        assertEquals(34, result?.age)
        verify { useCase.fetchUserData() }
    }

    @Test
    fun testGetUserDetails_Mockk() {
        val repoSpy : HomeRepo = spyk<HomeRepoImplementation>()
        val useCase = UserUseCase(repoSpy)

        every { repoSpy.getUserData() } returns User("12","Sachin", 100)

        val result = useCase.fetchUserData()

        assertEquals("12", result?.id)
        assertEquals("Sachin", result?.name)
        assertEquals(100, result?.age)
        verify { useCase.fetchUserData() }
    }

    // 5. Mock test for getUserDetails with UseCase
    @Test
    fun testGetUserDetails_MockWithUseCase() {
        val mockUser = User("harshad-123", "Harshad", 60)
        every { useCase.fetchUserData() } returns mockUser

        val result = viewModel.getUserDetails()

        assertEquals(mockUser, result)
        verify { useCase.fetchUserData() }
    }

    // 6. Stub test for saveUserDetails
    @Test
    fun testSaveUserDetails_Stub() {
        val mockUser = User("Agam-ind", "Agamjyot", 90)
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
        val spyViewModel = MainActivityViewModelWithUseCase(userUseCase)
        val mockUser = User("8765", "John Doe", 8)

        every { spyRepo.saveUserData(mockUser) } returns true

        val result = spyViewModel.saveUserDetails(mockUser)

        assertTrue(result)
        verify { spyRepo.saveUserData(mockUser) }
    }
}
