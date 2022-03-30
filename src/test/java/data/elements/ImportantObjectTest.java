package data.elements;

import exceptions.WrongNumericArgumentException;
import org.junit.Test;

import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class ImportantObjectTest {

    private String name= "object";
    private int id =1;
    private double x =90.432;
    private double y= 34;

    @Test(expected= WrongNumericArgumentException.class)
    public void pass_negative_id() throws DataFormatException {
//        given
        int wrongId =-9;
//        when
        ImportantObject o =new ImportantObject(wrongId,name,x,y);
//        then
        assert false;
    }



}