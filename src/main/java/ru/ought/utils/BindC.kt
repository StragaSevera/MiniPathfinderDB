package ru.ought.utils

import javafx.beans.binding.Bindings
import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.Parent
import java.util.*
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties

@Suppress("UNCHECKED_CAST")
object BindC {
    enum class Fields {
        PROPERTY, CONTROLLER
    }

    @JvmStatic
    fun getProp(node: Node): String? = node.properties[Fields.PROPERTY] as String?

    @Suppress("unused")
    @JvmStatic
    fun setProp(node: Node, value: String) {
        node.properties[Fields.PROPERTY] = value
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
        getAllChildren(root).filter { node -> getProp(node) != null }
            .forEach { node -> bind(node, getProp(node)!!, controller) }
    }

    private fun getAllChildren(root: Parent): Sequence<Node> = sequence {
        tailrec suspend fun SequenceScope<Node>.getAllChildren1(nodes: MutableList<Node>) {
            if(nodes.isEmpty()) return
            val node = nodes.first()
            nodes.removeAt(0)
            yield(node)
            if (node is Parent) {
                nodes.addAll(node.childrenUnmodifiable)
            }
            getAllChildren1(nodes)
        }
        getAllChildren1(LinkedList<Node>().also { it.add(root) })
    }

    private fun bind(node: Node, propertyName: String, controller: Any) {
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