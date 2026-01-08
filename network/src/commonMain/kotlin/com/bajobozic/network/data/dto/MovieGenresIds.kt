package com.bajobozic.network.data.dto

data class MovieGenresIds<ONE, TWO, THREE>(
    val listOne: List<ONE>,
    val listTwo: List<TWO>,
    val listThree: List<List<THREE>>
)
