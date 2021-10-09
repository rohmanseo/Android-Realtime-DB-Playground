package com.icodeu.realtimedb.dto

import com.icodeu.realtimedb.Car
import org.json.JSONArray
import org.json.JSONObject

class CarListResponse(val array: Array<Any>) {

    private val lists = mutableListOf<Car>()

    fun toCars(): List<Car> {
        lists.clear()

        val jsonArr = JSONArray(array).getJSONArray(0)

        (0 until jsonArr.length()).forEach {

            val jsonObj = jsonArr.getJSONObject(it)
            val id = jsonObj.getString("_id")
            val name = jsonObj.getString("name")
            val image = jsonObj.getString("image")

            lists.add(Car(id, name, image))
        }
        return lists
    }

}