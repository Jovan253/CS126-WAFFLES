package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

import java.util.Date;

public class DataChecker implements IDataChecker {

    public DataChecker() {
        // Initialise things here
    }

    public Long extractTrueID(String[] repeatedID) {
        // TODO
        return null;
    }

    public boolean isValid(Long inputID) {
        String temp = Long.toString(inputID);

        int[] idArray = new int[temp.length()];

        // add each digit of inputID to an array so it can be checked
        for (int i = 0; i < temp.length(); i++)
        {
            idArray[i] = temp.charAt(i) - '0';
            if (idArray[i]==0){
                return false;
            }
        }
        if (idArray.length!=16){ // check if correct length
            return false;
        }

        int sameDigit;
        for (int i=0; i<idArray.length;i++){
            sameDigit=0;
            for (int j=0; j<idArray.length;j++){
                if (idArray[i]==idArray[j]){
                    sameDigit++;
                }
                if (sameDigit>3){ // if more than three digits the same return false
                    return false;
                }
            }
        }
        return true;        
    }

    public boolean isValid(Customer customer) {
        if (customer == null || customer.getFirstName() == null || customer.getLastName() == null || customer.getDateJoined() == null || customer.getStringID() == null){
            return false;
        }
        Long id = Long.parseLong(customer.getStringID());
        return isValid(id);
    }

    public boolean isValid(Restaurant restaurant) {
        // TODO
        return false;
    }

    public boolean isValid(Favourite favourite) {
        // TODO
        return false;
    }

    public boolean isValid(Review review) {
        // TODO
        return false;
    }
}