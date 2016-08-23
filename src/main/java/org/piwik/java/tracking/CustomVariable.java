/* 
 * Piwik Java Tracker
 * 
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

/**
 * A user defined custom variable.
 * @author brettcsorba
 */
public final class CustomVariable{
    private final String key;
    private final String value;
    
    /**
     * Create a new CustomVariable
     * @param key the key of this CustomVariable
     * @param value the value of this CustomVariable
     */
    public CustomVariable(String key, String value){
        if (key == null){
            throw new NullPointerException("Key cannot be null.");
        }
        if (value == null){
            throw new NullPointerException("Value cannot be null.");
        }
        this.key = key;
        this.value = value;
    }

    /**
     * Get the key of this CustomVariable
     * @return the key of this CustomVariable
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the value of this CustomVariable
     * @return the value of this CustomVariable
     */
    public String getValue() {
        return value;
    }
}
