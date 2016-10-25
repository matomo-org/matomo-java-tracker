/* 
 * Piwik Java Tracker
 * 
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author brettcsorba
 */
class CustomVariableList{
    private final ConcurrentHashMap<Integer, CustomVariable> map = new ConcurrentHashMap<>();

    void add(CustomVariable cv){
        boolean found = false;
        for (Entry<Integer, CustomVariable> entry : map.entrySet()){
            if (entry.getValue().getKey().equals(cv.getKey())){
                map.put(entry.getKey(), cv);
                found = true;
            }
        }
        if (!found){
            int i = 1;
            while (map.putIfAbsent(i++, cv) != null){}        
        }
    }
    
    void add(CustomVariable cv, int index){
        if (index <= 0){
            throw new IllegalArgumentException("Index must be greater than 0.");
        }
        map.put(index, cv);
    }
    
    CustomVariable get(int index){
        if (index <= 0){
            throw new IllegalArgumentException("Index must be greater than 0.");
        }
        return map.get(index);
    }
    
    String get(String key){
        Iterator<Entry<Integer, CustomVariable>> i = map.entrySet().iterator();
        while (i.hasNext()){
            CustomVariable value = i.next().getValue();
            if (value.getKey().equals(key)){
                return value.getValue();
            }
        }
        return null;
    }
    
    void remove(int index){
        map.remove(index);
    }
    
    void remove(String key){
        Iterator<Entry<Integer, CustomVariable>> i = map.entrySet().iterator();
        while (i.hasNext()){
            Entry<Integer, CustomVariable> entry = i.next();
            if (entry.getValue().getKey().equals(key)){
                i.remove();
            }
        }
    }
    
    boolean isEmpty(){
        return map.isEmpty();
    }
        
    @Override
    public String toString(){
        JsonObjectBuilder ob = Json.createObjectBuilder();
        
        for (Entry<Integer, CustomVariable> entry : map.entrySet()){
            JsonArrayBuilder ab = Json.createArrayBuilder();
            ab.add(entry.getValue().getKey());
            ab.add(entry.getValue().getValue());
            ob.add(entry.getKey().toString(), ab);
        }
        
        return ob.build().toString();
    }
}
