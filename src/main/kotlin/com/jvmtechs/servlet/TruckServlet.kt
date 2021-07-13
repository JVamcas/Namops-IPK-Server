package com.jvmtechs.servlet

import com.jvmtechs.model.Truck
import com.jvmtechs.repo.TruckRepo
import com.jvmtechs.utils.ParseUtil.Companion.convert
import com.jvmtechs.utils.ParseUtil.Companion.toJson
import com.jvmtechs.utils.Results
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "truck-servlet", value = ["/trucks_fetch_all","/truck_add_update"])
class TruckServlet : AbstractServlet (){

    private lateinit var truckRepo: TruckRepo

    override fun init() {
        truckRepo = TruckRepo()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doGet(req, resp)

        val validated = req.validate(resp)
        if (validated) {
            when (uri) {
                "/trucks_fetch_all" -> {
                    val loadRes = truckRepo.loadAll()
                    if (loadRes is Results.Success<*>) {
                        val data = loadRes.data as ArrayList<Truck>
                        out.print("{Status:\"Success\",data: ${data.toJson()}}")
                    } else out.print("{Status: \"Server Error\"}")
                }
            }
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPost(req, resp)

        try {
            val truckJson = req.getParameter("truck")

            val validated = req.validate(resp)
            val truck = truckJson.convert<Truck>()

            if (validated) {
                when (uri) {
                    "/truck_add_update" -> {
                        val writeResults = truckRepo.updateModel(model = truck)
                        out.print(
                            if (writeResults is Results.Error)
                                "{Status:\"Server Error\"}"
                            else "{Status:\"Success\",data:${truck.toJson()}}"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            out.print("{Status: \"Server Error\"}")
        }
    }
}