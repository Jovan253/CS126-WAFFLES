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
            }
        }
        for (int j=0;j<blacklistArray.length;j++){
            if (favourite.getID() == blacklistArray[j]){ // only add customer if not a duplicate and valid and blacklists
                //customerArray.remove(customer);
                return false;          
            } 
        }                           
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
        // TODO
        return null;
    }

    public Favourite[] getFavourites() {
        // TODO
        return new Favourite[0];
    }

    public Favourite[] getFavouritesByCustomerID(Long id) {
        // TODO
        return new Favourite[0];
    }

    public Favourite[] getFavouritesByRestaurantID(Long id) {
        // TODO
        return new Favourite[0];
    }

    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODO
        return new Long[0];
    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODO
        return new Long[0];
    }

    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODO
        return new Long[0];
    }

    public Long[] getTopCustomersByFavouriteCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        // TODO
        return new Long[20];
    }
}
