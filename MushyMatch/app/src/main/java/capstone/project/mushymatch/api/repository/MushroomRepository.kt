package capstone.project.mushymatch.api.repository

import capstone.project.mushymatch.api.ApiService
import capstone.project.mushymatch.api.response.mushroom.DetailMushroomResponse
import capstone.project.mushymatch.api.response.mushroom.GetMushroomResponseItem
import capstone.project.mushymatch.api.response.recipe.DetailRecipesResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponseItem

class MushroomRepository(private val apiService: ApiService): IMushroomRepository {
    override suspend fun getMushrooms(): Result<List<GetMushroomResponseItem>> {
        return try {
            val response = apiService.getMushrooms()
            Result.Success(response )
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    override suspend fun getMushroomDetail(id: Int): Result<DetailMushroomResponse> {
        return try {
            val response = apiService.getMushroomDetail(id)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    override suspend fun getRecipes(id: Int): Result<List<ListRecipesResponseItem>> = try {
        val response = apiService.getRecipes(id)
        Result.Success(response)
    } catch (e: Exception) {
        Result.Error("Error occurred: ${e.message}")
    }

    override suspend fun getRecipeDetail(id: Int): Result<DetailRecipesResponse> = try {
        val response = apiService.getRecipeDetail(id)
        Result.Success(response)
    } catch (e: Exception) {
        Result.Error("Error occurred: ${e.message}")
    }
}

interface IMushroomRepository {
    suspend fun getMushrooms(): Result<List<GetMushroomResponseItem>>
    suspend fun getMushroomDetail(id: Int): Result<DetailMushroomResponse>
    suspend fun getRecipes(id: Int): Result<List<ListRecipesResponseItem>>
    suspend fun getRecipeDetail(id: Int): Result<DetailRecipesResponse>
}


