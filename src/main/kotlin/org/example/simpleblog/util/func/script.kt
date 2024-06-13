package org.example.simpleblog.util.func

import jakarta.servlet.http.HttpServletResponse
import java.io.PrintWriter

fun responseData(resp: HttpServletResponse, jsonData: String?) {
    val out: PrintWriter
    resp.setHeader("Content-Type", "application/json; charset=utf-8")

    try {
        out = resp.writer
        out.println(jsonData)
        out.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
