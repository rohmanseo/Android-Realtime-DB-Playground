
const mongoose = require("mongoose")
const carModel = require("./db/carModel")
const DB = "cardb";
const DBURL = "mongodb://localhost:27018/" + DB


mongoose.connect(DBURL, (error) => {
    if (error) {
        console.log("db connection error")
    }else {
        for(var i=0;i<20;i++){
            carModel.create({
                name: "Car name " +i,
                image: "https://cdn.pixabay.com/photo/2015/04/14/09/13/ferrari-721858_960_720.jpg"
            }).then(r  =>{
                console.log("added")
            })
        }
    }
})