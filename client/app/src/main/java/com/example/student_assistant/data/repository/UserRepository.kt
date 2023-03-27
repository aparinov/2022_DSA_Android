package com.example.student_assistant.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.example.student_assistant.data.local.AppDatabase
import com.example.student_assistant.data.local.UserDao
import com.example.student_assistant.data.local.mapper.UserMapper
import com.example.student_assistant.data.network.AuthApi
import com.example.student_assistant.data.network.mapper.AuthMapper
import com.example.student_assistant.domain.entity.LoginInfo
import com.example.student_assistant.domain.entity.RegistrationInfo
import com.example.student_assistant.domain.entity.UserInfo
import com.example.student_assistant.domain.entity.VerificationInfo
import com.example.student_assistant.domain.repository.IUserRepository
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UserDao,
    private val api: AuthApi,
    private val mapper: UserMapper,
    private val apiMapper: AuthMapper,
    ) : IUserRepository {

    override suspend fun register(info: RegistrationInfo): Result<Unit> {
        return try {
            api.register(apiMapper.map(info))
            Result.success(Unit)
        } catch (exc: IllegalStateException) {
            Result.failure(exc)
        } catch (exc: Exception) {
            exc.printStackTrace()
            Result.failure(IllegalStateException("Something went wrong"))
        }
    }

    override suspend fun verify(info: VerificationInfo): Result<Unit> {
        return try {
            api.verifyEmail(apiMapper.map(info))
            Result.success(Unit)
        } catch (exc: IllegalStateException) {
            Result.failure(exc)
        } catch (exc: Exception) {
            exc.printStackTrace()
            Result.failure(IllegalStateException("Something went wrong"))
        }
    }

    override suspend fun login(info: LoginInfo): Result<Unit> {
        return try {
            val cachedUser = dao.getUser()
            if (cachedUser.isNotEmpty()) {
                return Result.success(Unit)
            }
            api.login(apiMapper.map(info))
            dao.setUser(mapper.mapFromLoginInfo(info))
            Result.success(Unit)
        } catch (exc: IllegalStateException) {
            Result.failure(exc)
        } catch (exc: Exception) {
            exc.printStackTrace()
            Result.failure(IllegalStateException("Something went wrong"))
        }
    }

    override suspend fun getCachedUser(): Result<LoginInfo> {
        return try {
            val cachedUser = dao.getUser()
            if (cachedUser.isEmpty()) {
                Result.failure(IllegalStateException())
            } else {
                val cachedLoginInfo = mapper.mapToLoginInfo(cachedUser[0])
                Result.success(cachedLoginInfo)
            }
        } catch (exc: Exception) {
            exc.printStackTrace()
            Result.failure(exc)
        }
    }

    override suspend fun getUser(): Result<UserInfo> {
        return try {
            val cachedUser = dao.getUser()[0]
            val remoteUser = apiMapper.map(api.getUser(mapper.mapToLoginInfo(cachedUser).email))
            val localUser = mapper.mapToUserInfo(cachedUser)
            if (localUser != null) {
                Result.success(UserInfo(localUser.isStudent, localUser.name, localUser.bio, localUser.contacts, remoteUser.tags))
            } else {
                Result.failure(IllegalStateException())
            }
//            dao.setUser(mapper.mapFromUserInfo(user, cachedUser.email))
        } catch (exc: IllegalStateException) {
            Result.failure(exc)
        } catch (exc: Exception) {
            exc.printStackTrace()
            Result.failure(IllegalStateException("Something went wrong"))
        }
    }

    override suspend fun updateUser(name: String, surname: String, bio: String, tags: List<String>): Result<Unit> {
        return try {
            val cachedUser = dao.getUser()[0]
            val cachedUserInfo = mapper.mapToUserInfo(cachedUser)
            if (cachedUserInfo != null) {
                val info = UserInfo(cachedUserInfo.isStudent, "$name $surname", bio, cachedUserInfo.contacts, tags)
                val loginInfo = mapper.mapToLoginInfo(cachedUser)
                api.updateUser(loginInfo.email, apiMapper.map(info))
                dao.setUser(mapper.mapFromUserInfo(info, loginInfo.email))
            }
            Result.success(Unit)
        } catch (exc: IllegalStateException) {
            Result.failure(exc)
        } catch (exc: Exception) {
            exc.printStackTrace()
            Result.failure(IllegalStateException("Something went wrong"))
        }
    }

    override suspend fun logout() {
        dao.deleteUser()
    }
}