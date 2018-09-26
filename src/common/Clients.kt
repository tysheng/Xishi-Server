package com.tysheng.xishi.server.common

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import kotlinx.coroutines.experimental.io.jvm.javaio.toInputStream
import java.io.File
import java.net.URL
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object Clients {
    private val client by lazy {
        HttpClient(Apache) {
            engine {
                customizeClient {
                    setSSLContext(unCheckSsl())
//                    setSSLHostnameVerifier(unCheckVerifier())
                }
            }
        }
    }

    private val COLOR_LIST = arrayListOf("2080ff", "5cc414", "f99e22", "ff5c44", "bb66f6")

    suspend fun generateAvatar(name: String): String {
        val background = COLOR_LIST.random()

        val url ="https://ui-avatars.com/api/?" +
                "name=$name&background=$background&color=fff&font-size=0.33&rounded=true"
        val response = client.get<HttpResponse> {
            url(URL(url))
        }
        // save image
        val parent = File("./avatar/")
        if (!parent.exists()){
            parent.mkdirs()
        }
        val file = File(parent,"avatar-${url.md5()}.png")
        response.content.toInputStream().use { ins ->
            file.outputStream().buffered().use { os ->
                ins.copyToSuspend(os)
            }
        }
        return file.name
    }

    private fun unCheckSsl(): SSLContext {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }

            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }
        })

        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, java.security.SecureRandom())

        return sc
    }

    private fun unCheckVerifier() = HostnameVerifier { _, _ -> true }
}