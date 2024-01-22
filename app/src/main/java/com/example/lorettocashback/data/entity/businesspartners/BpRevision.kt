package com.example.lorettocashback.data.entity.businesspartners

import com.example.lorettocashback.util.ObjectTypes


data class BpRevision(
    var cardCode: String,
    var cardName: String,
    var documentLines: List<BpRevisionLines>,
    var beginningBalance: Double,
    var endingBalance: Double,
) {
    data class BpRevisionLines(
        var docDate: String,
        var docEntry: Int,
        var docNum: Int,
        var total: Double,
        var totalUZS: Double,
        var totalCash: Double,
        var totalCard: Double,
        var totalEPayment: Double,
        var totalBankTransfer: Double,
        var cumulativeTotal: Double,
        var transId: Int,
        var type: ObjectTypes?,
    )
}