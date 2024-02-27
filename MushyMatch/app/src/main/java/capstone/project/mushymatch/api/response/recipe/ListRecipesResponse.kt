package capstone.project.mushymatch.api.response.recipe

import com.google.gson.annotations.SerializedName

data class ListRecipesResponse(
	@SerializedName("ListRecipesResponse")
	val listRecipesResponse: List<ListRecipesResponseItem>
)


data class ListRecipesResponseItem(

	@field:SerializedName("name_jamur")
	val nameJamur: String,

	@field:SerializedName("pict_recipe")
	val pictRecipe: String,

	@field:SerializedName("name_recipe")
	val nameRecipe: String,

	@field:SerializedName("id_recipe")
	val idRecipe: Int
)
