
//import io.ktor.serialization.jackson.jackson
import android.content.Context
import com.example.kormopack.data.specsdatabase.SpecificationFeedEntity
import com.example.kormopack.data.specsdatabase.SpecsDB
import com.example.kormopack.data.specsdatabase.mapper.SpecificationBrandMapper.toEntity
import com.example.kormopack.data.specsdatabase.mapper.SpecificationFeedMapper.toEntity
import com.example.kormopack.domain.specsfrag.model.SpecificationBrandModel
import com.example.kormopack.domain.specsfrag.model.SpecificationFeedModel
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Application.module(context: Context) {
    install(ContentNegotiation) {
        gson()
    }

    routing {
        get("/feedbrands") {
            try {
                val specs = SpecsDB.getDb(context).getDao().getFeedBrands()

                if (specs.isNotEmpty()) {
                    call.respond(specs)
                } else {
                    call.respond(emptyList<SpecificationFeedEntity>())
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Помилка пошуку брендів: $e")
            }
        }
        get("/feeds") {
            try {
                val brand = call.request.queryParameters["brand"]
                val keyWord = call.request.queryParameters["keyWord"]
                if (brand != null) {
                    if (keyWord != null) {
                        val specs = SpecsDB.getDb(context).getDao().getFeedWhichContains(brand, keyWord)
                        if (specs.isNotEmpty()) {
                            call.respond(specs)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Не знайдено специфікацій для бренду - $brand")
                        }
                    }
                    val specs = SpecsDB.getDb(context).getDao().getFeedByBrand(brand)

                    if (specs.isNotEmpty()) {
                        call.respond(specs)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Не знайдено специфікацій для бренду - $brand")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Пропущено параметр 'brand'")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Помилка пошуку специфікацій: $e")
            }
        }
        post("/addBrand") {
            val brandName = call.request.queryParameters["brandName"]

            if (brandName.isNullOrEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "Немає 'brandName'")
                return@post
            }

            try {
                val dao = SpecsDB.getDb(context).getDao()
                val entity = SpecificationBrandModel(name = brandName, brandLogo = 6, cardBackColor = 5, textColor = 2).toEntity()

                dao.insertBrand(entity)

                call.respond(HttpStatusCode.OK, "Бренд створено")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Помилка створення бренду: ${e.message}")
            }
        }
        post("/addFeed") {
            val feedString =  call.request.queryParameters["spec"]
            val tempList = feedString?.split("/")

            try {
                if (tempList != null) {
                    val feed = SpecificationFeedModel(tempList[0].toInt(), tempList[1], tempList[2].toInt(), tempList[3],
                        tempList[4], tempList[5].toInt(), tempList[6].toInt(), tempList[7].toInt(), tempList[8].toInt(),
                        tempList[9].toInt(), tempList[10], tempList[11])

                    val dao = SpecsDB.getDb(context).getDao()

                    val en = feed.toEntity()
                    dao.insertFeed(en)
                }

                call.respond(HttpStatusCode.OK, "Специфікацію створено")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Помилка створення специфікації: ${e.message}")
            }
        }
    }
}

object KtorServer {

    var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null
        private set

    fun startServer(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            if (server == null) {
                server = embeddedServer(
                    Netty,
                    host = "localhost",
                    port = 8081,
                    module = { module(context) }
                ).start(wait = false)
            }
        }
    }

    fun stopServer() {
        server?.stop(1000, 2000)
        server = null
    }
}
