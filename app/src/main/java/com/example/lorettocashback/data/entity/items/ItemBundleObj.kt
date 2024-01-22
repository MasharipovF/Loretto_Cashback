package com.example.lorettocashback.data.entity.items


import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.util.Transformable
import com.google.gson.annotations.SerializedName

data class ItemBundleObjVal(
    @SerializedName("@odata.context")
    var odataContext: String?,
    @SerializedName("@odata.nextLink")
    var odataNextLink: String?,
    @SerializedName("value")
    var value: List<ItemBundleObj?>?,
) : Transformable<List<ItemBundle>> {
    data class ItemBundleObj(
        @SerializedName("ChildCode")
        var childCode: String?,
        @SerializedName("ChildName")
        var childName: String?,
        @SerializedName("ChildQuantity")
        var childQuantity: Double?,
        @SerializedName("FatherCode")
        var fatherCode: String?,
        @SerializedName("FatherQuantity")
        var fatherQuantity: Double?,
        @SerializedName("OnHand")
        var onHand: Double?,
        @SerializedName("WhsCode")
        var whsCode: String?,
    )

    override fun transform(): List<ItemBundle> {
        val bundle: ArrayList<ItemBundle> = arrayListOf()

        val groupResult = value?.groupBy { it?.fatherCode }

        groupResult?.entries?.forEach {

            if (it.value.isNotEmpty()) {
                val fatherCode = it.key
                val fatherQuantity = it.value[0]?.fatherQuantity ?: 1.0

                val bundleLines: ArrayList<ItemBundle.ItemBundleLines> = arrayListOf()
                it.value.forEach { line ->
                    val bundleLine = ItemBundle.ItemBundleLines(
                        childCode = line?.childCode ?: GeneralConsts.EMPTY_STRING,
                        childName = line?.childName ?: GeneralConsts.EMPTY_STRING,
                        childQuantity = line?.childQuantity ?: 0.0,
                        onHand = line?.onHand ?: 0.0,
                        whsCode = line?.whsCode ?: GeneralConsts.EMPTY_STRING
                    )
                    bundleLines.add(bundleLine)
                }

                bundle.add(
                    ItemBundle(
                        fatherCode = fatherCode?: GeneralConsts.EMPTY_STRING,
                        fatherQuantity = fatherQuantity,
                        bundleLines = bundleLines
                    )
                )

            }


        }


        return bundle

    }
}