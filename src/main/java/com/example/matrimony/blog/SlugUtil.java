package com.example.matrimony.blog;

public class SlugUtil {

    public static String toSlug(String title){
        return title.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9]", "-")
                .replaceAll("-+","-");
    }
}
