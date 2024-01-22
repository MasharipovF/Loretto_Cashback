package com.example.lorettocashback.data.entity.items


data class ItemBundle(
    var fatherCode: String,
    var fatherQuantity: Double,
    var bundleLines: List<ItemBundleLines?>
) {
    data class ItemBundleLines(
        var childCode: String,
        var childName: String,
        var childQuantity: Double,
        var onHand: Double,
        var whsCode: String
    )
}