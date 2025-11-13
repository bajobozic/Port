package com.bajobozic.port.home.data.locale.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bajobozic.port.home.data.locale.entity.GenreEntity
import core.data.local.entity.MovieEntity
import core.data.local.entity.MovieGenreCrossRef
import home.data.local.db.MovieWithGenres
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    //**********************************************************************************************
    //PAGING 3 start
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenreCrosRef(movieGenreCrossRef: MovieGenreCrossRef)

    @Delete
    suspend fun deleteAllMovieGenreCrossRef(movieGenreCrossRef: List<MovieGenreCrossRef>)

    @Query("SELECT * FROM moviegenrecrossref WHERE movie_id == :movieId")
    suspend fun getMovieGenreCrossRef(movieId: Int): List<MovieGenreCrossRef>

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("SELECT MAX(current_page) FROM movies")
    suspend fun getMaxCurrentPage(): Int?

    @Query("SELECT * FROM movies WHERE movie_id == :id")
    suspend fun getMovie(id: Int): MovieEntity?

    @Transaction
//    @Query("SELECT * FROM movies WHERE movie_id == :movieId")
    @Query("SELECT m.*,mg.*,g.* FROM movies AS m INNER JOIN moviegenrecrossref AS mg ON m.movie_id == mg.movie_id INNER JOIN genres as g ON g.genre_id == mg.genre_id WHERE m.movie_id == :movieId")
    fun getMovieFlow(movieId: Int): Flow<MovieWithGenres>

    @Delete
    @Transaction
    suspend fun deleteMovie(movieId: Int) {
        getMovie(movieId)?.let {
            deleteMovie(it)
            deleteAllMovieGenreCrossRef(getMovieGenreCrossRef(movieId))
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    suspend fun insertAll(
        list: List<MovieEntity>,
        genresList: List<GenreEntity>,
        genreIdsPerMovie: List<List<Int>>
    ) {
        val movieIdsList = insertMovies(list)
        insertGenres(genresList)
        movieIdsList.forEachIndexed { index, id ->
            val movieGenresIdsList = genreIdsPerMovie[index]
            movieGenresIdsList.forEach { genreId ->
                val movieGenreCrossRef = MovieGenreCrossRef(id.toInt(), genreId)
                insertMovieGenreCrosRef(movieGenreCrossRef)
            }

        }
    }

   /* @Transaction
    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun pagingSource(): PagingSource<Int, MovieWithGenres>*/

    @Delete
    @Transaction
    suspend fun clearAll() {
        clearAllMoviesGenreCrossref()
        clearAllMovies()
    }

    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()

    @Query("DELETE FROM moviegenrecrossref")
    suspend fun clearAllMoviesGenreCrossref()
    //PAGING 3 end
    //**********************************************************************************************

}