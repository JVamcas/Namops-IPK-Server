package com.jvmtechs.servlet

import com.jvmtechs.model.User
import com.jvmtechs.repo.UserRepo
import com.jvmtechs.utils.Results
import java.io.PrintWriter
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

abstract class AbstractServlet: HttpServlet() {
    lateinit var uri: String
    lateinit var out: PrintWriter


    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        uri = req.requestURI.substring(req.contextPath.length)
        out = resp.writer
        resp.contentType = "application/json"

    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        uri = req.requestURI.substring(req.contextPath.length)
        out = resp.writer
        resp.contentType = "application/json"

    }

    fun HttpServletRequest.validate(resp: HttpServletResponse): Boolean {
        val passcode = this.getParameter("password") ?: ""
        val surname = this.getParameter("username") ?: ""
        val out = resp.writer

        return try {
            when (val result = UserRepo().authenticate(userName = surname, password = passcode)) {
                is Results.Success<*> -> {
                    val validated = !(result.data as? ArrayList<User>).isNullOrEmpty()
                    if (!validated)
                        out.print("{Status: \"Invalid Auth\"}")
                    validated
                }
                else -> {
                    out.print("{Status: \"Server Error\"}")
                    false
                }
            }
        } catch (e: Exception) {
            out.print("{Status: \"Server Error\"}")
            false
        }
    }
}