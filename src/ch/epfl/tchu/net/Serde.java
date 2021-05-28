package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

/*
  @author Christopher Soriano (326354)
 */

/**
 * represents a serde : an object capable of serialising et deserialising
 * @param <E>
 */
public interface Serde<E> {
    /**
     * abstract method taking for argument the object to serialise
     * @param e, object to serialise
     * @return a string which corresponds to the serialised object
     */
    String serialize(E e);

    /**
     * abstract method taking for argument the object to deserialise
     * @param string, string that will be deserialised
     * @return the deserialised string
     */
    E deserialize(String string);

    /**
     *
     * @param serialize, serialisation function
     * @param deserialize, deserialisation function
     * @param <E> type parameter
     * @return the corresponding serde
     */
    static <E> Serde<E> of(Function<E, String> serialize, Function<String, E> deserialize){
        return new Serde<>() {
            @Override
            public String serialize(E e) { return serialize.apply(e); }

            @Override
            public E deserialize(String string) { return deserialize.apply(string); }
        };
    }

    /**
     *
     * @param values, list of values from a set of enumerated type values
     * @param <E>, type parameter
     * @return corresponding serde
     */
    static <E> Serde<E> oneOf(List<E> values){
         if(values == null) throw new NullPointerException("list is null");
         return new Serde<>() {

             @Override
             public String serialize(E e) {
                 if(e == null) return "";

                 if(!values.contains(e))
                     throw new IllegalArgumentException("element to serialize is not in given list");

                 return Integer.toString(values.indexOf(e));

             }

             @Override
             public E deserialize(String string) {
                 if(string.isEmpty()) return null;

                 E element = null;
                 try {
                     element =  values.get(Integer.parseInt(string));
                 } catch (NumberFormatException e){
                     System.out.println("string passed in argument is not an integer");
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

                 return element;
             }

         };
    }

    /**
     *
     * @param serde, gives values
     * @param separator, seperator caracter
     * @param <E>, type parameter
     * @return a serde capable of (de)serialising lists of values
     */
    static <E> Serde<List<E>> listOf(Serde<E> serde, String separator){
         return new Serde<>() {

             @Override
             public String serialize(List<E> e) {

                 if(e.isEmpty()) { return ""; }

                 List<String> list = new ArrayList<>();
                 e.forEach(element -> list.add(serde.serialize(element)));

                 return String.join(separator, list);
             }

             @Override
             public List<E> deserialize(String string) {
                 String[] splitString = string.split(Pattern.quote(separator), -1);

                 List<E> list = new ArrayList<>();
                 for(String e : splitString)
                     list.add(serde.deserialize(e));

                 return list;
             }
         };
    }

    /**
     * functions like list of but with a sortedBag
     * @param serde, gives values
     * @param separator, sepereator used to seperate the elements
     * @param <E>, type parameter
     * @return a serde capable of (de)serialising lists of values
     */
    static <E extends Comparable<E>> Serde<SortedBag<E>> bagOf(Serde<E> serde, String separator){
         return new Serde<>() {
             @Override
             public String serialize(SortedBag<E> bag) {
                 return listOf(serde, separator).serialize(bag.toList());
             }

             @Override
             public SortedBag<E> deserialize(String string) {
                 if(string.isEmpty()){
                     return SortedBag.of();
                 }
                 return SortedBag.of(listOf(serde, separator).deserialize(string));
             }
         };
    }


}
