package capstone.project.mushymatch.api.response.mushroom

import com.google.gson.annotations.SerializedName

data class DetailMushroomResponse(

	@field:SerializedName("habitat")
	val habitat: String,

	@field:SerializedName("id_jamur")
	val idJamur: Int,

	@field:SerializedName("latin_name")
	val latinName: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("picture")
	val picture: String,

	@field:SerializedName("status")
	val status: String
)
