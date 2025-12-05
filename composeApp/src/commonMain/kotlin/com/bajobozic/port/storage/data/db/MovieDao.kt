package com.bajobozic.port.storage.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import androidx.room.Transaction
import com.bajobozic.port.storage.data.entity.GenreEntity
import com.bajobozic.port.storage.data.entity.MovieEntity
import com.bajobozic.port.storage.data.entity.MovieGenreCrossRef
import com.bajobozic.port.storage.data.entity.MovieWithGenres
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    //**********************************************************************************************
    //PAGING 3 start
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(movies: List<MovieEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGenres(genres: List<GenreEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
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
    @Suppress(RoomWarnings.QUERY_MISMATCH)
    @Query("SELECT m.*,mg.*,g.* FROM movies AS m INNER JOIN moviegenrecrossref AS mg ON m.movie_id == mg.movie_id INNER JOIN genres as g ON g.genre_id == mg.genre_id WHERE m.movie_id == :movieId ORDER BY popularity DESC")
    fun getMovieFlow(movieId: Int): Flow<MovieWithGenres>

    @Query("SELECT * FROM genres")
    suspend fun getAllGenres(): List<GenreEntity>

    @Delete
    @Transaction
    suspend fun deleteMovie(movieId: Int) {
        getMovie(movieId)?.let {
            deleteMovie(it)
            deleteAllMovieGenreCrossRef(getMovieGenreCrossRef(movieId))
        }
    }

    @Transaction
    suspend fun insertAll(
        list: List<MovieEntity>,
        genresList: List<GenreEntity>,
        genreIdsPerMovie: List<List<Int>>
    ) {
        val movieIdsList = insertMovies(list)
        insertGenres(genresList)
        val crossRefs = movieIdsList.flatMapIndexed { index, id ->
            genreIdsPerMovie[index].map { genreId ->
                MovieGenreCrossRef(id.toInt(), genreId)
            }
        }
        crossRefs.forEach { insertMovieGenreCrosRef(it) }
    }

    @Transaction
    suspend fun deleteThenInsertAll(
        list: List<MovieEntity>,
        genresList: List<GenreEntity>,
        genreIdsPerMovie: List<List<Int>>
    ) {
        clearAll()
        insertAll(list, genresList, genreIdsPerMovie)
    }

    @Transaction
    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun pagingSource(): PagingSource<Int, MovieWithGenres>

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