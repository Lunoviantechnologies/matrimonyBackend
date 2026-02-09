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

	    list.add(build(10,"Magha","Rakshasa","Aadi","Rat","Leo","Ketu"));
	    list.add(build(11,"Purva Phalguni","Manushya","Madhya","Rat","Leo","Venus"));
	    list.add(build(12,"Uttara Phalguni","Manushya","Antya","Cow","Leo","Sun"));
	    list.add(build(13,"Hasta","Deva","Aadi","Buffalo","Virgo","Moon"));
	    list.add(build(14,"Chitra","Rakshasa","Madhya","Tiger","Virgo","Mars"));
	    list.add(build(15,"Swati","Deva","Antya","Buffalo","Libra","Rahu"));
	    list.add(build(16,"Vishakha","Rakshasa","Aadi","Tiger","Libra","Jupiter"));
	    list.add(build(17,"Anuradha","Deva","Madhya","Deer","Scorpio","Saturn"));
	    list.add(build(18,"Jyeshtha","Rakshasa","Antya","Deer","Scorpio","Mercury"));
	    list.add(build(19,"Mula","Rakshasa","Aadi","Dog","Sagittarius","Ketu"));
	    list.add(build(20,"Purva Ashadha","Manushya","Madhya","Monkey","Sagittarius","Venus"));
	    list.add(build(21,"Uttara Ashadha","Manushya","Antya","Mongoose","Sagittarius","Sun"));
	    list.add(build(22,"Shravana","Deva","Aadi","Monkey","Capricorn","Moon"));
	    list.add(build(23,"Dhanishta","Rakshasa","Madhya","Lion","Capricorn","Mars"));
	    list.add(build(24,"Shatabhisha","Rakshasa","Antya","Horse","Aquarius","Rahu"));
	    list.add(build(25,"Purva Bhadrapada","Manushya","Aadi","Lion","Aquarius","Jupiter"));
	    list.add(build(26,"Uttara Bhadrapada","Manushya","Madhya","Cow","Pisces","Saturn"));
	    list.add(build(27,"Revati","Deva","Antya","Elephant","Pisces","Mercury"));

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
