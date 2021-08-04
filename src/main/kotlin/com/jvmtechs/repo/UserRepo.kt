package com.jvmtechs.repo

import com.jvmtechs.model.User
import com.jvmtechs.utils.Results
import org.hibernate.Session

class UserRepo : AbstractRepo<User>() {

    fun authenticate(userName: String, password: String): Results {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val data = session!!.createQuery(
                "FROM User where LOWER(username)=:username AND password=:password AND deleted=FALSE", User::class.java)
                .setParameter("username", userName)
                .setParameter("password", password)
                .resultList.filterNotNull()
            val user = data.firstOrNull()
                ?.apply {
                    this.username = null
                    this.password = null
                }
            Results.Success(code = Results.Success.CODE.LOAD_SUCCESS, data = user)
        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    fun loadAll(): Results {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val data = session!!.createQuery("FROM User WHERE deleted=FALSE ORDER BY firstName", User::class.java).resultList
            data?.forEach {
                it.password = null
                it.username = null
            }
            Results.Success(code = Results.Success.CODE.LOAD_SUCCESS, data = data)

        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }
}
