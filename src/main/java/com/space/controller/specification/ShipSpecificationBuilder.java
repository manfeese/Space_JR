package com.space.controller.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShipSpecificationBuilder {

    private final List<SearchCriteria> params;

    public ShipSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public ShipSpecificationBuilder with(String key, String operation, Object value, boolean orPredicate) {
        if (value == null) {
            return this;
        }
        params.add(new SearchCriteria(key, operation, value, orPredicate));
        return this;
    }

    public ShipSpecificationBuilder and(String key, String operation, Object value) {
        return with(key, operation, value, false);
    }

    public ShipSpecificationBuilder or(String key, String operation, Object value) {
        return with(key, operation, value, true);
    }

    public Specification<Ship> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(ShipSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i)
                    .isOrPredicate()
                        ? Specification.where(result).or(specs.get(i))
                        : Specification.where(result).and(specs.get(i));
        }

        return result;
    }

}
