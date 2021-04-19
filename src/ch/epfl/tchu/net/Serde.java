package ch.epfl.tchu.net;

import java.util.EnumSet;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Function;

//TODO c'est une interface générique, donc faut mettre le type des éléments que serde peut (dé)sérialiser
public interface Serde<E>{

    abstract String serialize(E e);
    abstract E deserialize(String string);

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
                 return element;
             }

         };
    }



}
