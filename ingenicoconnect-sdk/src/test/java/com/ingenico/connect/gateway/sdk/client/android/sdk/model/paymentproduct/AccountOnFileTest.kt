package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct

import com.ingenico.connect.gateway.sdk.client.android.testUtil.GsonHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Junit Testclass which tests account on file keyvaluepairs and masking
 */
@RunWith(MockitoJUnitRunner::class)
class AccountOnFileTest {
    private val accountOnFile: AccountOnFile = GsonHelper.fromResourceJson(
        "accountOnFileVisa.json",
        AccountOnFile::class.java
    )

    @Test
    fun testIsEditingAllowed() {
        val aofaAlias: KeyValuePair = accountOnFile.attributes[0]
        assertFalse(aofaAlias.isEditingAllowed)
        val aofaFirstName: KeyValuePair =
            accountOnFile.attributes[1]
        assertFalse(aofaFirstName.isEditingAllowed)
        val aofaSurname: KeyValuePair = accountOnFile.attributes[2]
        assertFalse(aofaSurname.isEditingAllowed)
        val aofaCardholderName: KeyValuePair =
            accountOnFile.attributes[3]
        assertFalse(aofaCardholderName.isEditingAllowed)
        val aofaCardNumber: KeyValuePair =
            accountOnFile.attributes[4]
        assertFalse(aofaCardNumber.isEditingAllowed)
        val aofaCvv: KeyValuePair = accountOnFile.attributes[5]
        assertTrue(aofaCvv.isEditingAllowed)
        val aofaExpiryDate: KeyValuePair =
            accountOnFile.attributes[6]
        assertTrue(aofaExpiryDate.isEditingAllowed)
    }

    @Test
    fun testMaskingValue() {
        assertEquals("4111 11XX XXXX 1111 ", accountOnFile.getMaskedValue("alias"))
    }

    @Test
    fun testCustomMaskingValue() {
        assertEquals("41 111", accountOnFile.getMaskedValue("alias", "{{xx}} {{xxx}}"))
    }
}
