package com.example.smoke_detector

data class user(val username : String? = null,val email : String? = null,val password : String? = null,val ag_password : String? = null)
data class judgment(val temperature : Int? = null,val humidity : Int? = null)
