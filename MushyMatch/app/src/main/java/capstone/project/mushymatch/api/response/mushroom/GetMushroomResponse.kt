package capstone.project.mushymatch.api.response.mushroom

import com.google.gson.annotations.SerializedName

data class GetMushroomResponse(

	@field:SerializedName("GetMushroomResponse")
	val getMushroomResponse: List<GetMushroomResponseItem>
)

data class GetMushroomResponseItem(

	@field:SerializedName("habitat")
	val habitat: String,

	@field:SerializedName("latin_name")
	val latinName: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("pict")
	val pict: String,

	@field:SerializedName("status")
	val status: String
)
