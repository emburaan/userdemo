package com.app.userinputdemo.data

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface DaoUser {
    @Query("select * from user")
    fun getAllUsers(): Flowable<List<User>>

    @Query("select * from user where id in (:id)")
    fun getUserById(id: Int):Flowable<User>

    @Query("delete from user")
    fun deleteAllUsers()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(car: User)

    @Update
    fun updateUser(car: User)

    @Delete
    fun deleteUser(car: User)

}