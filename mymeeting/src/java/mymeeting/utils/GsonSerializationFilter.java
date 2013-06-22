/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymeeting.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.util.Set;

/**
 *
 * @author marber
 */
 public class GsonSerializationFilter implements ExclusionStrategy {

   public GsonSerializationFilter() {
   }

   public boolean shouldSkipClass(Class<?> clazz) {
     if(clazz.equals(Set.class)){
       return true;
     }
     return false;
   }

   public boolean shouldSkipField(FieldAttributes f) {
     return false;
   }
 }
