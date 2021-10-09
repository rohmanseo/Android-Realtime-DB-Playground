package com.icodeu.realtimedb.dto

import com.icodeu.realtimedb.Car
import org.json.JSONObject

class CarResponse(val array: Array<Any>) {

    companion object {
        const val INSERT_OPERATION = "insert"
        const val UPDATE_OPERATION = "update"
        const val DELETE_OPERATION = "replace"
    }

    private lateinit var operation: String
    fun getOperation(): String {
        return operation
    }

    fun toCar(): Car {
        val jsonObj = (array[0]) as JSONObject
        operation = jsonObj.getString("operationType")
        val carObj = jsonObj.getJSONObject("data")

        val id = carObj.getString("_id")
        val name = carObj.getString("name")
        val image = carObj.getString("image")

        return Car(id, name, image)
    }
}