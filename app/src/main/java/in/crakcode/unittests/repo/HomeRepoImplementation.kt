package `in`.crakcode.unittests.repo

import `in`.crakcode.unittests.User


class HomeRepoImplementation : HomeRepo {

    override fun getUserData(): User? {
       return User("123", "Tarun", 34)
    }

    override fun saveUserData(user: User): Boolean {
        // save user data
        return true
    }
}