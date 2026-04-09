package com.bajobozic.network.data.dto

internal data class MovieGenresIds<ONE, TWO, THREE>(
    val listOne: List<ONE>,
    val listTwo: List<TWO>,
    val listThree: List<List<THREE>>
)
