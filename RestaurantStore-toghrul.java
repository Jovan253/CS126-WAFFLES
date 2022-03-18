package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.Cuisine;
import uk.ac.warwick.cs126.models.EstablishmentType;
import uk.ac.warwick.cs126.models.Place;
import uk.ac.warwick.cs126.models.PriceRange;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.RestaurantDistance;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.util.ConvertToPlace;
import uk.ac.warwick.cs126.util.HaversineDistanceCalculator;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class RestaurantStore implements IRestaurantStore {

    private MyArrayList<Restaurant> restaurantArray;
    private DataChecker dataChecker;
    Long[] blacklistArray = new Long[5];
    private int count1 = 0;

    public RestaurantStore() {
        // Initialise variables here
        restaurantArray = new MyArrayList<>();
        dataChecker = new DataChecker();
    }

    public Restaurant[] loadRestaurantDataToArray(InputStream resource) {
        Restaurant[] restaurantArray = new Restaurant[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Restaurant[] loadedRestaurants = new Restaurant[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            String row;
            int restaurantCount = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Restaurant restaurant = new Restaurant(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            Cuisine.valueOf(data[4]),
                            EstablishmentType.valueOf(data[5]),
                            PriceRange.valueOf(data[6]),
                            formatter.parse(data[7]),
                            Float.parseFloat(data[8]),
                            Float.parseFloat(data[9]),
                            Boolean.parseBoolean(data[10]),
                            Boolean.parseBoolean(data[11]),
                            Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]),
                            Boolean.parseBoolean(data[14]),
                            Boolean.parseBoolean(data[15]),
                            formatter.parse(data[16]),
                            Integer.parseInt(data[17]),
                            Integer.parseInt(data[18]));

                    loadedRestaurants[restaurantCount++] = restaurant;
                }
            }
            csvReader.close();

            restaurantArray = loadedRestaurants;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return restaurantArray;
    }

    public void addBlackList(Long id){
        blacklistArray[count1] = 1112223334445556L;
        count1++;      
    }

    static int isSubstring(String s1, String s2) {
		int M = s1.length();
		int N = s2.length();
		for (int i = 0; i <= N - M; i++) {
			int j;
			for (j = 0; j < M; j++)
				if (s2.charAt(i + j)
					!= s1.charAt(j))
					break;

			if (j == M)
				return i;
		}
		return -1;
	}

    public boolean addRestaurant(Restaurant restaurant) {
        if (dataChecker.isValid(restaurant) == false){
            return false;
        }        
        for (int i=0;i<restaurantArray.size();i++){            
            if (restaurantArray.get(i).getID() == restaurant.getID()){                                
                restaurantArray.remove(restaurant);
                blacklistArray[count1] = restaurant.getID();
                count1++;
                return false;
            }
            
        }
        for (int j=0;j<blacklistArray.length;j++){
            if (restaurant.getID() == blacklistArray[j]){     
                //restaurantArray.remove(customer);
                return false;          
            } 
        }  
        restaurantArray.add(restaurant);                                 
        return true;
    }

    public boolean addRestaurant(Restaurant[] restaurants) {
        if (restaurants.length == 0){
            return false;
        }
        for (int i=0; i<restaurants.length;i++){
            addRestaurant(restaurants[i]);
        }
        return true; 
    }

    public Restaurant getRestaurant(Long id) {
        if (restaurantArray.isEmpty() == false){
            for (int i=0; i<restaurantArray.size();i++){
                if (restaurantArray.get(i).getID().equals(id)){
                    return restaurantArray.get(i);        
                }            
            }               
        }       
        return null; 
    }
    
    public Restaurant[] mergeSortArray(Restaurant[] a, int n, boolean isID, boolean isName, boolean isDate, boolean isDist, boolean isStar) {
        if (n < 2) {
            return a;
        }
        int mid = n / 2;
        Restaurant[] l = new Restaurant[mid];
        Restaurant[] r = new Restaurant[n - mid];
    
        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        mergeSortArray(l, mid, isID);
        mergeSortArray(r, n - mid, isID);
    
        mergeArray(a, l, r, mid, n - mid, isID);
        return a;
    }
    
    public Restaurant[] mergeArray(Restaurant[] a, Restaurant[] l, Restaurant[] r, int left, int right, boolean isID, boolean isName) {
        int i = 0;
        int j = 0; 
        int k = 0;
        if (isID == true){
            while (i < left && j < right) {
                if (l[i].getID() <= r[j].getID()) {
                    a[k++] = l[i++];
                }
                else {
                    a[k++] = r[j++];
                }
            }
        }
        else if (isName == true){
            while (i < left && j < right) {
                if (l[i].getName().compareTo(r[j].getName()) < 0) {
                    a[k++] = l[i++];
                }
                else if (l[i].getName().compareTo(r[j].getName()) > 0){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getID() <= r[j].getID()) {
                        a[k++] = l[i++];
                    }
                    else {
                        a[k++] = r[j++];
                    }
                }
            }
        }
        else if (isDate == true){
            while (i < left && j < right) {
                if (l[i].getDateEstablished().compareTo(r[j].getDateEstablished()) < 0) {
                    a[k++] = l[i++];
                }
                else if (l[i].getDateEstablished().compareTo(r[j].getDateEstablished()) > 0){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getName().compareTo(r[j].getName()) < 0) {
                        a[k++] = l[i++];
                    }
                    else if (l[i].getName().compareTo(r[j].getName()) > 0){
                        a[k++] = r[j++];
                    }
                    else{
                        if (l[i].getID() <= r[j].getID()) {
                            a[k++] = l[i++];
                        }
                        else {
                            a[k++] = r[j++];
                        }
                    }
                }
            }
        }
         else if (isStar == true){
            while (i < left && j < right) {
                if (l[i].getWarwickStars().compareTo(r[j].getWarwickStars()) < 0) {
                    a[k++] = l[i++];
                }
                else if (l[i].getWarwickStars().compareTo(r[j].getWarwickStars()) > 0){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getName().compareTo(r[j].getName()) < 0) {
                        a[k++] = l[i++];
                    }
                    else if (l[i].getName().compareTo(r[j].getName()) > 0){
                        a[k++] = r[j++];
                    }
                    else{
                        if (l[i].getID() <= r[j].getID()) {
                            a[k++] = l[i++];
                        }
                        else {
                            a[k++] = r[j++];
                        }
                    }
                }
            }
        }
        else if (isRating == true){
            while (i < left && j < right) {
                if (l[i].getCustomerRating().compareTo(r[j].getCustomerRating()) < 0) {
                    a[k++] = l[i++];
                }
                else if (l[i].getCustomerRating().compareTo(r[j].getCustomerRating()) > 0){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getName().compareTo(r[j].getName()) < 0) {
                        a[k++] = l[i++];
                    }
                    else if (l[i].getName().compareTo(r[j].getName()) > 0){
                        a[k++] = r[j++];
                    }
                    else{
                        if (l[i].getID() <= r[j].getID()) {
                            a[k++] = l[i++];
                        }
                        else {
                            a[k++] = r[j++];
                        }
                    }
                }
            }
        }
        /*else if (isDist == true){
            while (i < left && j < right) {
                if (l[i].getRestaurantsByDistanceFrom(distance).compareTo(r[j].getRestaurantsByDistanceFrom(distance)) < 0) {
                    a[k++] = l[i++];
                }
                else if (l[i].getRestaurantsByDistanceFrom(distance).compareTo(r[j].getRestaurantsByDistanceFrom(distance)) > 0){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getID() <= r[j].getID()) {
                        a[k++] = l[i++];
                    }
                    else {
                        a[k++] = r[j++];
                    }
                }
            }
        }
        */
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
        return a;
    }
    
    public Restaurant[] getRestaurants() {
        boolean isID = true;   
        Restaurant[] resarray = new Restaurant[restaurantArray.size()]; 
        if (restaurantArray.isEmpty()){
            return null;
        }
        for (int i=0;i<restaurantArray.size();i++){ 
            resarray[i] = restaurantArray.get(i);
        }
        mergeSortArray(resarray, resarray.length, isID);
        return resarray;
    }

    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        if (restaurants.length != 0) {
            boolean isID = true;          
            return mergeSortArray(restaurants, restaurants.length, isID);     
        }
        return null;  
    }

    public Restaurant[] getRestaurantsByName() {
        boolean isName = true;   
        Restaurant[] resarray = new Restaurant[restaurantArray.size()]; 
        if (restaurantArray.isEmpty()){
            return null;
        }
        for (int i=0;i<restaurantArray.size();i++){ 
            resarray[i] = restaurantArray.get(i);
        }
        mergeSortArray(resarray, resarray.length, isName);
        return resarray;      
    }

    public Restaurant[] getRestaurantsByDateEstablished() {
        boolean isDate = true;   
        Restaurant[] resarray = new Restaurant[restaurantArray.size()]; 
        if (restaurantArray.isEmpty()){
            return null;
        }
        for (int i=0;i<restaurantArray.size();i++){ 
            resarray[i] = restaurantArray.get(i);
        }
        mergeSortArray(resarray, resarray.length, isDate);
        return resarray;
    }

    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        if (restaurants.length != 0) {
            boolean isDate = true;          
            return mergeSortArray(restaurants, restaurants.length, isDate);     
        }
        
        return null;  
    }

    public Restaurant[] getRestaurantsByWarwickStars() {
        boolean isStar = true;   
        Restaurant[] resarray = new Restaurant[restaurantArray.size()]; 
        if (restaurantArray.isEmpty()){
            return null;
        }
        for (int i=0;i<restaurantArray.size();i++){ 
            resarray[i] = restaurantArray.get(i);
        }
        mergeSortArray(resarray, resarray.length, isStar);
        return resarray;
    }

    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        if (restaurants.length != 0) {
            boolean isRating = true;          
            return mergeSortArray(restaurants, restaurants.length, isRating);     
        }
        return new Restaurant[0];  
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        //distance = HaversineDistanceCalculator(latitude, longitude);
        return new RestaurantDistance[0];

    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {
        // TODO
        return new RestaurantDistance[0];
    }

    public Restaurant[] getRestaurantsContaining(String searchTerm) {
        boolean isID = false;
        boolean MultipleNames = false;
        MyArrayList<Restaurant> sameArray = new MyArrayList<>();
        //String searchTermConvert;
        String searchTermNew;
        if (searchTerm.contains(" ")){
            MultipleNames = true;
            searchTerm = searchTerm.replace(" ", "");           
        }    
        //searchTermConvert = StringFormatter.convertAccentsFaster(searchTerm); 
        searchTermNew = searchTerm.toLowerCase();


        for (int i=0;i<restaurantArray.size();i++){           
            if(MultipleNames != true) {
                if (restaurantArray.get(i).getName().toLowerCase().contains(searchTermNew)){
                    sameArray.add(restaurantArray.get(i));
                    count1++;
                }
                else if (restaurantArray.get(i).getCuisine().toLowerCase().contains(searchTermNew)){
                    sameArray.add(restaurantArray.get(i));
                    count1++;
                }
            }
            else {
                String testword = restaurantArray.get(i).getName().toLowerCase();
                String testword2 = restaurantArray.get(i).getCuisine().toLowerCase();
                if (isSubstring(searchTermNew, testword) != -1) {
                    sameArray.add(restaurantArray.get(i));
                    count1++;
                }
                else if (isSubstring(searchTermNew, testword2) != -1) {
                    sameArray.add(restaurantArray.get(i));
                    count1++;
                }
            }
        }
        Restaurant[] same = new Restaurant[count1];
        for (int j=0;j<count1;j++){ // potentially implement toArray
            same[j] = sameArray.get(j);
        }
        same = mergeSortArray(same, same.length, isID);
        return same;
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);
        return new Restaurant[0];
    }
}