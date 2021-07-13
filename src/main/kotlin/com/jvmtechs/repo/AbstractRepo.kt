package com.jvmtechs.repo

import com.jvmtechs.model.IPK
import com.jvmtechs.utils.Results
import com.jvmtechs.utils.SessionManager
import org.hibernate.Session
import org.hibernate.Transaction

abstract class AbstractRepo<T> {

    val sessionFactory by lazy { SessionManager.newInstance }

    open fun updateModel(model: T): Results {
        var session: Session? = null
        return try {
            session = sessionFactory!!
                .openSession()

            val transaction = session!!.beginTransaction()
            session.saveOrUpdate(model)
            transaction.commit()
            Results.Success(data = model, code = Results.Success.CODE.WRITE_SUCCESS)

        } catch (e: Exception) {
            e.printStackTrace()
            session?.transaction?.rollback()
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    fun batchUpdate(transactions: List<IPK>) {
        var trans: Transaction? = null
        var session: Session? = null
        try {
            session = sessionFactory?.openSession()
            trans = session?.beginTransaction()
            transactions.forEach {
                session?.saveOrUpdate(it)
            }
            trans?.commit()
        } catch (e: Exception) {
            e.printStackTrace()
            trans?.rollback()
        } finally {
            session?.close()
        }
    }
}