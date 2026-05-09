package com.example.combustible

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class Producto(
    val nombre: String,
    val codigo: String,
    val categoria: String,
    val precioVenta: Double,
    val stockInicial: Int,
    val stockMinimo: Int,
    val fecha: String
)

object PrecioManager {
    private const val ARCHIVO = "precios.txt"
    private val fmt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private const val SEP = "|"

    fun guardar(context: Context, producto: Producto) {
        val linea = "${producto.nombre}$SEP${producto.codigo}$SEP${producto.categoria}$SEP${producto.precioVenta}$SEP${producto.stockInicial}$SEP${producto.stockMinimo}$SEP${producto.fecha}\n"
        File(context.filesDir, ARCHIVO).appendText(linea)
    }

    fun cargar(context: Context): List<Producto> {
        val f = File(context.filesDir, ARCHIVO)
        if (!f.exists()) return emptyList()
        return f.readLines().filter { it.isNotBlank() }.mapNotNull {
            val p = it.split(SEP)
            if (p.size == 7) Producto(
                nombre       = p[0],
                codigo       = p[1],
                categoria    = p[2],
                precioVenta  = p[3].toDoubleOrNull() ?: 0.0,
                stockInicial = p[4].toIntOrNull() ?: 0,
                stockMinimo  = p[5].toIntOrNull() ?: 0,
                fecha        = p[6]
            ) else null
        }.reversed()
    }

    fun eliminar(context: Context, item: Producto) {
        val f = File(context.filesDir, ARCHIVO)
        if (!f.exists()) return
        val nuevas = f.readLines().filter { linea ->
            val p = linea.split(SEP)
            !(p.size == 7 && p[0] == item.nombre && p[1] == item.codigo && p[6] == item.fecha)
        }
        f.writeText(nuevas.joinToString("\n") + if (nuevas.isNotEmpty()) "\n" else "")
    }

    fun fechaActual(): String = fmt.format(Date())
}