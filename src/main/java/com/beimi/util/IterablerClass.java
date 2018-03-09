package com.beimi.util;

import java.util.Iterator;
import java.util.List;

public class IterablerClass<T> implements Iterable<T>{
    private List<T> list = null;

    public IterablerClass(List<T> t){
        this.list = t ;
    }
    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return new Iterator<T>() {
            private Integer index = 0;
            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                return index<list.size();
            }

            @Override
            public T next() {
                // TODO Auto-generated method stub
                return list.get(index++);
            }

            @Override
            public void remove() {
                // TODO Auto-generated method stub
            }
        };
    }
}