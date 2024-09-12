package `in`.crakcode.unittests

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import `in`.crakcode.unittests.db.AppDatabase
import `in`.crakcode.unittests.db.MIGRATION_1_2
import `in`.crakcode.unittests.db.MIGRATION_2_3
import `in`.crakcode.unittests.db.User
import `in`.crakcode.unittests.db.UserDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


class UserDaoTest {

    private val TEST_DB = "migration-test"

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,  // Use the class reference, not the class name
        emptyList(),  // If no custom migration specs are provided
        FrameworkSQLiteOpenHelperFactory()
    )

    @Before
    fun setup() {
        // Initialize the in-memory database before each test
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()

        // Get reference to UserDao
        userDao = db.userDao()
    }

    @After
    fun teardown() {
        // Close the database after each test
        db.close()
    }

    @Test
    fun insertAndGetUser() = runBlocking {
        // Given a User object
        val user = User(id = 1, name = "John", age = 25)

        // When inserting the user into the database
        userDao.insert(user)

        // Then retrieve the user by ID and assert the values are correct
        val retrievedUser = userDao.getUserById(1)
        assertNotNull(retrievedUser)
        assertEquals("John", retrievedUser?.name)
        assertEquals(25, retrievedUser?.age)
    }

    @Test
    fun deleteUser() = runBlocking {
        // Given a User object
        val user = User(id = 1, name = "John", age = 25)

        // When inserting and then deleting the user
        userDao.insert(user)
        userDao.deleteUser(user)

        // Then the user should no longer exist in the database
        val retrievedUser = userDao.getUserById(1)
        assertNull(retrievedUser)
    }

    @Test
    fun getAllUsers() = runBlocking {
        // Given two users
        val user1 = User(id = 1, name = "John", age = 25)
        val user2 = User(id = 2, name = "Jane", age = 30)

        // When inserting the users
        userDao.insert(user1)
        userDao.insert(user2)

        // Then retrieve all users and assert they match the inserted users
        val userList = userDao.getAllUsers()
        assertEquals(2, userList.size)
        assertEquals("John", userList[0].name)
        assertEquals("Jane", userList[1].name)
    }

    @Test
    fun migrate1To2() {
        // Create the database in version 1
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL("INSERT INTO users (id, name) VALUES (1, 'John Doe')")
            close()
        }

        var db: SupportSQLiteDatabase = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        // Verify the 'age' column has been added and data is preserved
        db.query("SELECT id, name, age FROM users WHERE id = 1").use { cursor ->
            assert(cursor.moveToFirst())
            assert(cursor.getInt(0) == 1)  // id
            assert(cursor.getString(1) == "John Doe")  // name
            assert(cursor.isNull(2))  // age should be NULL since we did not provide a value
        }
    }

    // Test for migration from version 2 to 3
    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        // Create the database in version 2
        helper.createDatabase(TEST_DB, 2).apply {
            // Insert some test data
            execSQL("INSERT INTO users (id, name, age) VALUES (1, 'John Doe', 25)")
            close()
        }
        val db: SupportSQLiteDatabase = helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_2_3)

        // Verify the 'addresses' table has been created
        db.query("SELECT * FROM addresses").use { cursor ->
            assert(cursor.columnCount == 3)  // Verify the columns: id, userId, address
        }

        // Ensure the data in 'users' table is still correct
        db.query("SELECT id, name, age FROM users WHERE id = 1").use { cursor ->
            assert(cursor.moveToFirst())
            assert(cursor.getInt(0) == 1)  // id
            assert(cursor.getString(1) == "John Doe")  // name
            assert(cursor.getInt(2) == 25)  // age
        }
    }
}
