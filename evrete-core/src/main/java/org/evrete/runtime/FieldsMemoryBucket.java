package org.evrete.runtime;

import org.evrete.api.*;
import org.evrete.runtime.evaluation.AlphaBucketMeta;

class FieldsMemoryBucket implements Memory {
    private final SharedBetaFactStorage fieldData;
    private final AlphaBucketMeta alphaMask;

    FieldsMemoryBucket(SessionMemory runtime, FieldsKey typeFields, AlphaBucketMeta alphaMask) {
        this.alphaMask = alphaMask;
        this.fieldData = runtime.newSharedKeyStorage(typeFields);
    }

    public void clear() {
        fieldData.clear();
    }

    SharedBetaFactStorage getFieldData() {
        return fieldData;
    }

    @Override
    public void commitChanges() {
        fieldData.commitChanges();
    }

    void insert(RuntimeFact fact) {
        if (alphaMask.test(fact)) {
            fieldData.insert(fact);
        }
    }

    void delete(ReIterable<? extends RuntimeFact> facts) {
        ReIterator<? extends RuntimeFact> it = facts.iterator();

        while (it.hasNext()) {
            delete(it.next());
        }
    }

    void delete(RuntimeFact fact) {
        if (alphaMask.test(fact)) {
            fieldData.delete(fact);
        }
    }

    @Override
    public String toString() {
        return fieldData.toString();
    }
}