package ru.ought.viewmodels

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.SingleSelectionModel
import ru.ought.binding.annotations.AfterBinding
import ru.ought.fx_properties.fx
import ru.ought.fx_properties.fxView


class EnchantmentsViewModel {
    private var nameText: String by fx()
    private var costBuyText: String by fx()

    private var costBuyTypeItems: ObservableList<String> by fx(FXCollections.observableArrayList("bonus", "gp"))

    private var costBuyTypeSelectionModel: SingleSelectionModel<String> by fxView()
    private var costBuyTypeIndex: Int
        get() = costBuyTypeSelectionModel.selectedIndex
        set(value) = costBuyTypeSelectionModel.select(value)


    @AfterBinding
    fun initializeAfterBinding() {
        costBuyTypeIndex = 0
    }

    @FXML
    fun onSave() {
        println(nameText)
        println(costBuyText)
        println(costBuyTypeItems[costBuyTypeIndex])
        costBuyTypeIndex = (costBuyTypeIndex + 1).rem(costBuyTypeItems.size)
    }
}