package edu.stonybrook.cse308.gerrybackend.data;

import lombok.Getter;

public class UnorderedPair<T> {

    @Getter
    private T item1;
    @Getter
    private T item2;

    public UnorderedPair(T item1, T item2){
        this.item1 = item1;
        this.item2 = item2;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        }
        if (!(obj instanceof UnorderedPair)){
            return false;
        }
        @SuppressWarnings("unchecked")
        UnorderedPair<T> otherPair = (UnorderedPair<T>) obj;
        boolean item1Found = (otherPair.contains(this.item1));
        boolean item2Found = (otherPair.contains(this.item2));
        return item1Found && item2Found;
    }

    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + this.item1.hashCode();
        result = 31 * result + this.item2.hashCode();
        return result;
    }

    public boolean contains(T obj){
        return (this.item1.equals(obj) || this.item2.equals(obj));
    }
}
