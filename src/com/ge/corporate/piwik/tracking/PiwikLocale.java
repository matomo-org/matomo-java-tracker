/*
 * Copyright (c) 2015 General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.ge.corporate.piwik.tracking;

import java.util.Locale;

/**
 * Object representing a locale required by some Piwik query parameters.
 * 
 * @author brettcsorba
 */
public class PiwikLocale{
    private Locale locale;
    
    /**
     * Create this PiwikLocale from a Locale.
     * @param locale the locale to create this object from
     */
    public PiwikLocale(Locale locale){
        this.locale = locale;
    }

    /**
     * Gets the locale.
     * @return the locale
     */
    public Locale getLocale(){
        return locale;
    }

    /**
     * Sets the locale.
     * @param locale the locale to set
     */
    public void setLocale(Locale locale){
        this.locale = locale;
    }
    
    /**
     * Returns the locale's lowercase country code.
     * @return the locale's lowercase country code
     */
    @Override
    public String toString(){
        return locale.getCountry().toLowerCase();
    }    
}
