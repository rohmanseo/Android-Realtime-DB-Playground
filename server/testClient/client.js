const io = require("socket.io-client");
const socket = io("http://192.168.0.5:5000");


socket.on("connect", () => {
    console.log("socket connected")
    console.log(socket.id);
    socket.on('welcome', (data) => {
        console.log(data)
    })
    socket.emit("welcome", "hello from client")

    socket.emit('allCars',"get all cars")
    socket.on('allCarsResponse',(data)=>{
        console.log(data)
    })

    socket.on('carChanges', (data)=>{
        console.log("car collection has been changed")
        console.log(JSON.stringify(data))
    })

});

socket.on("disconnect", () => {
    console.log("socket disconnected")
    console.log(socket.id);
});
