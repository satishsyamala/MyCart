package com.cart.invoice;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.net.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:/file.json"));

        String region = bufferedReader.readLine();

        String keyword = bufferedReader.readLine();

        List<String> result = findCountry(region, keyword);

        bufferedWriter.write(
            result.stream()
                .collect(joining("\n"))
            + "\n"
        );

        bufferedReader.close();
     
        
        bufferedWriter.close();
    }
    
    public static List<String> findCountry(String region, String keyword) {
        HttpURLConnection connection = null;
        try{
            String ur="https://jsonmock.hackerrank.com/api/countries/search?region="+region+"&name="+keyword+"&page=1";
            URL url=new URL(ur);
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
           
           InputStream in=connection.getInputStream();
           BufferedReader br=new BufferedReader(new InputStreamReader(in));
           StringBuilder sb=new StringBuilder();
           String line;
           while((line=br.readLine())!=null)
           {
               sb.append(line);
               
           }
           br.close();
           System.out.println(sb);
           JSONParser p=new JSONParser();
           JSONObject o=(JSONObject)p.parse(sb.toString());
           JSONArray data=(JSONArray)o.get("data");
           List<Object[]> dc=new ArrayList<>();
           
           for(int i=0;i<data.size();i++)
           {
               JSONObject d=(JSONObject)data.get(i);
               Object[] o1=new Object[2];
               o1[0]=d.get("name").toString();
               o1[1]=Integer.parseInt(d.get("population").toString());
               dc.add(o1);
           }
           Collections.sort(dc,new Comparator<Object[]>() {
        	   public int compare(Object[] o,Object[] o1)
        	   {
               if((Integer)o[1]!=(Integer)o1[1])
                 return o[0].toString().compareTo(o1[0].toString());
               else
            	   return  (Integer)o[1]-(Integer)o1[1];
        	   }
           });
           dc.forEach(c->System.out.println(c[0]+","+c[1]));
           return dc.stream().map(c->c[0]+","+c[1]).collect(Collectors.toList());
          
            
       
           
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(connection!=null)
                 connection.disconnect();
        }
        return null;
  }


}
