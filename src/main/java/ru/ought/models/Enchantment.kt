package ru.ought.models

enum class CostType { BONUS, GP }

data class Enchantment(var name: String, var cost: Int, var costType: CostType, var requirement: String,
                       var summary: String, var description: String) {
    constructor(): this("", 0, CostType.BONUS, "", "", "")
}