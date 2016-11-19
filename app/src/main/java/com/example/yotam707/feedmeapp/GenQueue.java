package com.example.yotam707.feedmeapp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yotam707 on 11/16/2016.
 */

public class GenQueue<E> {
    private LinkedList<E> list = new LinkedList<E>();

    public void enqueue(E e) {
        list.addLast(e);
    }

    public List<E> toList(){
        return new ArrayList<E>(list);
    }
    public E dequeue() {
        return list.removeFirst();
    }

    public void remove(E e){
        list.remove(e);
    }

    public int indexOf(E e){
        return list.indexOf(e);
    }

    public int getSize() {
        return list.size();
    }

    public E getByIndex(int i){
        return list.get(i);
    }

    public E peek() {
        if(getSize() == 0)
            return null;
        return list.getFirst();
    }

}
