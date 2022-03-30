package data.elements;

import data.graph.Point;

import java.util.ArrayList;

public class Stack<T extends Point> {

    private ArrayList<T> stack = new ArrayList<>();
    private int index = 0;

    public void push(T item) {
        stack.add(item);
        index++;
    }

    public T pop() {
        if (index == 0)
            throw new RuntimeException("Stos pusty, nie można wyjąć elementów");
        return stack.remove(--index);
    }

    public int getSize() {
        return index;
    }

    public T peek() {
        if (index == 0)
            throw new RuntimeException("Stos jest pusty");
        return stack.get(index - 1);
    }

    public ArrayList<T> getElementsOnStack() {
        return stack;
    }
}


