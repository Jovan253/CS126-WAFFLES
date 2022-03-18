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
    Long[] blacklistArray = new Long[10];
    private int count = 0;

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
        blacklistArray[count] = 1112223334445556L;
        count++;      
    }

    public boolean addRestaurant(Restaurant restaurant) {    
        if (dataChecker.isValid(restaurant) == false){
            return false;        
        }                

        for (int i=0;i<restaurantArray.size();i++){              
            //System.out.println(dataChecker.extractTrueID(restaurantArray.get(i).getRepeatedID()) + " " + dataChecker.extractTrueID(restaurant.getRepeatedID()));
            if (restaurantArray.get(i).getID() == restaurant.getID()){ 
                restaurantArray.remove(restaurant);
                //System.out.println(dataChecker.extractTrueID(restaurant.getRepeatedID()));
                blacklistArray[count] = restaurant.getID();
                count ++;
                return false;
            }
            
        }
        for (int j=0;j<blacklistArray.length;j++){
            if (restaurant.getID() == blacklistArray[j]){     
                //customerArray.remove(customer);
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

    public static void mergeSort(Restaurant[] a, int n, String str) {
        if (n < 2) {
            return;
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
        mergeSort(l, mid, str);
        mergeSort(r, n - mid, str);
    
        merge(a, l, r, mid, n - mid, str);
        //return a;
    }
    
    public static void merge(Restaurant[] a, Restaurant[] l, Restaurant[] r, int left, int right, String str) {
        int i = 0;
        int j = 0; 
        int k = 0;
        if (str == "id"){            
            while (i < left && j < right) {
                if (l[i].getID() < r[j].getID()) {
                    a[k++] = l[i++];
                }
                else {
                    a[k++] = r[j++];
                }
            }
        }
        else if (str == "name"){            
            while (i < left && j < right) {
                if (l[i].getName().toLowerCase().compareTo(r[j].getName().toLowerCase()) < 0) {
                    a[k++] = l[i++];
                }
                else if (l[i].getName().toLowerCase().compareTo(r[j].getName().toLowerCase()) > 0){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getID() < r[j].getID()) {
                        a[k++] = l[i++];
                    }
                    else {
                        a[k++] = r[j++];
                    }
                }
            }
        }
        else if (str == "date"){
            while (i < left && j < right) {
                if (l[i].getDateEstablished().compareTo(r[j].getDateEstablished()) > 0) {
                    a[k++] = l[i++];
                }
                else if (l[i].getDateEstablished().compareTo(r[j].getDateEstablished()) < 0){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getName().toLowerCase().compareTo(r[j].getName().toLowerCase()) < 0) {
                        a[k++] = l[i++];
                    }
                    else if (l[i].getName().toLowerCase().compareTo(r[j].getName().toLowerCase()) > 0){
                        a[k++] = r[j++];
                    }
                    else{
                        if (l[i].getID() < r[j].getID()) {
                            a[k++] = l[i++];
                        }
                        else {
                            a[k++] = r[j++];
                        }
                    }
                }
            }
        }
        else if (str == "star"){
            while (i < left && j < right) {
                if (l[i].getWarwickStars() > r[j].getWarwickStars()) {
                    a[k++] = l[i++];
                }
                else if (l[i].getWarwickStars() < r[j].getWarwickStars()){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getName().toLowerCase().compareTo(r[j].getName().toLowerCase()) < 0) {
                        a[k++] = l[i++];
                    }
                    else if (l[i].getName().toLowerCase().compareTo(r[j].getName().toLowerCase()) > 0){
                        a[k++] = r[j++];
                    }
                    else{
                        if (l[i].getID() < r[j].getID()) {
                            a[k++] = l[i++];
                        }
                        else {
                            a[k++] = r[j++];
                        }
                    }
                }
            }
        }
        else if (str == "rating"){
            while (i < left && j < right) {
                if (l[i].getCustomerRating() < r[j].getCustomerRating()) {
                    a[k++] = l[i++];
                }
                else if (l[i].getCustomerRating() > r[j].getCustomerRating()){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getName().toLowerCase().compareTo(r[j].getName().toLowerCase()) < 0) {
                        a[k++] = l[i++];
                    }
                    else if (l[i].getName().toLowerCase().compareTo(r[j].getName().toLowerCase()) > 0){
                        a[k++] = r[j++];
                    }
                    else{
                        if (l[i].getID() < r[j].getID()) {
                            a[k++] = l[i++];
                        }
                        else {
                            a[k++] = r[j++];
                        }
                    }
                }
            }
        }
        
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
        
    }

    public static void mergeSortDistance(RestaurantDistance[] a, int n) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        RestaurantDistance[] l = new RestaurantDistance[mid];
        RestaurantDistance[] r = new RestaurantDistance[n - mid];
    
        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        mergeSortDistance(l, mid);
        mergeSortDistance(r, n - mid);
    
        mergeDistance(a, l, r, mid, n - mid);
        //return a;
    }

    public static void mergeDistance(RestaurantDistance[] a, RestaurantDistance[] l, RestaurantDistance[] r, int left, int right) {
        int i = 0;
        int j = 0; 
        int k = 0;        

        while (i < left && j < right) {
            if (l[i].getDistance() < r[j].getDistance()) {
                a[k++] = l[i++];
            }
            else if (l[i].getDistance() > r[j].getDistance()){
                a[k++] = r[j++];
            }
            else{
                if (l[i].getRestaurant().getID() < r[j].getRestaurant().getID()) {
                    a[k++] = l[i++];
                }
                else {
                    a[k++] = r[j++];
                }
            }
        }

        
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
        
    }

    public Restaurant[] getRestaurants() {
        String str = "id";                      
        Restaurant[] array = new Restaurant[restaurantArray.size()];   
        
        for (int i=0;i<array.length;i++){ 
            array[i] = restaurantArray.get(i);                       
        }
                      
        mergeSort(array, array.length, str);
        return array;
    }

    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        String str = "id";                      
                      
        mergeSort(restaurants, restaurants.length, str);
        return restaurants;
    }

    public Restaurant[] getRestaurantsByName() {
        String str = "name";           
        Restaurant[] array = new Restaurant[restaurantArray.size()];          
        for (int i=0;i<array.length;i++){ 
            array[i] = restaurantArray.get(i);
        }
        
        mergeSort(array, array.length, str);
        return array;  
    }

    public Restaurant[] getRestaurantsByDateEstablished() {
        String str = "date";           
        Restaurant[] array = new Restaurant[restaurantArray.size()];          
        for (int i=0;i<array.length;i++){ 
            array[i] = restaurantArray.get(i);
        }
        
        mergeSort(array, array.length, str);
        return array; 
    }

    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        String str = "date";                      
                      
        mergeSort(restaurants, restaurants.length, str);
        return restaurants;
    }

    public Restaurant[] getRestaurantsByWarwickStars() {
        String str = "star";           
        Restaurant[] array = new Restaurant[restaurantArray.size()];          
        for (int i=0;i<array.length;i++){ 
            array[i] = restaurantArray.get(i);
        }
        
        mergeSort(array, array.length, str);
        return array; 
    }

    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        // if lenth 0 return new Restaurant[0]
        String str = "rating";                      
                      
        mergeSort(restaurants, restaurants.length, str);
        return restaurants;
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        if (restaurantArray.size() != 0){              
            RestaurantDistance[] array = new RestaurantDistance[restaurantArray.size()];        
            for (int i=0; i< restaurantArray.size();i++){            
                float distance = HaversineDistanceCalculator.inKilometres(latitude, longitude, restaurantArray.get(i).getLatitude()
                , restaurantArray.get(i).getLongitude());
                //System.out.println("Distance " + distance);            
                array[i] = new RestaurantDistance(restaurantArray.get(i), distance);
                //System.out.println("Array " + array[i]);            
            }
            mergeSortDistance(array, array.length); // need different merge sort!!!            
            return array;
        }
        else{        
            return new RestaurantDistance[0];
        }        
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {             
        RestaurantDistance[] array = new RestaurantDistance[restaurantArray.size()];        
        for (int i=0; i< restaurants.length;i++){            
            float distance = HaversineDistanceCalculator.inKilometres(latitude, longitude, restaurants[i].getLatitude()
            , restaurants[i].getLongitude());
            //System.out.println("Distance " + distance);            
            array[i] = new RestaurantDistance(restaurants[i], distance);
            //System.out.println("Array " + array[i]);            
        }
        mergeSortDistance(array, array.length); // need different merge sort!!!            
        return array;
    }

    public static int isSubstring(String s1, String s2){        
        int M = s1.length();
        int N = s2.length();
 
        /* A loop to slide pat[] one by one */
        for (int i = 0; i <= N - M; i++) {
            int j;
 
            /* For current index i, check for
            pattern match */
            for (j = 0; j < M; j++)
                if (s2.charAt(i + j)
                    != s1.charAt(j))
                    break;
 
            if (j == M)
                return i;
        }
 
        return -1;
    }

    public Restaurant[] getRestaurantsContaining(String searchTerm) {
//        return new Restaurant[0];
        String str = "name";
        boolean twoNames = false;        
        int counter = 0;
        //System.out.println(searchTerm);
        String searchTermNew;
        ConvertToPlace converted = new ConvertToPlace();
        Restaurant[] same = new Restaurant[restaurantArray.size()];

        if (searchTerm.contains("-")){
            twoNames = true;
            searchTerm = searchTerm.replace("-", "");
        }

        searchTermNew = StringFormatter.convertAccents(searchTerm);
        //System.out.println(searchTermNew);
        searchTermNew = searchTermNew.toLowerCase();        
        //System.out.println(searchTermNew);

        for (int i=0;i<restaurantArray.size();i++){
            if(twoNames != true) {         
                //System.out.println("Add for a name " + counter);                    
                
                //converted.convert(restaurantArray.get(i).getLatitude(), restaurantArray.get(i).getLongitude());
                if (restaurantArray.get(i).getName().toLowerCase().contains(searchTermNew)){  
                    //System.out.println("Add for first name " + counter);                  
                    same[counter]=restaurantArray.get(i);
                    counter++;
                }
                else if (restaurantArray.get(i).getCuisine().toString().toLowerCase().contains(searchTermNew)){                    
                    //System.out.println("Add for last name " + counter);                  
                    same[counter]=restaurantArray.get(i);
                    counter++;
                }
                else if (converted.convert(restaurantArray.get(i).getLatitude(), restaurantArray.get(i).getLongitude()).getName().contains(searchTermNew)){                 
                    System.out.println("Add for converted ");                  
                    same[counter]=restaurantArray.get(i);
                    counter++;
                }
            }
            else {
                String joinedWord = "bruh";
                if (isSubstring(searchTermNew, joinedWord) != -1) { // if the search term is a subset of the joined customerArray word then we add customer to the array
                    same[counter]=restaurantArray.get(i);
                    counter++;
                }
            }
        }

        Restaurant[] array = new Restaurant[counter];
        for (int j=0;j<counter;j++){
            array[j] = same[j];
        }

        mergeSort(array, counter, str);
        return array;    
    }
}
