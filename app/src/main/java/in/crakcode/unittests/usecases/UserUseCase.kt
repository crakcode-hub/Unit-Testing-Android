package `in`.crakcode.unittests.usecases

import `in`.crakcode.unittests.repo.HomeRepo
import `in`.crakcode.unittests.User

class UserUseCase(private val repo: HomeRepo) {

    fun fetchUserData(): User? {
        return repo.getUserData()
    }

    fun updateUserDetails(user: User): Boolean {
        return repo.saveUserData(user)
    }
}