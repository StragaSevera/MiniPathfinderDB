package ru.ought.utils

import javafx.beans.binding.Bindings
import javafx.beans.property.Property
import javafx.scene.control.Control
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties

@Suppress("UNCHECKED_CAST")
class BindC {
    // Backing fields
    var c: Any? = null
        set(value) {
            field = value
            bind()
        }
    var p: String? = null
        set(value) {
            field = value
            bind()
        }
    var co: Any? = null
        set(value) {
            field = value
            bind()
        }

    private fun bind() {
        if (c == null || p == null || co == null) return
        bind(c as Control, p!!, co!!)
    }

    private fun bind(control: Control, propertyName: String, controller: Any) {
        val controllerPropertyName = control.id + propertyName.capitalize()

        val propertyUncasted =
            control::class.functions
                .find { it.name == propertyName + "Property" }
                ?.call(control)
                ?: error("Cannot find property $propertyName in control!")
        val property = propertyUncasted as Property<Any>
        println(property.name)

        val controllerPropertyUncasted =
            controller::class.memberProperties
                .find { it.name == controllerPropertyName + "Property" }
                ?.call(controller)
                ?: error("Cannot find property $propertyName in controller!")
        val controllerProperty = controllerPropertyUncasted as Property<Any>
        println(controllerProperty.name)
        Bindings.bindBidirectional(property, controllerProperty);
    }

}