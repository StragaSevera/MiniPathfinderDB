package ru.ought.binding.utils

import javafx.beans.property.Property
import kotlin.reflect.KProperty

class JfxDelegatedProperty<T>(private val jfxProperty: Property<T>) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        return jfxProperty.value
    }
    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        jfxProperty.value = value
    }
}

fun <T> jfx(jfxProperty: Property<T>): JfxDelegatedProperty<T> = JfxDelegatedProperty(jfxProperty)