package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

import java.lang.annotation.Repeatable;
import java.util.Date;

public class DataChecker implements IDataChecker {

    public DataChecker() {
        // Initialise things here
    }

    public Long extractTrueID(String[] repeatedID) {        
        if (repeatedID.length != 3){
            return null;            
        }
        String convertId = "";
        if (repeatedID[0].equals(repeatedID[1]))
            convertId = repeatedID[0];
        else if (repeatedID[0].equals(repeatedID[2]))
            convertId = repeatedID[0];
        else if (repeatedID[1].equals(repeatedID[2]))
            convertId = repeatedID[1];
        else
            return null;
        
        Long id = Long.parseLong(convertId);
        if (isValid(id) == false){
            return null;
        }
        return id;        
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
        if (customer == null || customer.getFirstName() == null || customer.getLastName() == null 
        || customer.getDateJoined() == null || customer.getStringID() == null){
            return false;
        }
        Long id = Long.parseLong(customer.getStringID());
        return isValid(id);
    }

    public boolean isValid(Restaurant restaurant) {  

        if (restaurant == null || restaurant.getLastInspectedDate().compareTo(restaurant.getDateEstablished()) < 0
        || restaurant.getFoodInspectionRating() < 0 || restaurant.getFoodInspectionRating() > 5 || restaurant.getWarwickStars() <0
        || restaurant.getWarwickStars() > 5 || !(restaurant.getCustomerRating() == 0.0 || (restaurant.getCustomerRating() > 1.0 && restaurant.getCustomerRating() <= 5.0))
        ){
            return false;
        }
                
        Long validID = extractTrueID(restaurant.getRepeatedID());        
        // if (!isValid(validID) || validID == null){
        //     return false;
        // }
        restaurant.setID(validID); 
        return true;
    }

    public boolean isValid(Favourite favourite) {
        if (favourite == null || isValid(favourite.getCustomerID()) == false || isValid(favourite.getRestaurantID()) == false ){
            return false;
        }
        Long id = Long.parseLong(favourite.getStringID());
        return isValid(id);
    }

    public boolean isValid(Review review) {
        // TODO
        return false;
    }
}