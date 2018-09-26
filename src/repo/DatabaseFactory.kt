package repo

import com.tysheng.xishi.server.data.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.coroutines.experimental.CoroutineContext


object DatabaseFactory {

    fun init() {
        Database.connect(pgs())
        transaction {
            create(UserTable, AlbumTable, ShotTable, AlbumBookmarkTable, ShotBookmarkTable)
        }
    }

    private fun h2(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
//        config.jdbcUrl = "jdbc:h2:mem:test"
        config.jdbcUrl = "jdbc:h2:file:./test"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    private fun pgs(): HikariDataSource {
        val props = Properties()
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
        props.setProperty("dataSource.user", "tysheng")
        props.setProperty("dataSource.password", "sty")
        props.setProperty("dataSource.databaseName", "pgsdb")
//        props.put("dataSource.logWriter", PrintWriter(System.out))

        val config = HikariConfig(props)
        return HikariDataSource(config)
    }

    private val dispatcher: CoroutineContext

    init {
        dispatcher = newFixedThreadPoolContext(5, "database-pool")
    }

    suspend fun <T> dbQuery(
            block: () -> T): T =
            withContext(dispatcher) {
                transaction { block() }
            }

}