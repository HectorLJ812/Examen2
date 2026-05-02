package com.example.combustible

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class PrecioCombustible(val tipo: String, val precio: Double, val fecha: String)

object PrecioManager {
    private const val ARCHIVO = "precios.txt"
    private val fmt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun guardar(context: Context, tipo: String, precio: Double) {
        val linea = "$tipo|$precio|${fmt.format(Date())}\n"
        File(context.filesDir, ARCHIVO).appendText(linea)
    }

    fun cargar(context: Context): List<PrecioCombustible> {
        val f = File(context.filesDir, ARCHIVO)
        if (!f.exists()) return emptyList()
        return f.readLines().filter { it.isNotBlank() }.mapNotNull {
            val p = it.split("|")
            if (p.size == 3) PrecioCombustible(p[0], p[1].toDoubleOrNull() ?: 0.0, p[2]) else null
        }.reversed()
    }

    fun eliminar(context: Context, item: PrecioCombustible) {
        val f = File(context.filesDir, ARCHIVO)
        if (!f.exists()) return
        val nuevas = f.readLines().filter { linea ->
            val p = linea.split("|")
            !(p.size == 3 && p[0] == item.tipo &&
                    p[1] == item.precio.toString() && p[2] == item.fecha)
        }
        f.writeText(nuevas.joinToString("\n") + if (nuevas.isNotEmpty()) "\n" else "")
    }
}