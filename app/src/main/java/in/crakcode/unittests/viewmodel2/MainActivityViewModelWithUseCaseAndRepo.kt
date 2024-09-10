package `in`.crakcode.unittests.viewmodel2

import androidx.lifecycle.ViewModel
import `in`.crakcode.unittests.User
import `in`.crakcode.unittests.usecases.UserUseCase

class MainActivityViewModelWithUseCaseAndRepo(private val useCase: UserUseCase) : ViewModel() {

    fun getUserDetails(): User? {
        return useCase.fetchUserData()
    }

    fun saveUserDetails(user: User): Boolean {
        return useCase.updateUserDetails(user)
    }
}