package com.bajobozic.port.home.data.remote.dto

data class ManyToManyDto<ONE, TWO, THREE>(
    val listOne: List<ONE>,
    val listTwo: List<TWO>,
    val listThree: List<List<THREE>>
)
