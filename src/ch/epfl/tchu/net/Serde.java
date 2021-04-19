package ch.epfl.tchu.net;

import java.util.EnumSet;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Function;

//TODO c'est une interface générique, donc faut mettre le type des éléments que serde peut (dé)sérialiser
public interface Serde<E>{

    abstract String serialize(Object object);
    abstract Object deserialize(String string);

    //TODO j'ai mis object en type, parce que je sais pas quoi mettre d'autre
     static Serde<Object> of(Function<Object, String> serialize, Function<String, Object> deserialize){
        return new Serde<>() {
            @Override
            public String serialize(Object object) {
                return serialize.apply(object);
            }

            @Override
            public Object deserialize(String string) {
                return deserialize.apply(string);
            }
        };
    }

    static Serde oneOf(List<EnumSet<T>> list){
         return new Serde() {
             @Override
             public String serialize(Object object) {
                 return null;
             }

             @Override
             public Object deserialize(String string) {
                 return null;
             }

         }
    }



}
