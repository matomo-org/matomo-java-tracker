/* 
 * Piwik Java Tracking API
 * 
 * @link https://github.com/piwik/piwik-java-tracking-api
 * @license https://github.com/piwik/piwik-java-tracking-api/blob/master/LICENSE BSD 3-Clause
 */
package org.piwik.java;

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
