package ru.ought.fx_properties

import javafx.beans.property.*
import kotlin.reflect.full.isSubclassOf

fun <T> fx(fxProperty: Property<T>, id: String = "", prop: String = ""): FXDelegatedPropertyProvider<T> =
    fxDirectional(fxProperty, DoubleBindingDirection.ViewToController, id, prop)

fun <T> fxView(fxProperty: Property<T>, id: String = "", prop: String = ""): FXDelegatedPropertyProvider<T> =
    fxDirectional(fxProperty, DoubleBindingDirection.ControllerToView, id, prop)

fun <T> fxDirectional(
    fxProperty: Property<T>,
    direction: DoubleBindingDirection,
    id: String = "",
    prop: String = ""
): FXDelegatedPropertyProvider<T> =
    FXDelegatedPropertyProvider(FXPropertyInfo(fxProperty, direction, id, prop))

inline fun <reified T> fxView(seed: T? = null, id: String = "", prop: String = ""): FXDelegatedPropertyProvider<T> =
    fxDirectional(DoubleBindingDirection.ControllerToView, seed, id, prop)

inline fun <reified T> fx(seed: T? = null, id: String = "", prop: String = ""): FXDelegatedPropertyProvider<T> =
    fxDirectional(DoubleBindingDirection.ViewToController, seed, id, prop)

// TODO: Add binding of list and map properties
@Suppress("UNCHECKED_CAST")
inline fun <reified T> fxDirectional(
    direction: DoubleBindingDirection,
    seed: T? = null,
    id: String = "",
    prop: String = ""
): FXDelegatedPropertyProvider<T> {
    val t = T::class
    val requiredProperty = when {
        t.isSubclassOf(String::class) -> SimpleStringProperty(seed as? String ?: "")
        t.isSubclassOf(Int::class) -> SimpleIntegerProperty(seed as? Int ?: 0)
        t.isSubclassOf(Long::class) -> SimpleLongProperty(seed as? Long ?: 0L)
        t.isSubclassOf(Float::class) -> SimpleFloatProperty(seed as? Float ?: 0.0f)
        else -> if (seed == null) SimpleObjectProperty() else SimpleObjectProperty(seed as T)
    }
    return FXDelegatedPropertyProvider(
        FXPropertyInfo(
            requiredProperty,
            direction,
            id,
            prop
        )
    ) as FXDelegatedPropertyProvider<T>
}