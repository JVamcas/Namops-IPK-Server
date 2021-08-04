package com.jvmtechs.servlet

import com.google.gson.reflect.TypeToken
import com.jvmtechs.model.Job
import com.jvmtechs.model.JobSeq
import com.jvmtechs.repo.JobRepo
import com.jvmtechs.utils.ParseUtil.Companion.convert
import com.jvmtechs.utils.ParseUtil.Companion.toJson
import com.jvmtechs.utils.Results
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(
    name = "job-servlet",
    value = ["/jobs_fetch_all", "/job_add_update", "/next_job_index", "/job_batch_update", "/jobs_query", "/jobs_fetch_all_pending"]
)
class JobServlet : AbstractServlet() {

    private lateinit var jobRepo: JobRepo

    override fun init() {
        jobRepo = JobRepo()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doGet(req, resp)

        val validated = req.validate(resp)
        if (validated) {
            when (uri) {
                "/jobs_fetch_all" -> {
                    val loadRes = jobRepo.loadAll()
                    if (loadRes is Results.Success<*>) {
                        val data = loadRes.data as ArrayList<Job>
                        out.print("{Status:\"Success\",data: ${data.toJson()}}")
                    } else out.print("{Status: \"Server Error\"}")
                }
                "/next_job_index" -> {
                    val loadRes = jobRepo.nextJobIndex()
                    if (loadRes is Results.Success<*>) {
                        val data = loadRes.data as ArrayList<JobSeq>
                        out.print("{Status:\"Success\",data: ${data.toJson()}}")
                    } else out.print("{Status: \"Server Error\"}")
                }
                "/jobs_fetch_all_pending" -> {
                    val loadRes = jobRepo.loadAllReady()
                    if (loadRes is Results.Success<*>) {
                        val data = loadRes.data as ArrayList<Job>
                        out.print("{Status:\"Success\",data: ${data.toJson()}}")
                    } else out.print("{Status: \"Server Error\"}")
                }
            }
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPost(req, resp)

        try {
            val validated = req.validate(resp)

            if (validated) {
                when (uri) {
                    "/job_add_update" -> {
                        val tripJson = req.getParameter("job")
                        val job = tripJson.convert<Job>()
                        val writeResults = jobRepo.updateModel(model = job)
                        out.print(
                            if (writeResults is Results.Error)
                                "{Status:\"Server Error\"}"
                            else "{Status:\"Success\",data:${job.toJson()}}"
                        )
                    }
                    "/job_batch_update" -> {
                        val jsonJobList = req.getParameter("job")
                        val jobList = jsonJobList.convert<List<Job>>(object : TypeToken<List<Job>>() {}.type)
                        val updateResults = jobRepo.batchUpdate(jobList)
                        out.print(
                            if (updateResults is Results.Error)
                                "{Status:\"Server Error\"}"
                            else "{Status:\"Success\",data:${jobList.toJson()}}"
                        )
                    }
                    "/jobs_query" -> {
                        val jsonQuery = req.getParameter("job")
                        val results = jobRepo.queryModel(jsonQuery.convert())
                        out.print(
                            if (results is Results.Success<*>)
                                "{Status:\"Success\",data:${results.data.toJson()}}"
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