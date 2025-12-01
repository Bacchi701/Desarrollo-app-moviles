package com.example.confeccionesbrenda.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.confeccionesbrenda.data.models.CartItem
import com.example.confeccionesbrenda.data.models.Product
import com.example.confeccionesbrenda.data.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(entities = [Product::class, CartItem::class, User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "confecciones_brenda_db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(context)) // Usamos el nuevo Callback
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Callback más robusto que puebla la base de datos SÓLO si está vacía.
     * Se ejecuta cada vez que se abre la BD, no solo cuando se crea.
     */
    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                // Usamos un CoroutineScope dedicado para esta operación de fondo.
                CoroutineScope(SupervisorJob()).launch {
                    populateDatabase(database.productDao())
                }
            }
        }

        suspend fun populateDatabase(productDao: ProductDao) {
            // Si ya hay productos en la BD, no hacemos nada.
            if (productDao.getProductCount() > 0) {
                return
            }
            
            // Si la BD está vacía, insertamos los productos iniciales.
            val initialProducts = listOf(
                Product(name = "Polera Piqué Blanca", description = "Polera de piqué para uniforme escolar, color blanco, manga corta.", price = 12990.0, imageUrl = "polera_blanca", stock = 50),
                Product(name = "Pantalón Gris", description = "Pantalón de tela escolar, color gris, corte recto.", price = 15990.0, imageUrl = "pantalon_gris", stock = 40),
                Product(name = "Falda Escocesa", description = "Falda escocesa tableada, tradicional de uniforme.", price = 14990.0, imageUrl = "falda_escocesa", stock = 30),
                Product(name = "Chaqueta Polar Azul", description = "Chaqueta de polar con cierre, color azul marino, ideal para el frío.", price = 19990.0, imageUrl = "chaqueta_polar", stock = 25),
                Product(name = "Calcetines Azules (Pack 3)", description = "Pack de 3 pares de calcetines de algodón color azul.", price = 4990.0, imageUrl = "calcetines_azules", stock = 100)
            )
            productDao.insertProducts(initialProducts)
        }
    }
}
