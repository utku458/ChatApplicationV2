package com.example.chatapplication

class Message {

    var username:String?=null
    var message:String?=null
    var senderId:String?=null

    constructor(){

    }

    constructor(username:String?,message: String? , senderId:String?){
        this.username = username
        this.message = message
        this.senderId=senderId
    }








}