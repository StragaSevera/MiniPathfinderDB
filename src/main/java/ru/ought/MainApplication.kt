package ru.ought

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage


class MainApplication : Application() {
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<GridPane>(javaClass.getResource("/enchantments.fxml"))
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