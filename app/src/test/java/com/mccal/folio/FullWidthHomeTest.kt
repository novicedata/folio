package com.mccal.folio

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** The floating Side Bar never reserves its strip: a bottom dock's page always has the width the Side Bar used to take. */
class FullWidthHomeTest {
    private val bottom = LayoutPreset(dockPlacement = DockPlacement.BOTTOM)

    @Test fun `on a phone the bar and the grid use the whole width, with the Side Bar shown or not`() {
        val withRail = homeGeometry(411f, 891f, bottom, true, statusHeight = 160f)
        val without = homeGeometry(411f, 891f, bottom, true, statusHeight = 0f)
        assertTrue(withRail.horizontalDock)
        assertFalse("the bar floats: nothing sits beside it", withRail.dockBesideRail)
        assertTrue(without.horizontalDock)
        assertFalse("nothing to sit beside", without.dockBesideRail)
        assertEquals(411f - 32f, without.gridWidth, .01f)
        assertEquals("the bar's presence no longer changes the grid", withRail.gridWidth, without.gridWidth, .01f)
        assertEquals(411f, without.dockBarRoom, .01f)
        assertTrue(without.iconSize >= withRail.iconSize)
    }

    @Test fun `nothing ever sits beside the Side Bar, and a Side Bar dock keeps the grid it had`() {
        for ((w, h) in listOf(360f to 780f, 411f to 891f, 475f to 751f, 704f to 932f, 932f to 704f)) for (p in listOf(LayoutPreset(), bottom, LayoutPreset(dockPlacement = DockPlacement.SIDE))) {
            val g = homeGeometry(w, h, p, true, statusHeight = 160f)
            assertFalse("$w x $h", g.dockBesideRail)
        }
        val side = LayoutPreset(dockPlacement = DockPlacement.SIDE)
        assertEquals(homeGeometry(411f, 891f, side, true, statusHeight = 160f).gridWidth, homeGeometry(411f, 891f, side, true, statusHeight = 0f).gridWidth, .01f)
    }

    @Test fun `the upright inner screen already used the whole width, so it does not move`() {
        val a = homeGeometry(704f, 932f, LayoutPreset(), true, statusHeight = 160f)
        val b = homeGeometry(704f, 932f, LayoutPreset(), true, statusHeight = 0f)
        assertEquals(a.gridWidth, b.gridWidth, .01f)
        assertEquals(a.dockPitch, b.dockPitch, .01f)
    }
}
