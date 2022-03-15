package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.util.DataChecker;

public class FavouriteStore implements IFavouriteStore {

    private MyArrayList<Favourite> favouriteArray;
    private DataChecker dataChecker;
    private Long[] blacklistArray = new Long[5];   
    private int count = 0;

    public FavouriteStore() {
        // Initialise variables here
        favouriteArray = new MyArrayList<>();
        dataChecker = new DataChecker();
    }

    public Favourite[] loadFavouriteDataToArray(InputStream resource) {
        Favourite[] favouriteArray = new Favourite[0];

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

            Favourite[] loadedFavourites = new Favourite[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int favouriteCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");
                    Favourite favourite = new Favourite(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]));
                    loadedFavourites[favouriteCount++] = favourite;
                }
            }
            csvReader.close();

            favouriteArray = loadedFavourites;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return favouriteArray;
    }

    public void addBlackList(Long id){        
        blacklistArray[count] = 1112223334445556L;
        count++;        
    }

    public boolean addFavourite(Favourite favourite) {
        if (dataChecker.isValid(favourite) == false){
            return false;
        }
        for (int i=0;i<favouriteArray.size();i++){
            if (favouriteArray.get(i).getID() == favourite.getID()){                                
                favouriteArray.remove(favourite);
                blacklistArray[count] = favourite.getID();
                count ++;
                return false;
            }
            else{
                favouriteArray.add(favourite); 
                System.out.println(favouriteArray.size());
            }
        }
        for (int j=0;j<blacklistArray.length;j++){
            if (favourite.getID() == blacklistArray[j]){ // only add customer if not a duplicate and valid and blacklists
                //customerArray.remove(customer);
                return false;          
            } 
        }      
        //System.out.println(favouriteArray.size());                     
        return true;        
    }

    public boolean addFavourite(Favourite[] favourites) {
        if (favourites.length == 0){
            return false;
        }
        for (int i=0; i<favourites.length;i++){
            addFavourite(favourites[i]);
        }        
        return true;
    }

    public Favourite getFavourite(Long id) {
        if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                if (favouriteArray.get(i).getID().equals(id)){
                    return favouriteArray.get(i);        
                }            
            } 
        }
        return null;
    }

    public static void mergeSort(Favourite[] a, int n, boolean isID) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        Favourite[] l = new Favourite[mid];
        Favourite[] r = new Favourite[n - mid];
    
        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        mergeSort(l, mid, isID);
        mergeSort(r, n - mid, isID);
    
        merge(a, l, r, mid, n - mid, isID);
        //return null;
    }

    public static void merge(Favourite[] a, Favourite[] l, Favourite[] r, int left, int right, boolean isID) {
 
        int i = 0, j = 0, k = 0;
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
        else{
            while (i < left && j < right) {
                if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) < 0) {
                    a[k++] = l[i++];
                }
                else  if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) > 0) {
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
        


        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }        
    }

    /*
    public static void mergeSortLong(Long[] a, int n, boolean isID) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        Long[] l = new Long[mid];
        Long[] r = new Long[n - mid];
    
        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        mergeSortLong(l, mid, isID);
        mergeSortLong(r, n - mid, isID);
    
        mergeLong(a, l, r, mid, n - mid, isID);
        //return null;
    }

    public static void mergeLong(Long[] a, Long[] l, Long[] r, int left, int right, boolean isID) {
 
        int i = 0, j = 0, k = 0;
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
        else{
            while (i < left && j < right) {
                if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) < 0) {
                    a[k++] = l[i++];
                }
                else  if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) > 0) {
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
        


        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }        
    }
    */

    public Favourite[] getFavourites() {
        boolean isID = true;                      
        Favourite[] array = new Favourite[favouriteArray.size()];   
        // size fukcing zero??????

        for (int i=0;i<array.length;i++){
            array[i] = favouriteArray.get(i);                      
        }
                      
        mergeSort(array, array.length, isID);
        return array;
    }

    public Favourite[] getFavouritesByCustomerID(Long id) {
        boolean isID = false;
        Favourite[] array = new Favourite[favouriteArray.size()];   
        int counter = 0;

        if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                if (favouriteArray.get(i).getCustomerID().equals(id)){
                    array[counter] = favouriteArray.get(i);        
                    counter ++;
                }            
            } 
        }
        else{
            return new Favourite[0];    
        }
        
        mergeSort(array, counter, isID);
        return array;
    }

    public Favourite[] getFavouritesByRestaurantID(Long id) {
        boolean isID = false;
        Favourite[] array = new Favourite[favouriteArray.size()];   
        int counter = 0;

        if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                if (favouriteArray.get(i).getRestaurantID().equals(id)){
                    array[counter] = favouriteArray.get(i);        
                    counter ++;
                }            
            } 
        }
        else{
            return new Favourite[0];    
        }
        
        mergeSort(array, counter, isID);
        return array;
    }

    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        boolean isID = false;
        Favourite[] array = new Favourite[favouriteArray.size()];   
        int[] count1 = new int[10];
        int[] count2 = new int[10];
        int counter1 = 0;
        int counter2 = 0;
       
        Long resID1;
        Long resID2;
        Long[] common = new Long[6];

        if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                if (favouriteArray.get(i).getCustomerID().equals(customer1ID)){ // find if a customer exists - can be multiple                                                           
                    count1[counter1] = i;
                    counter1++;
                }            
                if (favouriteArray.get(i).getCustomerID().equals(customer2ID)){ // find if a customer exists                                        
                    count2[counter2] = i;
                    counter2++;
                }            
            }
        }
        else{
            return new Long[0];    
        }

        if (counter1 >0 && counter2 > 0){
            for (int i=0; i<count1.length;i++){                
                resID1 = favouriteArray.get(count1[i]).getRestaurantID();
                for (int j=0; j<count2.length;j++){
                    //resID1 = favouriteArray.get(count1[i]).getRestaurantID();                    
                    resID2 = favouriteArray.get(count2[j]).getRestaurantID();
                    if (resID1.equals(resID2)){
                        if (favouriteArray.get(count1[i]).getDateFavourited().compareTo(favouriteArray.get(count2[j]).getDateFavourited()) < 0){
                            common[i] = resID1;    
                        }
                        else{
                            common[i] = resID2;                        
                        }                                             
                    }                    
                }
            }
        }
        //mergeSortLong(common, common.length, isID);
        return common;
    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        boolean isID = false;
        Favourite[] array = new Favourite[favouriteArray.size()];   
        int[] count1 = new int[10];
        int[] count2 = new int[10];
        int counter1 = 0;
        int counter2 = 0;
       
        Long resID1;
        Long resID2;
        Long[] uncommon = new Long[6];

        // first find out if the customer exists - ie the ID matches to a customer
        if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                if (favouriteArray.get(i).getCustomerID().equals(customer1ID)){ // find if a customer exists - can be multiple                                                           
                    count1[counter1] = i;
                    counter1++;
                }            
                if (favouriteArray.get(i).getCustomerID().equals(customer2ID)){ // find if a customer exists                                        
                    count2[counter2] = i;
                    counter2++;
                }            
            }
        }
        else{
            return new Long[0];    
        }

        if (counter1 >0 && counter2 > 0){
            for (int i=0; i<count1.length;i++){
                resID1 = favouriteArray.get(count1[i]).getRestaurantID();
                for (int j=0; j<count2.length;j++){
                    //resID1 = favouriteArray.get(count1[i]).getRestaurantID();
                    resID2 = favouriteArray.get(count2[j]).getRestaurantID();
                    if (!resID1.equals(resID2)){                        
                        uncommon[i] = resID1;                                                                         
                    }                    
                }
            }
        }
        //mergeSortLong(uncommon, common.length, isID);
        return uncommon;
    }

    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        boolean isID = false;
        Favourite[] array = new Favourite[favouriteArray.size()];   
        int[] count1 = new int[10];
        int[] count2 = new int[10];
        int counter1 = 0;
        int counter2 = 0;
       
        Long resID1;
        Long resID2;
        Long[] uncommon = new Long[6];

        // first find out if the customer exists - ie the ID matches to a customer
        if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                if (favouriteArray.get(i).getCustomerID().equals(customer1ID)){ // find if a customer exists - can be multiple                                                           
                    count1[counter1] = i;
                    counter1++;
                }            
                if (favouriteArray.get(i).getCustomerID().equals(customer2ID)){ // find if a customer exists                                        
                    count2[counter2] = i;
                    counter2++;
                }            
            }
        }
        else{
            return new Long[0];    
        }

        if (counter1 >0 && counter2 > 0){
            for (int i=0; i<count1.length;i++){
                resID1 = favouriteArray.get(count1[i]).getRestaurantID();
                for (int j=0; j<count2.length;j++){
                    //resID1 = favouriteArray.get(count1[i]).getRestaurantID();
                    resID2 = favouriteArray.get(count2[j]).getRestaurantID();
                    if (!resID1.equals(resID2)){                        
                        uncommon[i] = resID1;                                                                         
                    }   
                    if (!resID2.equals(resID1)){                        
                        uncommon[i] = resID2;                                                                         
                    }                 
                }
            }
        }
        //mergeSortLong(uncommon, common.length, isID);
        return uncommon;
    }

    public Long[] getTopCustomersByFavouriteCount() {
        Long[] top = new Long[20];
        int counter = 0;

        if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                for (int j=0; j<top.length;j++){
                    if (favouriteArray.get(i).getCustomerID() != top[j]){
                        top[counter] = favouriteArray.get(i).getCustomerID();
                        counter++;
                    }
                    else{
                        //change its postition
                    }
                }                
            }
        }
        else{
            return new Long[20];    
        }
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        // TODO
        return new Long[20];
    }
}
