package com.icodeu.realtimedb

import androidx.lifecycle.ViewModel
import com.icodeu.realtimedb.dto.CarListResponse
import com.icodeu.realtimedb.dto.CarResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class MainViewModel : ViewModel() {
    private var tempList = arrayListOf<Car>()
    private val cars: BehaviorSubject<List<Car>> = BehaviorSubject.createDefault(tempList)
    private lateinit var ioClient: Socket
    val IO_TAG = "SOCKET"

    fun getCars(): Observable<List<Car>> = cars.hide()

    init {
        setUpSocket()?.let {
            ioClient = it
        }
        setUpEvents()
        setUpConnection()

    }

    private fun setUpConnection() {
        ioClient.connect()
    }

    private fun setUpEvents() {
        ioClient.on("connect") {
            Logger(IO_TAG, "socket connected")
            ioClient.emit("welcome", "hello from client")

            ioClient.on("welcome") {
                Logger(IO_TAG, it.toString())
            }

            ioClient.emit("allCars", "Give me the cars!")

            ioClient.on("allCarsResponse") {
                Logger(IO_TAG, "all cars event")
                try {
                    val lists = CarListResponse(it).toCars()
                    tempList.addAll(carListDescending(lists))
                    cars.onNext(tempList)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            ioClient.on("carChanges") {
                try{
                    val response = CarResponse(it)
                    Logger(IO_TAG, "car changes")
                    val newCar = response.toCar()

                    //I'm just interested in insertion event
                    if (response.getOperation() == CarResponse.INSERT_OPERATION){
                        Logger(IO_TAG, "car changes $newCar")
                        tempList.add(newCar)
                        tempList = carListDescending(tempList)
                        cars.onNext(tempList)
                    }

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setUpSocket(): Socket? {
        return try {
            IO.socket("http://192.168.0.5:5000")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return null
        }
    }

    private fun carListDescending(list: List<Car>):ArrayList<Car>{
        val arr = arrayListOf<Car>()
        arr.addAll(list)
        arr.sortByDescending {
            it.id
        }
        return arr
    }

    override fun onCleared() {
        super.onCleared()
        ioClient.disconnect()
    }
}