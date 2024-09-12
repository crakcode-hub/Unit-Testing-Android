package `in`.crakcode.unittests.complete

import `in`.crakcode.unittests.User
import `in`.crakcode.unittests.api.ApiClient
import `in`.crakcode.unittests.api.ApiService
import junit.framework.TestCase.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class ApiCompleteTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val baseUrl = mockWebServer.url("/").toString()
        apiService = ApiClient.create(baseUrl)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getUser should return valid user data`() {
        // Prepare the mock response
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "id": 1,
                    "name": "Sachin Saxena"
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // Call the API
        val call: Call<User> = apiService.getUser(1)
        val response: Response<User> = call.execute()

        // Verify the response
        assertEquals(200, response.code())
        assertEquals("Sachin Saxena", response.body()?.name)
    }

    @Test
    fun `getUser should return 404 for non-existent user`() {
        // Prepare the mock response for a 404 error
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        // Call the API
        val call: Call<User> = apiService.getUser(999)
        val response: Response<User> = call.execute()

        // Verify the response
        assertEquals(404, response.code())
        assertEquals(null, response.body())
    }
}
