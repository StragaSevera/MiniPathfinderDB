package ru.ought.binding

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import java.io.InputStream

class BindingLoader(private val data: InputStream) {
    companion object {
        fun from(resourceName: String) = BindingLoader(BindingLoader::class.java.getResourceAsStream(resourceName))

    }

    fun <T : Parent> load(): T {
        val fxmlLoader = FXMLLoader()
        val root = fxmlLoader.load<T>(data)
        val controller = fxmlLoader.getController<Any>()
        BindingManager.performBinding(root, controller)
        BindingManager.callAfterBinding(controller)
        return root
    }
}