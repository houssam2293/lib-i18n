/*
 * Copyright (C) 2018 Naoghuman's dream
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.naoghuman.lib.i18n.internal;

import com.github.naoghuman.lib.i18n.core.I18NResourceBundle;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * UnitTests to test the {@code Interface} {@link com.github.naoghuman.lib.i18n.core.I18NResourceBundle}
 * with its default implementation {@link com.github.naoghuman.lib.i18n.internal.DefaultI18NResourceBundle}.
 *
 * @since  0.2.0-PRERELEASE
 * @author Naoghuman
 * @see    com.github.naoghuman.lib.i18n.core.I18NResourceBundle
 * @see    com.github.naoghuman.lib.i18n.internal.DefaultI18NResourceBundle
 */
public class DefaultI18NResourceBundleTest {
    
    private static final String RESOURCE_BUNDLE = "com.github.naoghuman.lib.i18n.internal.message"; // NOI18N
    
    public DefaultI18NResourceBundleTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getBaseName() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setBaseName(RESOURCE_BUNDLE);
        
        assertEquals(RESOURCE_BUNDLE, rb.getBaseName());
    }

    @Test(expected = NullPointerException.class)
    public void getBaseNameThrowsNullPointerException() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.getBaseName();
    }

    @Test
    public void setBaseName() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setBaseName(RESOURCE_BUNDLE);
    }

    @Test(expected = NullPointerException.class)
    public void setBaseNameThrowsNullPointerException() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setBaseName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setBaseNameThrowsIllegalArgumentException() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setBaseName("");
    }

    @Test
    public void getString_String() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setBaseName(RESOURCE_BUNDLE);
        
        ObservableList<Locale> supportedLocales = FXCollections.observableArrayList();
        supportedLocales.addAll(Locale.ENGLISH, Locale.GERMAN);
        rb.setSupportedLocales(supportedLocales);
        
        rb.setActualLocale(Locale.ENGLISH);
        assertEquals("Test title", rb.getString("window.title"));
        
        rb.setActualLocale(Locale.GERMAN);
        assertEquals("Test Titel", rb.getString("window.title"));
    }

    @Test(expected = NullPointerException.class)
    public void getString_String_ThrowsNullPointerException() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.getString(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getString_String_ThrowsIllegalArgumentException() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.getString("");
    }

    @Test
    public void testGetString_String_ObjectArr() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setBaseName(RESOURCE_BUNDLE);
        
        ObservableList<Locale> supportedLocales = FXCollections.observableArrayList();
        supportedLocales.addAll(Locale.ENGLISH, Locale.GERMAN);
        rb.setSupportedLocales(supportedLocales);
        
        rb.setActualLocale(Locale.ENGLISH);
        assertEquals("Text with parameter: 2", rb.getString("label.with.parameter", 2));
        
        rb.setActualLocale(Locale.GERMAN);
        assertEquals("Text mit Parameter: 5", rb.getString("label.with.parameter", 5));
    }

    @Test(expected = NullPointerException.class)
    public void testGetString_String_ObjectArr_StringThrowsNullPointerException() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.getString(null, "hello");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetString_String_ObjectArr_StringThrowsIllegalArgumentException() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.getString("", "hello");
    }

    @Test(expected = NullPointerException.class)
    public void testGetString_String_ObjectArr_ObjectArrThrowsNullPointerException() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        String parameter = null;
        rb.getString("key", parameter);
    }

    @Test
    public void getSupportedLocales() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        
        assertTrue(rb.getSupportedLocales().isEmpty());
    }

    @Test
    public void getSupportedLocalesEmpty() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        
        assertTrue(rb.getSupportedLocales().isEmpty());
    }

    @Test
    public void setSupportedLocales() {
        ObservableList<Locale> supportedLocales = FXCollections.observableArrayList();
        supportedLocales.addAll(Locale.ENGLISH, Locale.GERMAN);
        
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setSupportedLocales(supportedLocales);
        
        assertEquals(2, rb.getSupportedLocales().size());
        assertEquals(Locale.ENGLISH, rb.getSupportedLocales().get(0));
        assertEquals(Locale.GERMAN, rb.getSupportedLocales().get(1));
    }

    @Test(expected = NullPointerException.class)
    public void setSupportedLocalesThrowsNullPointerException() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setSupportedLocales(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setSupportedLocalesThrowsIllegalArgumentException() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setSupportedLocales(FXCollections.observableArrayList());
    }

    @Test
    public void getDefaultLocaleIsEnglish() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        
        assertEquals(Locale.ENGLISH, rb.getDefaultLocale());
    }

    @Test
    public void setGetDefaultLocaleSetItalyButIsEnglish() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setDefaultLocale(Locale.ITALY);
        
        assertEquals(Locale.ENGLISH, rb.getDefaultLocale());
    }

    @Test
    public void setGetDefaultLocaleSetItalyAndItIsItaly() {
        ObservableList<Locale> supportedLocales = FXCollections.observableArrayList();
        supportedLocales.addAll(Locale.ITALY, Locale.GERMAN);
        
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setSupportedLocales(supportedLocales);
        rb.setDefaultLocale(Locale.ITALY);
        
        assertEquals(Locale.ITALY, rb.getDefaultLocale());
    }

    @Test
    public void getActualLocaleIsEnglish() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        
        assertEquals(Locale.ENGLISH, rb.getActualLocale());
    }

    @Test
    public void setGetActualLocaleSetItalyButIsEnglish() {
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setActualLocale(Locale.ITALY);
        
        assertEquals(Locale.ENGLISH, rb.getActualLocale());
    }

    @Test
    public void setGetActualLocaleSetItalyAndItIsItaly() {
        ObservableList<Locale> supportedLocales = FXCollections.observableArrayList();
        supportedLocales.addAll(Locale.ITALY, Locale.GERMAN);
        
        I18NResourceBundle rb = new DefaultI18NResourceBundle();
        rb.setSupportedLocales(supportedLocales);
        rb.setActualLocale(Locale.ITALY);
        
        assertEquals(Locale.ITALY, rb.getActualLocale());
    }
    
}
