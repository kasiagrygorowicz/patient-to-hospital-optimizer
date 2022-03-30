package data.elements;

import data.graph.Point;
import org.junit.Test;

import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class StackTest {

    private Stack stack =new Stack();

    @Test
    public void test_push() throws DataFormatException {
        stack.push(new Point(3,78));
        stack.push(new Point(99,78));
        assertEquals(2,stack.getSize(),0);
    }

    @Test
    public void test_pop() throws DataFormatException {
        Point p1 =new Point(3,78);
        Point p2 = new Point(7,6);
        stack.push(p1);
        stack.push(p2);
        assertEquals(p2,stack.pop());
        assertEquals(p1,stack.pop());
        assertEquals(0,stack.getSize());
    }

    @Test(expected = RuntimeException.class)
    public void pop_empty_stack(){
        Stack empty =new Stack();
        empty.pop();
    }

    @Test(expected = RuntimeException.class)
    public void peek_empty_stack(){
        Stack empty =new Stack();
        empty.peek();
    }

}