/* 
 * Piwik Java Tracker
 * 
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

/**
 * Object representing the custom variable array required by some 
 * Piwik query parameters.  An array is represented by an object that has
 * the index of the entry as the key (1 indexed) and a 2 entry array representing 
 * a custom key and custom value as the value.
 * 
 * @author brettcsorba
 */
public class PiwikJsonObject{
    Map<String, String> map = new HashMap<>();
    
    /**
     * Gets the custom value stored at this custom key.
     * @param key key used to lookup value
     * @return value stored at specified key, null if not present
     */
    public String get(String key){
        return map.get(key);
    }
    
    /**
     * Returns true if this object contains no custom key-value pairs.
     * @return true if this object contains no custom key-value pairs
     */
    public boolean isEmpty(){
        return size() == 0;
    }
    
    /**
     * Puts a custom value at this custom key.
     * @param key key to store value at
     * @param value value to store at specified key
     * @return previous value stored at key if present, null otherwise
     */
    public String put(String key, String value){
        return map.put(key, value);
    }
    
    /**
     * Removes the custom value stored at this custom key.
     * @param key key used to lookup value to remove
     * @return the value that was removed, null if no value was there to remove
     */
    public String remove(String key){
        return map.remove(key);
    }
    
    /** 
     * Returns the number of custom key-value pairs.
     * @return the number of custom key-value pairs
     */
    public int size(){
        return map.size();
    }
    
    /**
     * Produces the JSON string representing this object.<br>
     * <br>
     * For example, if the following values were put into the object<br>
     * <br>
     * {@code ("key1", "value1") } and {@code ("key2", "value2") }<br>
     * <br>
     * {@code {"1": ["key1", "value1"], "2": ["key2": "value2"]} }<br>
     * <br>
     * could be produced.  Note that there is no guarantee about the ordering of
     * the produced JSON based.  The following JSON is also valid and could also
     * be produced:<br>
     * <br>
     * {@code {"1": ["key2", "value2"], "2": ["key1": "value1"]} }.
     * @return the JSON string representation of this object
     */
    @Override
    public String toString(){
        JsonObjectBuilder ob = Json.createObjectBuilder();
        
        int x = 1;
        for (Entry<String, String> entry : map.entrySet()){
            JsonArrayBuilder ab = Json.createArrayBuilder();
            ab.add(entry.getKey());
            ab.add(entry.getValue());
            ob.add(Integer.toString(x++), ab);
        }
        
        return ob.build().toString();
    }
}
