package com.jvmtechs.servlet

import com.jvmtechs.model.User
import com.jvmtechs.repo.UserRepo
import com.jvmtechs.utils.ParseUtil.Companion.convert
import com.jvmtechs.utils.ParseUtil.Companion.toJson
import com.jvmtechs.utils.Results
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet(name = "user-servlet", value = ["/users_fetch_all", "/user_add_update", "/authenticate"])
class UserServlet : AbstractServlet() {

    private lateinit var userRepo: UserRepo

    override fun init() {
        userRepo = UserRepo()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doGet(req, resp)

        val validated = req.validate(resp)
        if (validated) {
            when (uri) {
                "/users_fetch_all" -> {
                    val loadRes = userRepo.loadAll()
                    if (loadRes is Results.Success<*>) {
                        val data = loadRes.data as ArrayList<User>
                        out.print("{Status:\"Success\",data: ${data.toJson()}}")
                    } else out.print("{Status: \"Server Error\"}")
                }
                "/authenticate" -> {
                    val username = req.getParameter("username")
                    val password = req.getParameter("password")
                    val loadRes = userRepo.authenticate(userName = username, password = password)
                    if (loadRes is Results.Success<*>) {
                        val data = loadRes.data as? User
                        out.print("{Status:\"Success\",data: ${data.toJson()}}")
                    } else out.print("{Status: \"Server Error\"}")
                }
            }
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPost(req, resp)
        try {
            val jsonUser = req.getParameter("user")

            val validated = req.validate(resp)
            val user = jsonUser.convert<User>()

            if (validated) {
                when (uri) {
                    "/user_add_update" -> {
                        val writeResults = userRepo.updateModel(model = user)
                        out.print(
                            if (writeResults is Results.Error)
                                "{Status:\"Server Error\"}"
                            else "{Status:\"Success\",data:${user.toJson()}}"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            out.print("{Status: \"Server Error\"}")
        }
    }
}