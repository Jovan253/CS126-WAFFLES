package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class CustomerStore implements ICustomerStore {

    private MyArrayList<Customer> customerArray;
    private DataChecker dataChecker;
    Long[] blacklistArray = new Long[5];
    private int count = 0;

    public CustomerStore() {
        // Initialise variables here
        customerArray = new MyArrayList<>();
        dataChecker = new DataChecker();        
    }

    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerArray = new Customer[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line=lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Customer[] loadedCustomers = new Customer[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int customerCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Customer customer = (new Customer(
                            Long.parseLong(data[0]),
                            data[1],
                            data[2],
                            formatter.parse(data[3]),
                            Float.parseFloat(data[4]),
                            Float.parseFloat(data[5])));

                    loadedCustomers[customerCount++] = customer;
                }
            }
            csvReader.close();

            customerArray = loadedCustomers;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return customerArray;
    }

    public void addBlackList(Long id){
        blacklistArray[count] = 1112223334445556L;
        count++;      
    }

    public boolean addCustomer(Customer customer) {
        if (dataChecker.isValid(customer) == false){
            return false;
        }
        for (int i=0;i<customerArray.size();i++){
            if (customerArray.get(i).getID() == customer.getID()){                                
                customerArray.remove(customer);
                blacklistArray[count] = customer.getID();
                count ++;
                return false;
            }
            
        }
        for (int j=0;j<blacklistArray.length;j++){
            if (customer.getID() == blacklistArray[j]){     
                //customerArray.remove(customer);
                return false;          
            } 
        }  
        customerArray.add(customer);                                 
        return true;
    }

    public boolean addCustomer(Customer[] customers) {
        if (customers.length == 0){
            return false;
        }
        for (int i=0; i<customers.length;i++){
            addCustomer(customers[i]);
        }
        return true; 
    }

    public Customer getCustomer(Long id) {
        if (customerArray.isEmpty() == false){
            for (int i=0; i<customerArray.size();i++){
                if (customerArray.get(i).getID().equals(id)){
                    return customerArray.get(i);        
                }            
            }               
        }       
        return null; 

    }

    public static void mergeSort(Customer[] a, int n, boolean isID) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        Customer[] l = new Customer[mid];
        Customer[] r = new Customer[n - mid];
    
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

    public static void merge(Customer[] a, Customer[] l, Customer[] r, int left, int right, boolean isID) {
 
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
                if (l[i].getLastName().compareTo(r[j].getLastName()) < 0) {
                    a[k++] = l[i++];
                }
                else if (l[i].getLastName().compareTo(r[j].getLastName()) > 0){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getFirstName().compareTo(r[j].getFirstName()) < 0) {
                        a[k++] = l[i++];
                    }
                    else if (l[i].getFirstName().compareTo(r[j].getFirstName()) > 0){
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

    
    public Customer[] getCustomers() { // order array in terms of ID   
        boolean isID = true;                      
        Customer[] array = new Customer[customerArray.size()];   
        
        for (int i=0;i<array.length;i++){ // potentially implement toArray
            array[i] = customerArray.get(i);         
            System.out.println(customerArray.get(i).getFirstName());   
        }
                      
        mergeSort(array, array.length, isID);
        return array;
    }

    public Customer[] getCustomers(Customer[] customers) { 
        boolean isID = true;              
        
        mergeSort(customers, customers.length, isID);                    
        return customers;
    }

    public Customer[] getCustomersByName() {
        boolean isID = false;          
        Customer[] array = new Customer[customerArray.size()];          
        for (int i=0;i<array.length;i++){ 
            array[i] = customerArray.get(i);
        }
        
        mergeSort(array, array.length, isID);
        return array;        
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        boolean isID = false;          
        
        mergeSort(customers, customers.length, isID);              
        return customers;
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

    public Customer[] getCustomersContaining(String searchTerm) {
        boolean isID = false;
        boolean twoNames = false;        
        int counter = 0;
        //System.out.println(searchTerm);
        String searchTermNew;
        Customer[] same = new Customer[customerArray.size()];

        if (searchTerm.contains("-")){
            twoNames = true;
            searchTerm = searchTerm.replace("-", "");
        }

        searchTermNew = StringFormatter.convertAccents(searchTerm);
        //System.out.println(searchTermNew);
        searchTermNew = searchTermNew.toLowerCase();        
        //System.out.println(searchTermNew);

        for (int i=0;i<customerArray.size();i++){
            if(twoNames != true) {         
                //System.out.println("Add for a name " + counter);       
                if (customerArray.get(i).getFirstName().toLowerCase().contains(searchTermNew)){  
                    //System.out.println("Add for first name " + counter);                  
                    same[counter]=customerArray.get(i);
                    counter++;
                }
                else if (customerArray.get(i).getLastName().toLowerCase().contains(searchTermNew)){                    
                    //System.out.println("Add for last name " + counter);                  
                    same[counter]=customerArray.get(i);
                    counter++;
                }
            }
            else {
                String joinedWord = customerArray.get(i).getFirstName().toLowerCase() + customerArray.get(i).getLastName().toLowerCase(); // joins the names from customer array
                if (isSubstring(searchTermNew, joinedWord) != -1) { // if the search term is a subset of the joined customerArray word then we add customer to the array
                    same[counter]=customerArray.get(i);
                    counter++;
                }
            }
        }

        Customer[] array = new Customer[counter];
        for (int j=0;j<counter;j++){
            array[j] = same[j];
        }

        mergeSort(array, counter, isID);
        return array;
    }


}