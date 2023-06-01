package org.slamperboom.model.processing;

public class ParameterForPredefinedCondition {
    private final String name;
    private final String[] variants;//if empty -- write whatever you want

    public ParameterForPredefinedCondition(String name, String... variants) {
        this.name = name;
        this.variants = variants;
    }

    public String getName() {
        return name;
    }

    public String[] getVariants() {
        return variants;
    }
}
