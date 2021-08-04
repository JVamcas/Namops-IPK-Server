package com.jvmtechs.repo

import com.jvmtechs.model.IPK
import com.jvmtechs.model.IPKQuery
import com.jvmtechs.model.Trip
import com.jvmtechs.model.TripQuery
import com.jvmtechs.utils.Results
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.csv.CSVFormat
import org.hibernate.Session
import java.io.StringReader
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class IPKRepo : AbstractRepo<IPK>() {

    fun loadAll(): Results {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val strQry = "SELECT * FROM \"tblIncomePerKilometre\"  ORDER  BY dtmCreated DESC LIMIT 100"
            val data = session!!.createNativeQuery(strQry, IPK::class.java).resultList
            Results.Success(code = Results.Success.CODE.LOAD_SUCCESS, data = data)

        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    /***
     * Find the odometer reading for this vehicle from webfleet
     * @return [List<Pair<String,Double>]] unit no vs odometer
     */

    fun loadVehiclesOdometer(): List<Pair<String, Double>> {
        val url = "https://csv.telematics.tomtom.com/extern?" +
                "account=namops&username=Rauna&password=3Mili2,87&" +
                "apikey=0e7ddb3b-65b0-4991-82a9-1c5c6f312317&lang=en&action=showObjectReportExtern"
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            val results = client.newCall(request).execute()//wait for the results from webfleet
            val data = results.body?.string()
            val csvParser = CSVFormat.newFormat(';').withQuote('"').withFirstRecordAsHeader().parse(StringReader(data))
            csvParser.records.map {
                it[0] to it[30].toDouble() / 10
            }

        } catch (e: java.lang.Exception) {
            listOf()
        }
    }

    /***
     * Find all the IPK completed today up to before now, normally now=23:30
     */
    fun loadPrevDayIPKs(): List<IPK>? {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val dayEnd = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 30)) //end of yesterday
            val dayStart = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 1)) //start of yesterday
            val strQry = "SELECT * FROM income_per_kilometre i WHERE i.date between \'${dayStart}\' AND \'${dayEnd}\'"
            val data = session!!.createNativeQuery(strQry, IPK::class.java).resultList
            data as List<IPK>

        } catch (e: Exception) {
            null
        } finally {
            session?.close()
        }
    }

    fun loadTodayIPK(): List<IPK>? {
        var session: Session? = null
        return try {
            session = sessionFactory!!.openSession()
            val dayEnd = LocalDateTime.now()
            val dayStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 1))
            val strQry = "SELECT * FROM income_per_kilometre i WHERE i.date between \'${dayStart}\' AND \'${dayEnd}\'"
            val data = session!!.createNativeQuery(strQry, IPK::class.java).resultList
            data as List<IPK>

        } catch (e: Exception) {
            null
        } finally {
            session?.close()
        }
    }

    fun queryModel(query: IPKQuery): Results {

        var session: Session? = null
        var qryStr = "FROM IPK  WHERE deleted=false"

        if (query.fromDate != null && query.toDate != null)
            qryStr += " AND dtmCreated BETWEEN \'${query.fromDate}\' AND \'${query.toDate}\'"

        qryStr += " ORDER BY dtmCreated DESC"

        return try {
            session = sessionFactory?.openSession()
            var data = session
                ?.createQuery(qryStr, IPK::class.java)
                ?.resultList
                ?.filterNotNull()
            data = if (query.truck != null)
                data?.filter { it.truck?.id == it.truck?.id }
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