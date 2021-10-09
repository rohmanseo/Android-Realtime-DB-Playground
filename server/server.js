const express = require("express");
const {createServer} = require("http");
const {Server} = require("socket.io");
const mongoose = require("mongoose")
const carModel = require("./db/carModel")
const DB = "cardb";
const DBURL = "mongodb://localhost:27018/" + DB

const app = express();
const httpServer = createServer(app);
const io = new Server(httpServer, {
    cors: {
        origin: "https://192.168.0.5:5000",
        methods: ["GET", "POST"]
    },
    maxHttpBufferSize: 1e8
});

var responseChange = {
    operationType:"",
    data:""
}
mongoose.connect(DBURL, (error) => {
    if (error) {
        console.log("db connection error")
    } else {
        console.log("db is connected")

        carModel.watch()
            .on('change', data => {
                responseChange.operationType = data.operationType
                responseChange.data = data.fullDocument
                console.log(data)
                if (data.operationType === "delete"){
                    responseChange.data = data.documentKey
                }
                console.log("car model changed" + JSON.stringify(responseChange))
                io.emit('carChanges',responseChange)
            })
    }
})

io.on("connection", (socket) => {
    console.log("socket connection...")

    socket.on('welcome', (data) => {
        console.log(data)
    })
    socket.emit("welcome", "hello from server")

    socket.on('allCars',(data)=>{
        carModel.find()
            .exec((err,data)=>{
                console.log(data)
                socket.emit("allCarsResponse",data)
            })
    })

});


httpServer.listen(5000, () => {
    console.log("server is listening on port 5000")
});
