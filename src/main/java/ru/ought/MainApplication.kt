package ru.ought

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import kotlinx.serialization.json.Json
import ru.ought.binding.BindingLoader

val jsonEncoder = Json { prettyPrint = true }

class MainApplication : Application() {
    override fun start(primaryStage: Stage) {
        val root = BindingLoader.from("/enchantments.fxml").load<GridPane>()
        val primaryScene = Scene(root)
        with(primaryStage) {
            minWidth = 440.0
            minHeight = 370.0
            title = "JavaFX and Gradle"
            scene = primaryScene
            show()
        }
        Platform.runLater { root.requestFocus() }
    }
}