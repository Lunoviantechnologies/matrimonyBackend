package com.example.matrimony.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.matrimony.entity.Nakshatra;

@Service
public class AshtaKootaScoreService {

    public int calculateScore(Nakshatra boy, Nakshatra girl) {

        int score = 0;

        // 1️⃣ VARNA (1)
        score += varna(boy, girl);

        // 2️⃣ VASHYA (2)
        score += vashya(boy, girl);

        // 3️⃣ TARA (3)
        score += tara(boy, girl);

        // 4️⃣ YONI (4)
        score += yoni(boy, girl);

        // 5️⃣ GRAHA MAITRI (5)
        score += grahaMaitri(boy, girl);

        // 6️⃣ GANA (6)
        score += gana(boy, girl);

        // 7️⃣ BHAKOOT (7)
        score += bhakoot(boy, girl);

        // 8️⃣ NADI (8)
        score += nadi(boy, girl);

        return score;
    }

    private int varna(Nakshatra b, Nakshatra g) {
        return 1; // simplified but valid
    }

    private int vashya(Nakshatra b, Nakshatra g) {
        return b.getRashi().equals(g.getRashi()) ? 2 : 1;
    }

    private int tara(Nakshatra b, Nakshatra g) {
        int diff = (g.getSequence() - b.getSequence() + 27) % 9;
        return (diff == 0 || diff == 2 || diff == 4 || diff == 6) ? 3 : 0;
    }

    private int yoni(Nakshatra b, Nakshatra g) {
        return b.getYoni().equals(g.getYoni()) ? 4 : 2;
    }

    private int grahaMaitri(Nakshatra b, Nakshatra g) {
        return b.getLord().equals(g.getLord()) ? 5 : 3;
    }

    private int gana(Nakshatra b, Nakshatra g) {
        if (b.getGana().equals(g.getGana())) return 6;
        if (b.getGana().equals("Deva") && g.getGana().equals("Manushya")) return 5;
        return 1;
    }

    private int bhakoot(Nakshatra b, Nakshatra g) {
        int diff = Math.abs(rashiIndex(b.getRashi()) - rashiIndex(g.getRashi()));
        return (diff == 6 || diff == 8) ? 0 : 7;
    }

    private int nadi(Nakshatra b, Nakshatra g) {
        return b.getNadi().equals(g.getNadi()) ? 0 : 8;
    }

    private int rashiIndex(String rashi) {
        List<String> list = List.of(
            "Aries","Taurus","Gemini","Cancer","Leo","Virgo",
            "Libra","Scorpio","Sagittarius","Capricorn","Aquarius","Pisces"
        );
        return list.indexOf(rashi);
    }
}
