package data.elements;

import exceptions.WrongNumericArgumentException;
import org.junit.Test;

import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class HospitalTest {
    private String name= "szpital";
    private int id =1;
    private int beds=200;
    private int freeBeds =100;
    private double x =100.5;
    private double y=122.77;

    @Test(expected= WrongNumericArgumentException.class)
    public void pass_negative_id() throws DataFormatException {
//        given
        int wrongId =-2;
//        when
        Hospital h =new Hospital(wrongId,name,x,y,beds,freeBeds);
//        then
        assert false;
    }

    @Test(expected= WrongNumericArgumentException.class)
    public void pass_negative_beds() throws DataFormatException {
//        given
        int wrongBeds =-200;
//        when
        Hospital h =new Hospital(id,name,x,y,wrongBeds,freeBeds);
//        then
        assert false;
    }

    @Test(expected= WrongNumericArgumentException.class)
    public void pass_negative_freeBeds() throws DataFormatException {
//        given
        int wrongFreeBeds =-99;
//        when
        Hospital h =new Hospital(id,name,x,y,beds,wrongFreeBeds);
//        then
        assert false;
    }

    @Test(expected= WrongNumericArgumentException.class)
    public void pass_less_beds_than_freeBeds() throws DataFormatException {
//        given
        int wrongBeds =100;
        int wrongFreeBeds=900;
//        when
        Hospital h =new Hospital(id,name,x,y,wrongBeds,wrongFreeBeds);
//        then
        assert false;
    }
}