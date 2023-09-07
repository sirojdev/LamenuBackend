package mimsoft.io.features.branch.repository

import io.ktor.server.testing.*
import mimsoft.io.features.branch.BranchDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class BranchServiceImplTest {

    val branchServiceImplObject = BranchServiceImpl

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 7
        val response = branchServiceImplObject.getAll(merchantId)
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val id: Long = 7
        val response = branchServiceImplObject.get(id, merchantId)
        assertNotNull(response)
    }

    @Test
    fun add() {

    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun getByName() {
    }

    @Test
    fun nearestBranch() {
    }
}