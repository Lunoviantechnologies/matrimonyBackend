package com.example.util;

import java.util.ArrayList;
import java.util.List;

import com.example.matrimony.entity.PlanFeature;
import com.example.matrimony.entity.SubscriptionPlan;

public class PlanFeatureMasterData {

    public static List<PlanFeature> getAll(SubscriptionPlan p1,
                                          SubscriptionPlan p2,
                                          SubscriptionPlan p3,
                                          SubscriptionPlan p4,
                                          SubscriptionPlan p5) {

        List<PlanFeature> list = new ArrayList<>();

        list.add(build(p1, 30, true, "No", false, "Full profile view, standard visibility"));
        list.add(build(p2, 60, true, "No", false, "Advanced search, higher visibility"));
        list.add(build(p3, 80, true, "Basic", false, "Profile boost, priority listing"));
        list.add(build(p4, 150, true, "Assisted", true, "Manual profile review"));
        list.add(build(p5, 300, true, "Advanced", true, "Senior RM, top visibility"));

        return list;
    }

    private static PlanFeature build(SubscriptionPlan plan,
                                    Integer contacts,
                                    Boolean chat,
                                    String astroSupport,
                                    Boolean relationshipManager,
                                    String benefit) {

        PlanFeature feature = new PlanFeature();
        feature.setPlan(plan);
        feature.setContacts(contacts);
        feature.setChat(chat);
        feature.setAstroSupport(astroSupport);
        feature.setRelationshipManager(relationshipManager);
        feature.setBenefit(benefit);

        return feature;
    }
}
