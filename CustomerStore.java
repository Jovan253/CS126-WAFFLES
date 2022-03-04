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
    private Long[] blacklistArray = new Long[5];   
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
            else{
                customerArray.add(customer); 
            }
        }
        for (int j=0;j<blacklistArray.length;j++){
            if (customer.getID() == blacklistArray[j]){ // only add customer if not a duplicate and valid and blacklists
                //customerArray.remove(customer);
                return false;          
            } 
        }                           
        return true;
    }

    public boolean addCustomer(Customer[] customers) {
        if (customers.length == 0){
            return false;
        }
// doouble check with chingiz
        for (int i=0; i<customers.length;i++){
            addCustomer(customers[i]);
        }
        return true;                
    }

    public Customer getCustomer(Long id) {     
        if (customerArray.isEmpty()){
            return null;
        }
        for (int i=0; i<customerArray.size();i++){
            if (customerArray.get(i).getID().equals(id)){
                return customerArray.get(i);        
            }            
        }
        return null;
    }

    public Customer[] mergeSortArray(Customer[] a, int n, boolean isID) {
        if (n < 2) {
            return a;
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
        mergeSortArray(l, mid, isID);
        mergeSortArray(r, n - mid, isID);
    
        mergeArray(a, l, r, mid, n - mid, isID);
        return null;
    }

    public static void mergeArray(Customer[] a, Customer[] l, Customer[] r, int left, int right, boolean isID) {
 
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
        Customer[] array = new Customer[256]; 
        if (customerArray.isEmpty()){
            return null;
        } 
        //customerArray = mergeSort(customerArray, isID);
        for (int i=0;i<customerArray.size();i++){ // potentially implement toArray
            array[i] = customerArray.get(i);          
        }
        
        if (array.length == 0){
            return null;
        }
        array = mergeSortArray(array, array.length, isID);
        return array;
    }

    public Customer[] getCustomers(Customer[] customers) { 
        if (customers.length == 0){
            return null;
        }
        boolean isID = true;          
        return mergeSortArray(customers, customers.length, isID);               
    }

    public Customer[] getCustomersByName() {
        boolean isID = false;          
        Customer[] array = new Customer[256]; 
        if (customerArray.isEmpty()){
            return null;
        }
        //customerArray = mergeSort(customerArray, isID);
        for (int i=0;i<customerArray.size();i++){ // potentially implement toArray
            array[i] = customerArray.get(i);
        }
        array = mergeSortArray(array, array.length, isID);
        return array;        
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        if (customers.length == 0){
            return null;
        }
        boolean isID = false;          
        return mergeSortArray(customers, customers.length, isID);
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        boolean isID = false;
        Customer[] same = new Customer[customerArray.size()];
        MyArrayList<Customer> sameArray = new MyArrayList<>();
        for (int i=0;i<customerArray.size();i++){
            if (customerArray.get(i).getFirstName().contains(searchTerm)){
                sameArray.add(customerArray.get(i));
            }
            if (customerArray.get(i).getLastName().contains(searchTerm)){
                sameArray.add(customerArray.get(i));
            }
        }
        // TODO
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);

        for (int j=0;j<sameArray.size();j++){ // potentially implement toArray
            same[j] = sameArray.get(j);
        }
        same = mergeSortArray(same, same.length, isID)
        return same;
    }

}
