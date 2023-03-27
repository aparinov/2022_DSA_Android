package com.example.student_assistant.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.student_assistant.data.local.entity.CardDB
import com.example.student_assistant.data.local.entity.ProjectDB

@Dao
interface CardDao {

    @Query("SELECT * FROM card")
    suspend fun getAll(): List<CardDB>

    @Query("SELECT * FROM card WHERE id = :id")
    suspend fun getCardById(id: String): CardDB

    @Insert
    suspend fun addCard(card: CardDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCard(card: CardDB)

    @Delete
    suspend fun deleteCard(card: CardDB)

    @Query("SELECT * FROM " +
            "card JOIN project ON card.project_id = project.id " +
            "WHERE name LIKE :name OR description LIKE :description")
    suspend fun getCardsByStringFilter(name: String, description: String): Map<CardDB, List<ProjectDB>>

    @Query("SELECT * FROM " +
            "card JOIN project ON card.project_id = project.id " +
            "WHERE name LIKE :name AND " +
            "description LIKE :description " +
            "AND start_date < :startDate AND " +
            "end_date > :endDate AND " +
            "student_number > :minStudentNumber AND " +
            "student_number < :maxStudentNumber AND " +
            "due_date > :minDueDate AND " +
            "due_date < :maxDueDate AND " +
            "status == :status")
    suspend fun getCardsByFullFilter(name: String, description: String, startDate: Long,
                         endDate: Long, minStudentNumber: Int, maxStudentNumber: Int,
                         minDueDate: Long, maxDueDate: Long, status: Int): Map<CardDB, List<ProjectDB>>

    @Query("SELECT * FROM " +
            "card JOIN project ON card.project_id = project.id " +
            "WHERE is_recommended = TRUE")
    suspend fun getRecommendedProjects(): Map<CardDB, List<ProjectDB>>
}