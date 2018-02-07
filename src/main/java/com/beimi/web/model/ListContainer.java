package com.beimi.web.model;

import java.util.List;

public class ListContainer<T> {

    public ListContainer(List<T> list){
        this.list = list;
    }

    private List<T> list;

    public List<T> getData() {
        return list;
    }

    public void setData(List<T> list) {
        this.list = list;
    }
}
