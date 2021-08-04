package com.jvmtechs.servlet

import com.jvmtechs.model.Trip
import com.jvmtechs.model.Truck
import com.jvmtechs.repo.TripRepo
import com.jvmtechs.repo.TruckRepo
import com.jvmtechs.utils.ParseUtil.Companion.convert
import com.jvmtechs.utils.ParseUtil.Companion.toJson
import com.jvmtechs.utils.Results
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "trip-servlet", value = ["/trip_fetch_all", "/trip_add_update", "/trip_query", "/update_trip_cost"])
class TripServlet : AbstractServlet() {

    private lateinit var tripRepo: TripRepo

    override fun init() {
        tripRepo = TripRepo()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doGet(req, resp)

        val validated = req.validate(resp)
        if (validated) {
            when (uri) {
                "/trip_fetch_all" -> {
                    val loadRes = tripRepo.loadAll()
                    if (loadRes is Results.Success<*>) {
                        val data = loadRes.data as ArrayList<Trip>
                        out.print("{Status:\"Success\",data: ${data.toJson()}}")
                    } else out.print("{Status: \"Server Error\"}")
                }
            }
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPost(req, resp)

        try {
            val tripJson = req.getParameter("trip")

            val validated = req.validate(resp)
            val trip = tripJson.convert<Trip>()

            if (validated) {
                when (uri) {
                    "/trip_add_update" -> {
                        val writeResults = tripRepo.updateModel(model = trip)
                        out.print(
                            if (writeResults is Results.Error)
                                "{Status:\"Server Error\"}"
                            else "{Status:\"Success\",data:${trip.toJson()}}"
                        )
                    }
                    "/trip_query" -> {
                        val tripJsonQuery = req.getParameter("trip")
                        val results = tripRepo.queryModel(query = tripJsonQuery.convert())
                        out.print(
                            if (results is Results.Success<*>)
                                "{Status:\"Success\",data:${results.data.toJson()}}"
                            else "{Status:\"Server Error\"}"
                        )
                    }
                    "/update_trip_cost" -> {
                        val jobLog = req.getParameter("jobLog")?.toIntOrNull() ?: -1
                        val newCost = req.getParameter("newCost")?.toFloatOrNull() ?: 0.0f
                        val results = tripRepo.updateTripCost(jobLog, newCost)
                        out.print(
                            if (results is Results.Success<*>)
                                "{Status:\"Success\",data:\"\"}"
                            else "{Status:\"Server Error\"}"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            out.print("{Status: \"Server Error\"}")
        }
    }
}