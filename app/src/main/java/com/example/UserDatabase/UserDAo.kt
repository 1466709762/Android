package com.example.mychat1.UserDatabase

import androidx.room.*

@Dao
interface UserDao {


    @Update
    fun updateUser(newNote: User)

    @Query("select * from User")
    fun loadAllNotes(): List<User>

    @Query("select * from User where number > :number")
    fun loadNotesLongerThan(number:String) : List<User>

    @Query("select * from User where id == :id")
    fun loadByID(id:Long) :User

    @Delete
    fun deleteUser(note: User)

    @Query("delete from User where name == :name")
    fun deleteNoteByName(name: String): Int

    @Insert
    fun insertNote(note: User)


    @Query("delete from User where id == :id")
    fun deleteNoteById(id:Long): Int

}