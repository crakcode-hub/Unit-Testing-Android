package `in`.crakcode.unittests

interface HomeRepo {
    fun getUserData() : User?
    fun saveUserData(user: User) : Boolean
}




