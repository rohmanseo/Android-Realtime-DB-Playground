const mongoose = require("mongoose")
const carModel = require("./db/carModel")
const DB = "cardb";
const DBURL = "mongodb://localhost:27018/" + DB


mongoose.connect(DBURL, (error) => {
    if (error) {
        console.log("db connection error")
    } else {
        var i = 1
        setInterval(() => {
            carModel.create({
                name: "New car name " + i,
                image: "https://cdn.pixabay.com/photo/2015/04/14/09/13/ferrari-721858_960_720.jpg"
            }).then(r => {
                console.log("added")
            })
            i++
        }, 3000)

    }
})