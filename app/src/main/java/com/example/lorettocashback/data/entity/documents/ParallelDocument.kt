package com.example.lorettocashback.data.entity.documents

import com.example.lorettocashback.data.entity.businesspartners.BusinessPartners
import com.example.lorettocashback.data.entity.masterdatas.SalesManagers

data class ParallelDocument(
    var BusinessPartner: BusinessPartners?,
    var BpPhone: String?,
    var DocDate: String?,
    var DocDueDate: String?,
    var BaseDocEntry: Long?,
    var DocumentLoaded: Document?,
    var isUpdateMode: Boolean?,
    var WarehouseCode: String?,
    var Manager: SalesManagers?,
    var Comments: String?,
    var BasketList: ArrayList<DocumentLines>,
    var DocTotalUSD: Double?,
    var DocTotalUZS: Double?,
    var Discount: Double?,
    var DiscountedTotalUSD: Double?,
    var DiscountedTotalUZS: Double?,
    var PayByCashUZS: Double?,
    var PayByCardUZS: Double?,
    var PayByePaymentUZS: Double?,
    var payByCashbackUZS: Double?,
    var payByBankTransferSumUZS: Double?,
    var changeSumUZS: Double?,
    var paidSumUZS: Double?,
    var loadedDocument: Document?,
    var insertedDocument: Document?,
    var insertedCashBackDocument: Document?,
    var insertedSalesOrder: Document?,
    var updatedSalesOrder: Boolean?,
    var insertedPaymentUZS: Boolean?,
    var mobileAppId: String?,
    var isBpNameSwitched: Boolean?,
    )

