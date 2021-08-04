package com.jvmtechs.repo

import com.jvmtechs.model.Job
import com.jvmtechs.model.JobQuery
import com.jvmtechs.model.JobSeq
import com.jvmtechs.model.Trip
import com.jvmtechs.utils.Results
import org.hibernate.Session
import org.hibernate.Transaction

class JobRepo : AbstractRepo<Job>() {

    fun loadAll(): Results {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val data = session!!.createQuery(
                "FROM Job WHERE deleted=FALSE ORDER BY dtmDue asc",
                Job::class.java
            ).resultList
            Results.Success(code = Results.Success.CODE.LOAD_SUCCESS, data = data)

        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    fun loadAllReady(): Results {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val data =
                session!!.createQuery("From Job where lower(status)='ready'", Job::class.java).resultList
            Results.Success(code = Results.Success.CODE.LOAD_SUCCESS, data = data)

        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    fun nextJobIndex(): Results {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val data = session!!.createQuery("FROM JobSeq", JobSeq::class.java).resultList
            Results.Success(code = Results.Success.CODE.LOAD_SUCCESS, data = data)

        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    override fun batchUpdate(transactions: List<Job>): Results {
        var trans: Transaction? = null
        var session: Session? = null
        return try {
            session = sessionFactory?.openSession()
            trans = session?.beginTransaction()
            transactions.forEach {
                session?.saveOrUpdate(it)
                it.jobNo = String.format("JC%04d", it.id) + it.entryNo
                session?.update(it)
            }
            trans?.commit()
            Results.Success(data = transactions, code = Results.Success.CODE.UPDATE_SUCCESS)

        } catch (e: Exception) {
            trans?.rollback()
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    fun queryModel(query: JobQuery): Results {
        var session: Session? = null
        var qryStr = "FROM Job  WHERE deleted=false AND completed=false"

        if (!query.jobNo.isNullOrEmpty())
            qryStr += " AND jobNo=\'${query.jobNo}\'"

        if (!query.qbNumber.isNullOrEmpty())
            qryStr += " AND qBCode=\'${query.qbNumber}\'"

        if (query.fromDate != null && query.toDate != null)
            qryStr += " AND dtmPosted BETWEEN \'${query.fromDate}\' AND \'${query.toDate}\'"

        qryStr += " ORDER BY dtmPosted DESC"

        return try {
            session = sessionFactory?.openSession()
            var data = session
                ?.createQuery(qryStr, Job::class.java)
                ?.resultList
                ?.filterNotNull()

            data = if (query.allocatedDriver != null)
                data?.filter { query.allocatedDriver in it.driversList }
            else data

            data = if (query.container != null)
                data?.filter { query.container in it.containerList }
            else data
            Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)

        } catch (e: Exception) {
            e.printStackTrace()
            Results.Error(e)
        } finally {
            session?.close()
        }
    }
}