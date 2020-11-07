package ru.ought.fx_properties

import javafx.beans.property.*
import kotlin.reflect.full.isSubclassOf

fun <T> fx(fxProperty: Property<T>, id: String = "", property: String = ""): FXDelegatedPropertyProvider<T> =
    fxDirectional(fxProperty, DoubleBindingDirection.ViewToController, id, property)

fun <T> fxView(fxProperty: Property<T>, id: String = "", property: String = ""): FXDelegatedPropertyProvider<T> =
    fxDirectional(fxProperty, DoubleBindingDirection.ControllerToView, id, property)

fun <T> fxDirectional(
    fxProperty: Property<T>,
    direction: DoubleBindingDirection,
    id: String = "",
    property: String = ""
): FXDelegatedPropertyProvider<T> =
    FXDelegatedPropertyProvider(FXPropertyInfo(fxProperty, direction, id, property))

inline fun <reified T> fxView(seed: T? = null, id: String = "", property: String = ""): FXDelegatedPropertyProvider<T> =
    fxDirectional(DoubleBindingDirection.ControllerToView, seed, id, property)

inline fun <reified T> fx(seed: T? = null, id: String = "", property: String = ""): FXDelegatedPropertyProvider<T> =
    fxDirectional(DoubleBindingDirection.ViewToController, seed, id, property)

// TODO: Add binding of list and map properties
@Suppress("UNCHECKED_CAST")
inline fun <reified T> fxDirectional(
    direction: DoubleBindingDirection,
    seed: T? = null,
    id: String = "",
    property: String = ""
): FXDelegatedPropertyProvider<T> {
    val t = T::class
    val requiredProperty = when {
        t.isSubclassOf(String::class) -> SimpleStringProperty(seed as? String ?: "")
        t.isSubclassOf(Int::class) -> SimpleIntegerProperty(seed as Int)
        t.isSubclassOf(Long::class) -> SimpleLongProperty(seed as Long)
        t.isSubclassOf(Float::class) -> SimpleFloatProperty(seed as Float)
        else -> if (seed == null) SimpleObjectProperty() else SimpleObjectProperty(seed as T)
    }
    return FXDelegatedPropertyProvider(
        FXPropertyInfo(
            requiredProperty,
            direction,
            id,
            property
        )
    ) as FXDelegatedPropertyProvider<T>
}