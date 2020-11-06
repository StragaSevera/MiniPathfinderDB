package ru.ought.binding

import javafx.beans.binding.Bindings
import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.Parent
import ru.ought.binding.annotations.AfterBinding
import ru.ought.binding.annotations.DoubleBinding
import ru.ought.binding.annotations.DoubleBindingDirection
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
            when (annotation.direction) {
                DoubleBindingDirection.ViewToController -> Bindings.bindBidirectional(
                    nodeFxProperty,
                    controllerFxProperty
                )
                DoubleBindingDirection.ControllerToView -> Bindings.bindBidirectional(
                    controllerFxProperty,
                    nodeFxProperty
                )
            }
        }
    }

    fun callAfterBinding(controller: Any?) {
        requireNotNull(controller)

        val afterBindingMethods =
            controller::class.functions.filter { method -> method.annotations.any { it is AfterBinding } }
        check(afterBindingMethods.size <= 1) { "There can be only one AfterBinding method" }

        afterBindingMethods.firstOrNull()?.call(controller)
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
        var node: Node? = null
        var nodePropertyName: String? = null
        var nodeId: String? = null

        val bindingVariants = DoubleBinding.parse(annotation, controllerPropertyName)
        for (binding in bindingVariants) {
            val currentNode = children.find { it.id == binding.id }
            if (currentNode != null) {
                node = currentNode
                nodePropertyName = binding.propertyName
                nodeId = binding.id
            }
        }

        requireNotNull(node) { "Cannot find a node that matches $controllerPropertyName" }

        return (node::class.functions
            .find { it.name == nodePropertyName }
            ?.call(node)
            ?: error("Cannot find property $nodePropertyName in node with id $nodeId!")) as Property<Any>
    }
}