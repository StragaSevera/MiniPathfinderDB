package ru.ought

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import ru.ought.utils.BindC


class MainApplication : Application() {
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<GridPane>(javaClass.getResource("/enchantments.fxml"))
        BindC.performBinding(root)
        val primaryScene = Scene(root)
        with(primaryStage) {
            minWidth = 440.0
            minHeight = 370.0
            title = "JavaFX and Gradle"
            scene = primaryScene
            show()
        }
    }
}