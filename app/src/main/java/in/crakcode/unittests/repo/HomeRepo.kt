package `in`.crakcode.unittests.repo

import `in`.crakcode.unittests.User

interface HomeRepo {
    fun getUserData() : User?
    fun saveUserData(user: User) : Boolean
}




