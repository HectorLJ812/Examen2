package com.example.combustible

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrecioScreen() {
    val context = LocalContext.current

    var nombre       by remember { mutableStateOf("") }
    var codigo       by remember { mutableStateOf("") }
    var categoria    by remember { mutableStateOf("") }
    var precioVenta  by remember { mutableStateOf("") }
    var stockInicial by remember { mutableStateOf("") }
    var stockMinimo  by remember { mutableStateOf("") }
    var mensaje      by remember { mutableStateOf("") }
    var registros    by remember { mutableStateOf(PrecioManager.cargar(context)) }

    val campoColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = TarjetaGris,
        focusedContainerColor   = TarjetaGris,
        unfocusedBorderColor    = Color.Transparent,
        focusedBorderColor      = Morado
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FondoGris),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Nuevo Registro",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1A1A1A)
                    )

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        placeholder = { Text("Nombre del producto *") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = campoColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = codigo,
                        onValueChange = { codigo = it },
                        placeholder = { Text("Código *") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = campoColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = categoria,
                        onValueChange = { categoria = it },
                        placeholder = { Text("Categoría *") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = campoColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = precioVenta,
                        onValueChange = { precioVenta = it },
                        placeholder = { Text("Precio de venta (Q) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = campoColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = stockInicial,
                        onValueChange = { stockInicial = it },
                        placeholder = { Text("Stock inicial *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = campoColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = stockMinimo,
                        onValueChange = { stockMinimo = it },
                        placeholder = { Text("Stock mínimo *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = campoColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (mensaje.isNotEmpty()) {
                        Text(
                            text = mensaje,
                            fontSize = 13.sp,
                            color = if (mensaje.startsWith("✓")) Morado
                            else MaterialTheme.colorScheme.error
                        )
                    }

                    Button(
                        onClick = {
                            val precio   = precioVenta.trim().toDoubleOrNull()
                            val sInicial = stockInicial.trim().toIntOrNull()
                            val sMinimo  = stockMinimo.trim().toIntOrNull()
                            when {
                                nombre.isBlank()                 -> mensaje = "⚠️ Ingresa el nombre del producto"
                                codigo.isBlank()                 -> mensaje = "⚠️ Ingresa el código"
                                categoria.isBlank()              -> mensaje = "⚠️ Ingresa la categoría"
                                precio == null || precio <= 0    -> mensaje = "⚠️ Precio inválido"
                                sInicial == null || sInicial < 0 -> mensaje = "⚠️ Stock inicial inválido"
                                sMinimo == null || sMinimo < 0   -> mensaje = "⚠️ Stock mínimo inválido"
                                else -> {
                                    PrecioManager.guardar(
                                        context,
                                        Producto(
                                            nombre       = nombre.trim(),
                                            codigo       = codigo.trim(),
                                            categoria    = categoria.trim(),
                                            precioVenta  = precio,
                                            stockInicial = sInicial,
                                            stockMinimo  = sMinimo,
                                            fecha        = PrecioManager.fechaActual()
                                        )
                                    )
                                    registros = PrecioManager.cargar(context)
                                    mensaje = "✓ Producto guardado"
                                    nombre = ""; codigo = ""; categoria = ""
                                    precioVenta = ""; stockInicial = ""; stockMinimo = ""
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Morado)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Guardar Registro",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            nombre = ""; codigo = ""; categoria = ""
                            precioVenta = ""; stockInicial = ""; stockMinimo = ""; mensaje = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Morado),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = SolidColor(Morado)
                        )
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Limpiar campos",
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        if (registros.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "REGISTROS GUARDADOS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        items(registros) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.nombre,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "${item.codigo}  •  ${item.categoria}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = item.fecha,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Q %.2f".format(item.precioVenta),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "Stock ini: ${item.stockInicial}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Stock mín: ${item.stockMinimo}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = {
                        PrecioManager.eliminar(context, item)
                        registros = PrecioManager.cargar(context)
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFCC3333)
                    )
                }
            }
            HorizontalDivider(
                color = Color(0xFFDDDDDD),
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}