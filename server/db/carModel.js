const mongoose = require('mongoose')

const {Schema} = mongoose

const carSchema = new Schema({
    name: String,
    image: String
}, {collection: "cars"})

module.exports = mongoose.model('Car', carSchema)