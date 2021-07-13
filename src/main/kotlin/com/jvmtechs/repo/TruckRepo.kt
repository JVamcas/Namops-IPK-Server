package com.jvmtechs.repo

import com.jvmtechs.model.Truck
import com.jvmtechs.utils.Results
import org.hibernate.Session

class TruckRepo : AbstractRepo<Truck>() {


    fun loadAll(): Results {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val data = session!!.createQuery("FROM Truck WHERE deleted=FALSE", Truck::class.java).resultList
            Results.Success(code = Results.Success.CODE.LOAD_SUCCESS, data = data)

        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }
}