package ru.ought.fx_properties

import kotlin.reflect.KProperty

class FXDelegatedPropertyProvider<T>(private val fxPropertyInfo: FXPropertyInfo<T>) {
    operator fun provideDelegate(thisRef: Any, property: KProperty<*>): FXDelegatedProperty<T> {
        FXDelegatedPropertyManager.registerProperty(thisRef, property.name, fxPropertyInfo)
        return FXDelegatedProperty(fxPropertyInfo)
    }
}

class FXDelegatedProperty<T>(private val fxPropertyInfo: FXPropertyInfo<T>) {

    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        return fxPropertyInfo.fxProperty.value
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        fxPropertyInfo.fxProperty.value = value
    }
}