package com.example.zambosafe

import com.example.zambosafe.model.Agency
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AgencyTest {

    private val fireStation = Agency(
        id = "1",
        name = "Sample Fire Station",
        category = "Fire",
        location = "Tetuan",
        phone = "0000-000-0000",
        facebookUrl = null,
        websiteUrl = null
    )

    @Test
    fun matches_emptyQueryMatchesEverything() {
        assertTrue(fireStation.matches(""))
        assertTrue(fireStation.matches("   "))
    }

    @Test
    fun matches_isCaseInsensitiveAcrossFields() {
        assertTrue(fireStation.matches("fire"))      // name + category
        assertTrue(fireStation.matches("TETUAN"))    // location
        assertTrue(fireStation.matches("0000"))      // phone
    }

    @Test
    fun matches_rejectsUnrelatedText() {
        assertFalse(fireStation.matches("police"))
    }
}
