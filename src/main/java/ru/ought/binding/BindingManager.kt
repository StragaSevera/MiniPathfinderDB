package ru.ought.binding

import javafx.beans.binding.Bindings
import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.Parent
import ru.ought.binding.utils.getChildrenDeep
import kotlin.reflect.KProperty1
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties

@Suppress("UNCHECKED_CAST")
object BindingManager {
    fun performBinding(root: Parent, controller: Any?) {
        requireNotNull(controller)

        val children = root.getChildrenDeep().toList()
        val controllerProperties = getControllerProperties(controller)

        for ((controllerProperty, annotation) in controllerProperties) {
            val controllerFxProperty = controllerProperty.call(controller) as Property<Any>
            val nodeFxProperty = getNodeFxProperty(annotation, controllerProperty.name, children)
            Bindings.bindBidirectional(nodeFxProperty, controllerFxProperty)
        }
    }

    private fun getControllerProperties(controller: Any) =
        controller::class.memberProperties
            .map { property -> property to property.annotations.find { it is DoubleBinding } }
            .filter { (_, annotation) -> annotation != null }
                as List<Pair<KProperty1<out Any, *>, DoubleBinding>>

    private fun getNodeFxProperty(
        annotation: DoubleBinding,
        controllerPropertyName: String,
        children: List<Node>
    ): Property<Any> {
        val (nodePropertyName, nodeId) = DoubleBinding.parse(annotation, controllerPropertyName)
        val node = children.find { it.id == nodeId } ?: error("Cannot find a node with id $nodeId")
        return (node::class.functions
            .find { it.name == nodePropertyName }
            ?.call(node)
            ?: error("Cannot find property $nodePropertyName in node with id $nodeId!")) as Property<Any>
    }
}