package org.evrete.spi.minimal;

import org.evrete.api.*;
import org.evrete.runtime.FieldsKey;

import java.util.function.BiPredicate;

class DefaultMemoryFactory implements MemoryFactory {
    private final RuntimeContext<?> ctx;

    DefaultMemoryFactory(RuntimeContext<?> ctx) {
        this.ctx = ctx;

    }

    private static KeysStore factory(int[] arraySizes, int level) {
        int depth = arraySizes.length;
        int arrSize = arraySizes[level];
        assert arrSize > 0;
        if (level == depth - 1) {
            return new KeysStorePlain(level, arrSize);
        } else {
            return new KeysStoreMap(level, arrSize, () -> factory(arraySizes, level + 1));
        }
    }

/*
    @Override
    public FactStorage newFactStorage(Type<?> type, BiPredicate<Object, Object> identityFunction) {
        return new DefaultFactStorage(ctx, type, identityFunction);
    }
*/

/*
    @Override
    public <Z, Fa> FactStorage<Z> newFactStorage(Type<?> type, Class<Z> storageClass, BiPredicate<Z, Z> identityFunction) {
        return new DefaultFactStorage(ctx, type, identityFunction);
    }
*/

    @Override
    public <Z> FactStorage<Z> newFactStorage(Type<?> type, Class<Z> storageClass, BiPredicate<Z, Z> identityFunction) {
        return new DefaultFactStorage<>(ctx, type, identityFunction);
    }

    @Override
    public SharedBetaFactStorage newBetaStorage(FieldsKey typeFields) {
        return new SharedBetaData(typeFields);
    }

    @Override
    public KeysStore newKeyStore(int[] factTypeCounts) {
        return factory(factTypeCounts, 0);
    }

    @Override
    public SharedPlainFactStorage newPlainStorage() {
        return new SharedAlphaData();
    }
}