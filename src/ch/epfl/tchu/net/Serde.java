package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface Serde<E> {

    String serialize(E e);
    E deserialize(String string);

     static <E> Serde<E> of(Function<E, String> serialize, Function<String, E> deserialize){
        return new Serde<>() {
            @Override
            public String serialize(E e) {
                return serialize.apply(e);
            }

            @Override
            public E deserialize(String string) {
                return deserialize.apply(string);
            }
        };
    }


    static <E> Serde<E> oneOf(List<E> values){
         if(values == null){
             throw new NullPointerException("list in null");
         }
         return new Serde<>() {

             @Override
             public String serialize(E e) {
                 if(values.contains(e)) {
                     //System.out.println(Integer.toString(values.indexOf(e)));
                     return Integer.toString(values.indexOf(e));
                 }
                 //TODO wtf
                 return Integer.toString(-1);
             }

             @Override
             public E deserialize(String string) {
                 E element = null;
                 try {
                     element =  values.get(Integer.parseInt(string));
                 }catch (NumberFormatException e){
                     System.out.println("string passed in argument is not an integer");
                 }
                 //System.out.println(element);
                 return element;
             }

         };
    }

    static <E> Serde<List<E>> listOf(Serde<E> serde, String separator){
         return new Serde<>() {

             @Override
             public String serialize(List<E> e) {
                 /*String string = "";
                 for(E element : e){
                     string += serde.serialize(element);
                 }
                 return String.join(Pattern.quote(separator), string);*/
                 List<String> list = new ArrayList<>();
                 for(E element : e) {
                     list.add(serde.serialize(element));
                 }
                 String str = String.join(Pattern.quote(separator), list);
                 return str;
             }

             @Override
             public List<E> deserialize(String string) {
                 String[] splittedString = string.split(Pattern.quote(separator), -1);
                 List<String> list = new ArrayList<>(Arrays.asList(splittedString));
                 List<E> newList = new ArrayList<>();
                 for(String e : list){
                     newList.add(serde.deserialize(e));
                 }
                 return newList;
             }
         };
    }

    static <E extends Comparable<E>> Serde<SortedBag<E>> bagOf(Serde<E> serde, String separator){
         return new Serde<>() {
             @Override
             public String serialize(SortedBag<E> bag) {
                 /*String string = "";
                 for(E e : bag){
                    string += serde.serialize(e);
                 }
                 return String.join(Pattern.quote(separator), string);*/
                 List<String> list = new ArrayList<>();
                 for(E e: bag) {
                     list.add(serde.serialize(e));
                 }
                 //return String.join(",", list); //This one works fine when doing ticketsSortedBagSerde, puts the commas
                 return String.join(Pattern.quote(separator), list);
             }

             @Override
             public SortedBag<E> deserialize(String string) {
                 String[] splittedString = string.split(Pattern.quote(separator), -1);
                 List<String> list = new ArrayList<>(Arrays.asList(splittedString));
                 List<E> newList = new ArrayList<>();
                 for(String e : list){
                     newList.add(serde.deserialize(e));
                 }
                 return SortedBag.of(newList);
             }
         };
    }



}
