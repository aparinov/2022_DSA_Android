package com.example.student_assistant.data.local.mapper

import com.example.student_assistant.data.local.entity.UserDB
import com.example.student_assistant.domain.entity.LoginInfo
import com.example.student_assistant.domain.entity.UserInfo
import javax.inject.Inject

class UserMapper @Inject constructor() {
    fun mapToUserInfo(dto: UserDB): UserInfo? {
        if (dto.isStudent == null || dto.bio == null || dto.contacts == null || dto.name == null) {
            return null
        }
        return UserInfo(dto.isStudent, dto.name, dto.bio, dto.contacts, listOf())
    }
    fun mapToLoginInfo(dto: UserDB): LoginInfo {
        return LoginInfo(dto.email, "")
    }

    fun mapFromLoginInfo(info: LoginInfo): UserDB {
        return UserDB(
            info.email,
            null,
            null,
            null,
            null,
        )
    }

    fun mapFromUserInfo(info: UserInfo, email: String): UserDB {
        return UserDB(
            email,
            info.isStudent,
            info.name,
            info.bio,
            info.contacts,
        )
    }
}