package capstone.project.mushymatch.api

import capstone.project.mushymatch.api.response.PredictImageResponse
import capstone.project.mushymatch.api.response.mushroom.*
import capstone.project.mushymatch.api.response.recipe.*
import capstone.project.mushymatch.api.response.mushroom.DetailMushroomResponse
import capstone.project.mushymatch.api.response.recipe.DetailRecipesResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("get-jamur")
    suspend fun getMushrooms(): List<GetMushroomResponseItem>

    @GET("jamur/{id_jamur}")
    suspend fun getMushroomDetail(@Path("id_jamur") id: Int): DetailMushroomResponse

    @GET("list-recipes/{id_jamur}")
    suspend fun getRecipes(@Path("id_jamur") id: Int): List<ListRecipesResponseItem>

    @GET("recipes/{id_recipe}")
    suspend fun getRecipeDetail(@Path("id_recipe") id: Int): DetailRecipesResponse

    @Multipart
    @POST("predict/image")
    suspend fun predictImage(@Part image: MultipartBody.Part): PredictImageResponse

}
