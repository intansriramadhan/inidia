package capstone.project.mushymatch.api.repository

import capstone.project.mushymatch.util.MockData.dummyMushroomDetailData
import capstone.project.mushymatch.util.MockData.dummyMushroomResponseItem
import capstone.project.mushymatch.util.MockData.dummyRecipesData
import capstone.project.mushymatch.util.MockData.dummyReciptDetail
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MushroomRepositoryTest {

    private val repository: IMushroomRepository = mock()

    @Test
    fun `When Get Mushroom Called Should Return Expected List`() {
        // Mock the expected data
        val expectedData = dummyMushroomResponseItem()
        runTest {
            whenever(repository.getMushrooms()).thenReturn(Result.Success(expectedData))
            val response = repository.getMushrooms()
            assertEquals(Result.Success(expectedData), response)
        }
    }

    @Test
    fun `When Get Mushroom Detail Called Should Return Expected Data`() {
        val expectedData = dummyMushroomDetailData()
        runTest {
            whenever(repository.getMushroomDetail(1)).thenReturn(Result.Success(expectedData))
            val response = repository.getMushroomDetail(1)
            assertEquals(Result.Success(expectedData), response)
        }
    }

    @Test
    fun `When Get Recipes Called Should Return Expected List`() {
        val expectedData = dummyRecipesData()
        runTest {
            whenever(repository.getRecipes(1)).thenReturn(Result.Success(expectedData))
            val response = repository.getRecipes(1)
            assertEquals(Result.Success(expectedData), response)
        }
    }
    @Test
    fun `When Get Recipes Detail Called Should Return Expected Data`() {
        val expectedData = dummyReciptDetail()
        runTest {
            whenever(repository.getRecipeDetail(1)).thenReturn(Result.Success(expectedData))
            val response = repository.getRecipeDetail(1)
            assertEquals(Result.Success(expectedData), response)
        }
    }
}