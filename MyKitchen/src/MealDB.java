import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.management.RuntimeErrorException;

public class MealDB extends API{
    //attributes
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    //constructor
    public MealDB(){}
    
    //query API by recipe name
    public Recipe queryByName(String name)
    {
        try{
            String queryURL = BASE_URL + "search.php?s="+ name;
            URL url = new URL(queryURL);
            HttpURLConnection url_connection = (HttpURLConnection) url.openConnection();
            url_connection.setRequestMethod("GET");

            url_connection.connect();

            int response_code = url_connection.getResponseCode();

            if(response_code != 200)
            {
                throw new RuntimeException("HttpResponseCode: " + response_code);
            }
            else{
                System.out.println("Connection successful with response code: " + response_code);
            }
            return new Recipe();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new Recipe();
    }

    //query API by main ingredient
    public Recipe queryByIngredient(String mainIngredient)
    {
        return new Recipe();
    }

    //query a random recipe
    public Recipe queryRandom()
    {
        return new Recipe();
    }

    //Main
    public static void main(String[] args)
    {
        MealDB myConnection = new MealDB();
        myConnection.queryByName("Spaghetti");
    }
}