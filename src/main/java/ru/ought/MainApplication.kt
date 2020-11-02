package ru.ought

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import ru.ought.utils.BindingParser


class MainApplication : Application() {
    override fun start(primaryStage: Stage) {
        val root = BindingParser("/enchantments.fxml").load<GridPane>()
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