/* 
 * Piwik Java Tracker
 * 
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

/**
 *
 * @author brettcsorba
 */
public final class CustomVariable{
    private final String key;
    private final String value;
    
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

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
