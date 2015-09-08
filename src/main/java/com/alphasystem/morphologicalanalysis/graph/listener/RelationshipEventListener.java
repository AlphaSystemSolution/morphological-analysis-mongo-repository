package com.alphasystem.morphologicalanalysis.graph.listener;

import com.alphasystem.morphologicalanalysis.common.model.Linkable;
import com.alphasystem.morphologicalanalysis.graph.model.Relationship;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import com.mongodb.DBObject;

import java.util.Set;

import static java.lang.String.format;

/**
 * @author sali
 */
public class RelationshipEventListener extends DocumentEventListener<Relationship> {

    @Override
    public void onBeforeSave(Relationship source, DBObject dbo) {
        super.onBeforeSave(source, dbo);
        Linkable dependent = source.getDependent();
        Linkable owner = source.getOwner();
        if (dependent == null || owner == null) {
            throw new IllegalStateException("Both \"dependent\" and \"owner\" are required to create a relationship");
        }
        System.out.println(format("Relationship id is %s and display name is %s",
                source.getId(), source.getDisplayName()));
        Set<String> keys = dbo.keySet();
        System.out.println(keys);
        Object o = dbo.get("relationship");
        System.out.println(dbo.getClass().getName() + " : " + o.getClass().getName() + " : " + o);
    }
}
