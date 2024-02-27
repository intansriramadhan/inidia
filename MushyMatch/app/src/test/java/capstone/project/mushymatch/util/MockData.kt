package capstone.project.mushymatch.util

import capstone.project.mushymatch.api.response.mushroom.DetailMushroomResponse
import capstone.project.mushymatch.api.response.mushroom.GetMushroomResponseItem
import capstone.project.mushymatch.api.response.recipe.DetailRecipesResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponseItem


object MockData {
    fun dummyMushroomResponseItem(): List<GetMushroomResponseItem> {
        val data = mutableListOf<GetMushroomResponseItem>()

        for (i in 1..5) {
            val mushroom = GetMushroomResponseItem(
                habitat = "pertinax $i",
                latinName = "Adam Cervantes $i",
                name = "Henrietta Pope $i",
                description = "nulla $i",
                id = 8363 + i,
                pict = "sed $i",
                status = "quas $i"

            )
            data.add(mushroom)
        }

        return data
    }

    fun dummyMushroomDetailData(): DetailMushroomResponse {
        return DetailMushroomResponse(
            habitat = "id",
            idJamur = 9436,
            latinName = "Alana Taylor",
            name = "Emile Guy",
            description = "nunc",
            picture = "eirmod",
            status = "vix"
        )
    }

    fun dummyRecipesData(): List<ListRecipesResponseItem> {
        val data = mutableListOf<ListRecipesResponseItem>()

        for (i in 1..5) {
            val receipt = ListRecipesResponseItem(
                nameJamur = "Alma Small $i",
                pictRecipe = "habeo $i",
                nameRecipe = "Ramon Hebert $i",
                idRecipe = 6433 + 1
            )
            data.add(receipt)
        }
        return data
    }

    fun dummyReciptDetail(): DetailRecipesResponse {
        return DetailRecipesResponse(
            pictRecipe = "bibendum",
            ingredients = "quisque",
            nameRecipe = "Riley Swanson",
            video = "ipsum",
            steps = "ornare",
            idRecipe = 5426
        )
    }
}