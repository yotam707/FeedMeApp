package com.example.yotam707.feedmeapp.Utils;

public class StringUtils {
    public static String convertToCamelCase(String text) {
        String result = "", result1 = "";
        for (int i = 0; i < text.length(); i++) {
            String next = text.substring(i, i + 1);
            if (i == 0) {
                result += next.toUpperCase();
            } else {
                result += next.toLowerCase();
            }
        }
        result = result.replace('_', ' ');
        String[] splited = result.split("\\s+");
        String[] splited1 = new String[splited.length];

        for (int i = 0; i < splited.length; i++) {
            int l = splited[i].length();
            result1 = "";
            for (int j = 0; j < splited[i].length(); j++) {
                String next = splited[i].substring(j, j + 1);

                if (j == 0) {
                    result1 += next.toUpperCase();
                } else {
                    result1 += next.toLowerCase();
                }
            }
            splited1[i] = result1;
        }
        result = "";
        for (int i = 0; i < splited1.length; i++) {
            result += " " + splited1[i];
        }
        return result;
    }

    public static String changeSpaces(String val){
        val =  val.replace(' ', '+');
        return  val.replace('_', '+');
    }
    public static String removeUnderscoreAndSPaces(String val){
        return  val.replace('_', ' ').replaceAll("\\s+","");
    }
}
