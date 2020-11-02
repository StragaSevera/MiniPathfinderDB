package ru.ought.utils

import javafx.beans.binding.Bindings
import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.Parent
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties

@Suppress("UNCHECKED_CAST")
object BindC {
    enum class Fields {
        PROPERTY_NAMES, CONTROLLER
    }

    @JvmStatic
    fun getPropertyNames(node: Node): String? = node.properties[Fields.PROPERTY_NAMES] as String?

    @Suppress("unused")
    @JvmStatic
    fun setPropertyNames(node: Node, value: String) {
        node.properties[Fields.PROPERTY_NAMES] = value
    }

    @JvmStatic
    fun getController(node: Node): Any? = node.properties[Fields.CONTROLLER]

    @Suppress("unused")
    @JvmStatic
    fun setController(node: Node, value: Any) {
        node.properties[Fields.CONTROLLER] = value
    }

    fun performBinding(root: Parent) {
        val controller = getController(root)
        requireNotNull(controller)
        root.getChildrenDeep().filter { node -> getPropertyNames(node) != null }
            .forEach { node -> bind(node, getPropertyNames(node)!!, controller) }
    }

    private fun bind(node: Node, propertyNames: String, controller: Any) {
        propertyNames.split(",").forEach { propertyName ->
            val controllerPropertyName = node.id + propertyName.capitalize()

            val propertyUncasted =
                node::class.functions
                    .find { it.name == propertyName + "Property" }
                    ?.call(node)
                    ?: error("Cannot find property $propertyName in control!")
            val property = propertyUncasted as Property<Any>

            val controllerPropertyUncasted =
                controller::class.memberProperties
                    .find { it.name == controllerPropertyName + "Property" }
                    ?.call(controller)
                    ?: error("Cannot find property $controllerPropertyName in controller!")
            val controllerProperty = controllerPropertyUncasted as Property<Any>

            Bindings.bindBidirectional(property, controllerProperty)
        }
    }
}