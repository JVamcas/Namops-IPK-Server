package com.jvmtechs.repo

import com.jvmtechs.model.Job
import com.jvmtechs.utils.Results
import org.hibernate.Session

class JobRepo: AbstractRepo<Job>() {

    fun loadAll(): Results {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val data = session!!.createQuery("FROM Job WHERE deleted=FALSE", Job::class.java).resultList
            Results.Success(code = Results.Success.CODE.LOAD_SUCCESS, data = data)

        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }
}