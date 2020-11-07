package ru.ought.models

import kotlinx.serialization.Serializable

enum class CostType { Bonus, GP }

@Serializable
data class Enchantment(
    val name: String, val requirement: String,
    val summary: String, val description: String,
    val costBuy: Int, val costCraft: Int, val costType: CostType,
    val slot: Int, val cl: Int,
    val spells: String, val feats: String
)