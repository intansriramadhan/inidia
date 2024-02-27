package capstone.project.mushymatch.api.response.recipe

import com.google.gson.annotations.SerializedName

data class DetailRecipesResponse(

	@field:SerializedName("pict_recipe")
	val pictRecipe: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@field:SerializedName("name_recipe")
	val nameRecipe: String,

	@field:SerializedName("video")
	val video: String,

	@field:SerializedName("steps")
	val steps: String,

	@field:SerializedName("id_recipe")
	val idRecipe: Int
)
