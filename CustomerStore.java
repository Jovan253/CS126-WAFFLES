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

    public CustomerStore() {
        // Initialise variables here
        customerArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        Long[] blacklistArray = new Long[5];
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

    public boolean addBlackList(Long id){
        blacklistArray[0] = "1234567890123456";
        blacklistArray[1] = "1234567890123457";    
    }

    public boolean addCustomer(Customer customer) {
        if (dataChecker.isValid(customer) == false){
            return false;
        }
        for (int i=0;i<customerArray.size();i++){
            if (customerArray.get(i).getID() == customer.getID() || customerArray.get(i).getID() == blacklistArray[i]){
                customerArray.remove(customer);
                return false;
            }            
        }
        customerArray.add(customer);
    }

    public boolean addCustomer(Customer[] customers) {
        // TODO
        for (int i=0; i<customers.length;i++){
            if (dataChecker.isValid(customers[i]) == false || customerArray.get(i).getID == customers[i] || customers[i].getID() == blacklistArray[i]){
                customerArray.remove(customers[i]);
                return false;
            }
        }
        customerArray.add(customers[i]);
    }

    public Customer getCustomer(Long id) {
        // TODO
        for (int i=0; i<customerArray.size();i++){
            if (customerArray.get(i).getID() == id){
                return customerArray[i];        
            }
        }
        return null;
    }

    public MyArrayList<Customer> mergeSort(MyArrayList<Customer> array, boolean isID) { // seperate the array into subsets size 1
        MyArrayList<Customer> left = new MyArrayList<Customer>();
        MyArrayList<Customer> right = new MyArrayList<Customer>();
        int center;
     
        if (array.size() == 1) {  // subset is size 1  
            return array;
        }
        else {
            center = array.size()/2;
            // copy the left half of whole into the left.
            for (int i=0; i<center; i++) {
                    left.add(array.get(i));
            }
     
            //copy the right half of whole into the new arraylist.
            for (int i=center; i<array.size(); i++) {
                    right.add(array.get(i));
            }
     
            // Sort the left and right halves of the arraylist.
            left  = mergeSort(left);
            right = mergeSort(right);
     
            // Merge the results back together.
            merge(left, right, array, isID);
        }
        return array;
    }

    // for first & last name, pass in variable to know that this is that and use comparetyo

    private void merge(MyArrayList<Customer> left, MyArrayList<Customer> right, MyArrayList<Customer> array, boolean isID) { // merge the subsets
        int leftIndex = 0;
        int rightIndex = 0;
        int arrayIndex = 0;
     
        // As long as neither the left nor the right ArrayList has
        // been used up, keep taking the smaller of left.get(leftIndex)
        // or right.get(rightIndex) and adding it at both.get(bothIndex).

        // if it is an ID we compare then compare ID's
        if (isID == true){
            while (leftIndex < left.size() && rightIndex < right.size()) {
                if (left.get(leftIndex).getID()< right.get(rightIndex).getID()) {
                    array.set(arrayIndex, left.get(leftIndex));
                    leftIndex++;
                } 
                else {
                    array.set(arrayIndex, right.get(rightIndex));
                    rightIndex++;
                }
                arrayIndex++;
            }
        }
        // otherwise we compare last name, first name and then ID's
        else {
            while (leftIndex < left.size() && rightIndex < right.size()) {
                if ((left.get(leftIndex).getLastName().compareTo(right.get(rightIndex).getLastName())) < 0) {
                    array.set(arrayIndex, left.get(leftIndex));
                    leftIndex++;
                } 
                else if ((left.get(leftIndex).getLastName().compareTo(right.get(rightIndex).getLastName())) > 0){
                    array.set(arrayIndex, right.get(rightIndex));
                    rightIndex++;
                }
                else{ // if last name equivalent then compare first names             
                    if ((left.get(leftIndex).getFirstName().compareTo(right.get(rightIndex).getFirstName())) < 0){
                        array.set(arrayIndex, left.get(leftIndex));
                        leftIndex++;
                    }
                    else if ((left.get(leftIndex).getFirstName().compareTo(right.get(rightIndex).getFirstName())) > 0){
                        array.set(arrayIndex, right.get(rightIndex));
                        rightIndex++;
                    }
                    else{ // if first names equivalent then compare IDs
                        if (left.get(leftIndex).getID()< right.get(rightIndex).getID()) {
                            array.set(arrayIndex, left.get(leftIndex));
                            leftIndex++;
                        } 
                        else {
                            array.set(arrayIndex, right.get(rightIndex));
                            rightIndex++;
                        }
                    }

                }
                arrayIndex++;
            }
        }
     
        MyArrayList<Customer> rest;
        int restIndex;
        if (leftIndex >= left.size()) {
            // The left ArrayList has been use up...
            rest = right;
            restIndex = rightIndex;
        } else {
            // The right ArrayList has been used up...
            rest = left;
            restIndex = leftIndex;
        }
     
        // Copy the rest of whichever ArrayList (left or right) was not used up.
        for (int i=restIndex; i<rest.size(); i++) {
            array.set(arrayIndex, rest.get(i).getID());
            arrayIndex++;
        }
    }

    public static void mergeSortArray(Customer[] a, int n, boolean isID) {
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
        mergeSort(l, mid);
        mergeSort(r, n - mid);
    
        merge(a, l, r, mid, n - mid, isID);
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
                if (l[i].getLastName() < r[j].getLastName()) {
                    a[k++] = l[i++];
                }
                else if (l[i].getLastName() > r[j].getLastName()){
                    a[k++] = r[j++];
                }
                else{
                    if (l[i].getFirstName() < r[j].getFirstName()) {
                        a[k++] = l[i++];
                    }
                    else if (l[i].getFirstName() > r[j].getFirstName()){
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
        return mergeSort(customerArray, isID);
    }

    public Customer[] getCustomers(Customer[] customers) { 
        boolean isID = true;          
        return mergeSortArray(customers);               
    }

    public Customer[] getCustomersByName() {
        boolean isID = false;          
        return mergeSort(customerArray);        
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        boolean isID = false;          
        return mergeSortArray(customers);
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        // TODO
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);
        return new Customer[0];
    }

}
