package com.jvmtechs.repo

import com.jvmtechs.model.*
import com.jvmtechs.utils.Results
import org.hibernate.Session
import org.hibernate.Transaction

class TripRepo : AbstractRepo<Trip>() {

    fun loadAll(): Results {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val tripQry = "SELECT * FROM \"tblTrip\" ORDER BY \"dtmStarted\" LIMIT 200"
            val data =
                session!!.createNativeQuery(tripQry, Trip::class.java).resultList
            Results.Success(code = Results.Success.CODE.LOAD_SUCCESS, data = data)

        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }



    fun queryModel(query: TripQuery): Results {

        var session: Session? = null
        var qryStr = "FROM Trip  WHERE deleted=false"

        if (query.fromDate != null && query.toDate != null)
            qryStr += " AND dtmStarted BETWEEN \'${query.fromDate}\' AND \'${query.toDate}\'"

        qryStr += " ORDER BY dtmStarted DESC"

        return try {
            session = sessionFactory?.openSession()
            var data = session
                ?.createQuery(qryStr, Trip::class.java)
                ?.resultList
                ?.filterNotNull()

            data = if (query.driver != null)
                data?.filter { it.driver?.id == it.driver?.id }
            else data

            data = if (query.truck != null)
                data?.filter { it.truck?.id == it.truck?.id }
            else data

            data = if (!query.containerNo.isNullOrEmpty())
                data?.mapNotNull {
                    val cont = it.containerList.map { it.containerNo?.toLowerCase() }
                    if (query.containerNo?.toLowerCase() in cont)
                        it
                    else null
                }
            else data

            Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)

        } catch (e: Exception) {
            e.printStackTrace()
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    fun updateTripCost(jobLog: Int, newCost: Float): Results {
        var session: Session? = null
        var trans: Transaction? = null
        return try {
            session = sessionFactory!!.openSession()
            trans = session!!.beginTransaction()
            val qryStr =
                "update \"tblTrip\" set \"priceLessTax\"=:tripCost where \"tripLog\" in (select \"triplog\" from \"tblContainers\" where joblog=:jobLog)"
            session.createNativeQuery(qryStr, Trip::class.java)
                .setParameter("tripCost", newCost)
                .setParameter("jobLog", jobLog)
                .executeUpdate()
            trans!!.commit()
            Results.Success(code = Results.Success.CODE.WRITE_SUCCESS, data = null)
        } catch (e: Exception) {
            trans?.rollback()
            Results.Error(e)
        } finally {
            session?.close()
        }
    }
}