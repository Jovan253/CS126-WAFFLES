package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.lang.model.element.Element;

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
        }
        for (int j=0;j<blacklistArray.length;j++){
            if (favourite.getID() == blacklistArray[j]){ // only add customer if not a duplicate and valid and blacklists
                //customerArray.remove(customer);
                return false;          
            } 
        }      
        favouriteArray.add(favourite); 
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

    public static void mergeSort(Favourite[] a, int n, String str) {
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
        mergeSort(l, mid, str);
        mergeSort(r, n - mid, str);
    
        merge(a, l, r, mid, n - mid, str);
        //return null;
    }

    public static void merge(Favourite[] a, Favourite[] l, Favourite[] r, int left, int right, String str) {
 
        int i = 0, j = 0, k = 0;
        if (str == "id"){
            while (i < left && j < right) {
                if (l[i].getID() <= r[j].getID()) {
                    a[k++] = l[i++];
                }
                else {
                    a[k++] = r[j++];
                }
            }
        }
        else if (str == "name"){
            while (i < left && j < right) {
                if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) > 0) {
                    a[k++] = l[i++];
                }
                else  if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) < 0) {
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
        else if (str == "res"){
            while (i < left && j < right) {
                if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) > 0) {
                    a[k++] = l[i++];
                }
                else  if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) < 0) {
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getRestaurantID() <= r[j].getRestaurantID()) {
                        a[k++] = l[i++];
                    }
                    else {
                        a[k++] = r[j++];
                    }
                }
            }
        }
        else if (str == "res2"){
            while (i < left && j < right) {
                if (l[i].getRestaurantID() <= r[j].getRestaurantID()) {
                    a[k++] = l[i++];
                }
                else {
                    a[k++] = r[j++];
                }                
            }
        }
        else if (str == "cTop"){
            while (i < left && j < right) {    
                if (l[i].getCustomerID() < r[j].getCustomerID()){
                    a[k++] = l[i++];
                }   
                else if (l[i].getCustomerID() > r[j].getCustomerID()){
                    a[k++] = r[j++];
                }  
                else{
                    if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) > 0) {
                        a[k++] = l[i++];
                    }
                    else  if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) < 0) {
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
        else if (str == "rTop"){
            while (i < left && j < right) {    
                if (l[i].getRestaurantID() < r[j].getRestaurantID()){
                    a[k++] = l[i++];
                }   
                else if (l[i].getRestaurantID() > r[j].getRestaurantID()){
                    a[k++] = r[j++];
                }  
                else{
                    if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) > 0) {
                        a[k++] = l[i++];
                    }
                    else  if (l[i].getDateFavourited().compareTo(r[j].getDateFavourited()) < 0) {
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
        


        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }        
    }



    public Favourite[] getFavourites() {                           
        Favourite[] array = new Favourite[favouriteArray.size()];           

        for (int i=0;i<array.length;i++){
            array[i] = favouriteArray.get(i);                      
        }
                      
        mergeSort(array, array.length, "id");
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

        Favourite[] sorted = new Favourite[counter];
        //System.out.println(counter);
        for (int j=0;j<counter;j++){
            sorted[j] = array[j];
        }
        
        mergeSort(sorted, counter, "name");        
        return sorted;
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
        
        Favourite[] sorted = new Favourite[counter];
        //System.out.println(counter);
        for (int j=0;j<counter;j++){
            sorted[j] = array[j];
        }
        
        mergeSort(sorted, counter, "name");        
        return sorted;
    }

    // TODO
    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        boolean isID = false;
        Favourite[] array = new Favourite[favouriteArray.size()];   
        int[] count1 = new int[10];
        int[] count2 = new int[10];
        int counter1 = 0;
        int counter2 = 0;
       
        Long resID1;
        Long resID2;
        Favourite[] common = new Favourite[favouriteArray.size()];

        if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                if (favouriteArray.get(i).getCustomerID().equals(customer1ID)){ // find if a customer exists - can be multiple                                                           
                    count1[counter1] = i;
                    counter1++;
                    //System.out.println("count1[] = " + i);
                }            
                if (favouriteArray.get(i).getCustomerID().equals(customer2ID)){ // find if a customer exists                                        
                    count2[counter2] = i;
                    counter2++;
                    //System.out.println("count2[] = " + i);
                }            
            }
        }
        else{
            return new Long[0];    
        }
        //System.out.println(counter1 + " " +  counter2);
        if (counter1 >0 && counter2 > 0){
            for (int i=0; i<counter1;i++){                
                resID1 = favouriteArray.get(count1[i]).getRestaurantID();                
                for (int j=0; j<counter2;j++){
                    //resID1 = favouriteArray.get(count1[i]).getRestaurantID();                    
                    resID2 = favouriteArray.get(count2[j]).getRestaurantID();                    
                    if (resID1.equals(resID2)){                        
                        if (favouriteArray.get(count1[i]).getDateFavourited().compareTo(favouriteArray.get(count2[j]).getDateFavourited()) > 0){
                            //System.out.println("resID1 = " + resID1 + " resID2 = " + resID2);                            
                            common[i] = favouriteArray.get(count1[i]);    
                        }
                        else{
                            common[i] = favouriteArray.get(count2[j]);                        
                        }                                             
                    }                    
                }
            }
        }
        
        mergeSort(common, counter1, "res");
        Long[] ids = new Long[counter1];
        for (int i=0;i<counter1;i++){
            ids[i] = common[i].getRestaurantID();
        }
        return ids;
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
        //Favourite[] uncommon = new Favourite[favouriteArray.size()];
        Favourite[] uncommon = new Favourite[favouriteArray.size()];
        //Favourite[] uncommon2 = new Faidsvourite[favouriteArray.size()];
        //Favourite[] uncommon3 = new Faidsvourite[favouriteArray.size()];
        MyArrayList<Favourite> custArray = new MyArrayList<>();
        MyArrayList<Favourite> cust2Array = new MyArrayList<>();
        
        // first find out if the customer exists - ie the ID matches to a customer
        if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                if (favouriteArray.get(i).getCustomerID().equals(customer1ID)){ // find if a customer exists - can be multiple                                                           
                    custArray.add(favouriteArray.get(i));
                    //uncommon[counter1++] = favouriteArray.get(i);
                }            
                if (favouriteArray.get(i).getCustomerID().equals(customer2ID)){ // find if a customer exists                                        
                    cust2Array.add(favouriteArray.get(i));
                    //uncommon2[counter2++] = favouriteArray.get(i);
                }                            
            }
        }
        else{
            return new Long[0];    
        }
          
        
        int count = 0;
        int countl = 1;
        boolean finished = false;
        Long record = 0L;
        Favourite temp;
        for (int i=0;i<custArray.size();i++){            
            for (int j=0;j<cust2Array.size();j++){
                if (custArray.get(i).getRestaurantID().equals(cust2Array.get(j).getRestaurantID())){
                    //System.out.println("Breaking time " + custArray.get(i).getRestaurantID() + " "+ cust2Array.get(j).getRestaurantID() + " " +j);
                    //System.out.println("BREAK " + i + " " + j);
                    record = custArray.get(i).getRestaurantID();
                    break;
                }
                else{
                    if (cust2Array.get(j).getRestaurantID().equals(record) == false && record != 0){
                        //System.out.println("Add" + cust2Array.get(j).getRestaurantID());                    
                        //System.out.println("Adding time " + custArray.get(i).getRestaurantID() + " "+ i + " "+ cust2Array.get(j).getRestaurantID() + " " +j);
                        uncommon[count++] = cust2Array.get(j);
                    }                                                           
                }
            }
        }
        
                   
        //System.out.println(uncommon);
        mergeSort(uncommon, count, "res");
        Long[] ids = new Long[count];
        for (int i=0;i<count;i++){
            ids[i] = uncommon[i].getRestaurantID();
        }
        return ids;
        
    }

    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        boolean isID = false;
        
        //Long[] uncommon = new Long[favouriteArray.size()];
        int counter1 = 0;
        int counter2 = 0;
       
        Long resID1;
        Long resID2;
        count = 0;
        Favourite[] count1 = new Favourite[favouriteArray.size()];
        Favourite[] count2 = new Favourite[favouriteArray.size()];
        Favourite[] uncommon = new Favourite[favouriteArray.size()];
        MyArrayList<Favourite> custArray = new MyArrayList<>();
        MyArrayList<Favourite> cust2Array = new MyArrayList<>();
        // first find out if the customer exists - ie the ID matches to a customer
        if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                if (favouriteArray.get(i).getCustomerID().equals(customer1ID)){ // find if a customer exists - can be multiple                                                           
                    custArray.add(favouriteArray.get(i));
                    count1[counter1] = favouriteArray.get(i);
                    counter1++;                    
                }            
                if (favouriteArray.get(i).getCustomerID().equals(customer2ID)){ // find if a customer exists                                        
                    cust2Array.add(favouriteArray.get(i));
                    count2[counter2] = favouriteArray.get(i);
                    counter2++;                    
                }            
            }
        }
        else{
            return new Long[0];    
        }

        mergeSort(count1, counter1, "res2");
        mergeSort(count2, counter2, "res2");
        // for (int i = 0; i < counter1;i++){
        //     System.out.println(count1[i]);
        // }
        // for (int i = 0; i < counter2;i++){
        //     System.out.println(count2[i]);
        // }                

        int i = 0, j = 0, k = 0;
        while (i < counter1 && j < counter2) {            
            // If not common, print smaller
            //System.out.print(count1[i].getRestaurantID().compareTo(count2[j].getRestaurantID()));
            if (count1[i].getRestaurantID().compareTo(count2[j].getRestaurantID()) < 0) {                
                uncommon[count++] = count1[i];//.getRestaurantID();
                i++;
                k++;
                System.out.println("i is now " + i);
            }
            else if (count2[j].getRestaurantID().compareTo(count1[i].getRestaurantID()) < 0) {                
                uncommon[count++] = count2[j];////.getRestaurantID();
                k++;
                j++;
                System.out.println("j is now " + j);
            }
 
            // Skip common element
            else {                
                i++;
                j++;
                System.out.println("i is now " + i + "j is now " + j);
            }
        }
        
        
        // printing remaining elements
        while (i < counter1) {                        
            uncommon[count++] = count1[i];
            i++;
            k++;
        }
        while (j < counter2) {                        
            uncommon[count++] = count2[j];
            j++;
            k++;
        }
          
        mergeSort(uncommon, count, "res");
        Long[] ids = new Long[count];
        for (int x=0;x<count;x++){
           ids[x] = uncommon[x].getRestaurantID();
        }
        return ids;        
    }

    public Long[] getTopCustomersByFavouriteCount() {
        Favourite[] top = new Favourite[favouriteArray.size()];
        int counter = 0;
        if (favouriteArray.size() < 20){
            return null;
        }
        else if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                top[counter++] = favouriteArray.get(i);                
            }
        }
        else{
            return new Long[20];    
        }
        mergeSort(top, counter, "cTop");
        Long[] ids = new Long[20];
        for (int x=0;x<20;x++){
           ids[x] = top[x].getCustomerID();
        }
        return ids; 
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        Favourite[] top = new Favourite[favouriteArray.size()];
        int counter = 0;
        if (favouriteArray.size() < 20){
            return null;
        }
        else if (!favouriteArray.isEmpty()){
            for (int i=0; i<favouriteArray.size();i++){
                top[counter++] = favouriteArray.get(i);                
            }
        }
        else{
            return new Long[20];    
        }
        mergeSort(top, counter, "rTop");
        Long[] ids = new Long[20];
        for (int x=0;x<20;x++){
           ids[x] = top[x].getRestaurantID();
        }
        return ids; 
    }
}
