package com.jvmtechs.servlet

import com.jvmtechs.model.IPK
import com.jvmtechs.repo.IPKRepo
import com.jvmtechs.utils.ParseUtil.Companion.toJson
import com.jvmtechs.utils.Results
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(
    name = "IPK Servlet",
    value = ["/ipk_scheduler_start", "/ipk_scheduler_stop", "/ipk_fetch_all", "/ipk_query"]
)
class IPKServlet : AbstractServlet() {

    var scheduler: ScheduledExecutorService? = null
    private lateinit var ipkRepo: IPKRepo

    override fun init() {
        ipkRepo = IPKRepo()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doGet(req, resp)

        val validated = req.validate(resp)
        if (validated) {
            try {
                when (uri) {
                    "/ipk_fetch_all" -> {
//                        updateIPK() //first update ipk before loading it
                        if (scheduler == null)
                            startIPKScheduler()

                        val loadRes = ipkRepo.loadAll()
                        if (loadRes is Results.Success<*>) {
                            val data = loadRes.data as ArrayList<IPK>
                            out.print("{Status:\"Success\",data: ${data.toJson()}}")
                        } else out.print("{Status: \"Server Error\"}")
                    }
                    "/ipk_scheduler_start" -> {
                        if (scheduler?.isTerminated == true || scheduler?.isShutdown == true) {
                            startIPKScheduler()
                            out.print("{Status: \"Success\", data: \"IPK Scheduler started.\"}")
                        } else out.print("{Status: \"Success\", data: \"IPK Scheduler is already running\"}")
                    }

                    "/ipk_scheduler_stop" -> {
                        if (scheduler?.isTerminated == true || scheduler?.isShutdown == true)
                            out.print("{Status: \"Success\", data: \"IPK Scheduler is not running.\"}")
                        else {
                            scheduler?.awaitTermination(20, TimeUnit.SECONDS)
                            out.print("{Status: \"Success\", data: \"IPK Scheduler stopped.\"}")
                        }
                    }
                }
            } catch (e: Exception) {
                out.print("{Status: \"Server Error\", data: \"${e.message}.\"}")
            }
        }
    }


    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPost(req, resp)

        val validated = req.validate(resp)
        try {
            if (validated) {
                when (uri) {

                    "/ipk_query" -> {

                    }
                }
            }
        } catch (e: Exception) {
            out.print("{Status: \"Server Error.\"}")
        }
    }

    private fun startIPKScheduler() {
        val now = ZonedDateTime.now(ZoneId.of("Africa/Windhoek"))
        var nextRun = now.withHour(8).withMinute(43).withSecond(50)
        if (now > nextRun) nextRun = nextRun.plusDays(1)

        val duration: Duration = Duration.between(now, nextRun)
        val initialDelay: Long = duration.seconds

        scheduler = Executors.newScheduledThreadPool(1)
        scheduler?.scheduleAtFixedRate(
            {
                try {
//                    updateIPK()
                } catch (e: Exception) {
                    nextRun = nextRun.plusMinutes(10)
                }
            },
            initialDelay,
            TimeUnit.DAYS.toSeconds(1),
            TimeUnit.SECONDS
        )
    }

}