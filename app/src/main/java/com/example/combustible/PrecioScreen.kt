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
    val tipos = listOf("Gasolina Regular", "Gasolina Super", "Diesel")

    var tipoSeleccionado by remember { mutableStateOf(tipos[0]) }
    var expandido        by remember { mutableStateOf(false) }
    var precio           by remember { mutableStateOf("") }
    var mensaje          by remember { mutableStateOf("") }
    var registros        by remember { mutableStateOf(PrecioManager.cargar(context)) }

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

                    ExposedDropdownMenuBox(
                        expanded = expandido,
                        onExpandedChange = { expandido = !expandido }
                    ) {
                        OutlinedTextField(
                            value = tipoSeleccionado,
                            onValueChange = {},
                            readOnly = true,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Home,
                                    contentDescription = null,
                                    tint = Color.DarkGray
                                )
                            },
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = TarjetaGris,
                                focusedContainerColor   = TarjetaGris,
                                unfocusedBorderColor    = Color.Transparent,
                                focusedBorderColor      = Morado
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandido,
                            onDismissRequest = { expandido = false }
                        ) {
                            tipos.forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo) },
                                    onClick = {
                                        tipoSeleccionado = tipo
                                        expandido = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = precio,
                        onValueChange = { precio = it },
                        placeholder = { Text("Precio en Quetzales *") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Done,
                                contentDescription = null,
                                tint = Color.DarkGray
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = TarjetaGris,
                            focusedContainerColor   = TarjetaGris,
                            unfocusedBorderColor    = Color.Transparent,
                            focusedBorderColor      = Morado
                        ),
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
                            val p = precio.trim().toDoubleOrNull()
                            when {
                                precio.isBlank()    -> mensaje = "⚠️ Ingresa un precio"
                                p == null || p <= 0 -> mensaje = "⚠️ Precio inválido"
                                else -> {
                                    PrecioManager.guardar(context, tipoSeleccionado, p)
                                    registros = PrecioManager.cargar(context)
                                    mensaje = "✓ Precio guardado"
                                    precio = ""
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
                        onClick = { precio = ""; mensaje = "" },
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
                        text = item.tipo,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = item.fecha,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = "Q %.2f".format(item.precio),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A1A1A)
                )

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