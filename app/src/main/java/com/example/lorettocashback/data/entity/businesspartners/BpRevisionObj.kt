package com.example.lorettocashback.data.entity.businesspartners


import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.util.ObjectTypes
import com.example.lorettocashback.util.Transformable
import com.google.gson.annotations.SerializedName

data class BpRevisionObjVal(
    @SerializedName("@odata.context")
    var odataContext: String?,
    @SerializedName("@odata.nextLink")
    var odataNextLink: String?,
    @SerializedName("value")
    var value: List<BpRevisionObj?>?,
) : Transformable<BpRevision?> {
    data class BpRevisionObj(
        @SerializedName("CardCode")
        var cardCode: String?,
        @SerializedName("CardName")
        var cardName: String?,
        @SerializedName("DocDate")
        var docDate: String?,
        @SerializedName("DocEntry")
        var docEntry: Int?,
        @SerializedName("DocNum")
        var docNum: Int?,
        @SerializedName("Total")
        var total: Double?,
        @SerializedName("TotalUZS")
        var totalUZS: Double?,
        @SerializedName("TotalCash")
        var totalCash: Double?,
        @SerializedName("TotalCard")
        var totalCard: Double?,
        @SerializedName("TotalEPayment")
        var totalEPayment: Double?,
        @SerializedName("TotalBankTransfer")
        var totalBankTransfer: Double?,
        @SerializedName("TransId")
        var transId: Int?,
        @SerializedName("Type")
        var type: Int?,
    )

    override fun transform(): BpRevision? {
        var bpRevision: BpRevision? = null

        var cardCode: String? = null
        var cardName: String? = null
        var beginningBalance: Double = 0.0
        var endingBalance: Double = 0.0
        var cumulativeSum: Double = 0.0

        if (!value?.filter { it?.type != ObjectTypes.BEGINNING_BALANCE.objectCode && it?.type != ObjectTypes.ENDING_BALANCE.objectCode }
                .isNullOrEmpty()) {

            cardCode = value!![0]?.cardCode ?: GeneralConsts.EMPTY_STRING
            cardName = value!![0]?.cardName ?: GeneralConsts.EMPTY_STRING
            beginningBalance =
                value!!.find { it?.type == ObjectTypes.BEGINNING_BALANCE.objectCode }?.total ?: 0.0
            endingBalance =
                value!!.find { it?.type == ObjectTypes.ENDING_BALANCE.objectCode }?.total ?: 0.0
            cumulativeSum = beginningBalance

            val bpRevisionLines: ArrayList<BpRevision.BpRevisionLines> = arrayListOf()
            value?.filter { it?.type != ObjectTypes.BEGINNING_BALANCE.objectCode && it?.type != ObjectTypes.ENDING_BALANCE.objectCode }
                ?.forEach {
                    cumulativeSum += it?.total ?: 0.0
                    bpRevisionLines.add(
                        BpRevision.BpRevisionLines(
                            docDate = it?.docDate ?: GeneralConsts.EMPTY_STRING,
                            docEntry = it?.docEntry ?: -1,
                            docNum = it?.docNum ?: -1,
                            total = it?.total ?: 0.0,
                            totalUZS = it?.totalUZS ?: 0.0,
                            totalCash = it?.totalCash ?: 0.0,
                            totalCard = it?.totalCard ?: 0.0,
                            totalEPayment = it?.totalEPayment ?: 0.0,
                            totalBankTransfer = it?.totalBankTransfer ?: 0.0,
                            cumulativeTotal = cumulativeSum,
                            transId = it?.transId ?: -1,
                            type = ObjectTypes.values()
                                .find { objectTypes -> objectTypes.objectCode == it?.type }
                        )
                    )
                }



            bpRevision = BpRevision(
                cardCode = cardCode,
                cardName = cardName,
                beginningBalance = beginningBalance,
                endingBalance = endingBalance,
                documentLines = bpRevisionLines
            )
        }


        return bpRevision

    }
}