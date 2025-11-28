package com.bajobozic.port.storage.domain.model

import com.bajobozic.port.storage.data.entity.MovieWithGenres
import com.bajobozic.port.storage.data.entity.toModel

interface GetMovieWithGenres {
    fun asMovie(movieWithGenres: MovieWithGenres): Movie {
        return movieWithGenres.toModel()
    }
}