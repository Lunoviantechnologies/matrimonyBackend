package com.example.util;

import java.util.ArrayList;
import java.util.List;

import com.example.matrimony.entity.Nakshatra;

public class NakshatraMasterData {

    public static List<Nakshatra> getAll() {

        List<Nakshatra> list = new ArrayList<>();

        list.add(build(1,"Ashwini","Deva","Aadi","Horse","Aries","Ketu"));
        list.add(build(2,"Bharani","Manushya","Madhya","Elephant","Aries","Venus"));
        list.add(build(3,"Krittika","Rakshasa","Antya","Sheep","Taurus","Sun"));
        list.add(build(4,"Rohini","Manushya","Aadi","Serpent","Taurus","Moon"));
        list.add(build(5,"Mrigashirsha","Deva","Madhya","Deer","Gemini","Mars"));
        list.add(build(6,"Ardra","Manushya","Antya","Dog","Gemini","Rahu"));
        list.add(build(7,"Punarvasu","Deva","Aadi","Cat","Cancer","Jupiter"));
        list.add(build(8,"Pushya","Deva","Madhya","Sheep","Cancer","Saturn"));
        list.add(build(9,"Ashlesha","Rakshasa","Antya","Cat","Cancer","Mercury"));
        // … add till 27
        return list;
    }

    private static Nakshatra build(int seq, String name, String gana,
                                   String nadi, String yoni,
                                   String rashi, String lord) {
        Nakshatra n = new Nakshatra();
        n.setSequence(seq);
        n.setName(name);
        n.setGana(gana);
        n.setNadi(nadi);
        n.setYoni(yoni);
        n.setRashi(rashi);
        n.setLord(lord);
        return n;
    }
}
