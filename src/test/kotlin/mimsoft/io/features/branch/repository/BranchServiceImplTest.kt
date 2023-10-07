package mimsoft.io.features.branch.repository

import io.ktor.server.testing.*
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class BranchServiceImplTest {

    private val branchServiceImpl = BranchServiceImpl

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 7
        val response = branchServiceImpl.getAll(merchantId)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val id: Long = 7
        val response = branchServiceImpl.get(id, merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val textModel = TextModel(
            uz = "StringUz",
            ru = "StringRu",
            eng = "StringEng"
        )
        val branchDto = BranchDto(
            id = null,
            open = "09:00",
            close = "22:00",
            merchantId = 1,
            name = textModel,
            longitude = 100.5,
            latitude = 1.3,
            address = "Tashkent Qorasuv 2",
            distance = null
        )
        val response = branchServiceImpl.add(branchDto)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val textModel = TextModel(
            uz = "StringUz",
            ru = "StringRu",
            eng = "StringEng"
        )
        val branchDto = BranchDto(
            id = 32,
            open = "09:00",
            close = "22:00",
            merchantId = 1,
            name = textModel,
            longitude = 100.5,
            latitude = 1.3,
            address = "Tashkent Qorasuv 3",
            distance = null
        )
        val response = branchServiceImpl.update(branchDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val branchId: Long = 32
        val response = branchServiceImpl.delete(branchId)
        if (response)
            assertTrue(response)
    }

    @Test
    fun getByName() = testApplication {
        val branchName = "StringUz"
        val merchantId: Long = 1
        val response = branchServiceImpl.getByName(branchName, merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun nearestBranch() = testApplication {
        val latitude = 1.3
        val longitude = 100.5
        val merchantId: Long = 1
        val response = branchServiceImpl.nearestBranch(latitude, longitude, merchantId)
        if (response != null)
            assertNotNull(response)
    }
}