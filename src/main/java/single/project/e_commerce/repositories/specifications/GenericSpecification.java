package single.project.e_commerce.repositories.specifications;


import jakarta.persistence.criteria.*;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;
import single.project.e_commerce.models.Address;
import single.project.e_commerce.models.Category;
import single.project.e_commerce.utils.commons.AppConst;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Build predicate from {@link SpecSearchCriteria}
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
public class GenericSpecification<T extends SupportsSpecification> implements Specification<T> {
    private SpecSearchCriteria criteria;

    public GenericSpecification(SpecSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getKey().equalsIgnoreCase("category")) {
            Join<T, Category> join = root.join("categories", JoinType.INNER);
            return buildJoinCategoryPredicate(join, query, builder);
        } else if (criteria.getKey().equalsIgnoreCase("address")) {
            Join<T, Address> join = root.join("address", JoinType.INNER);
            return buildJoinAddressPredicate(join, query, builder);
        }
        return buildNormalPredicate(root, query, builder);
    }

    private Predicate buildNormalPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        };
    }

    private Predicate buildJoinCategoryPredicate(Join<T, ?> join, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate namePredicate = builder.like(join.get("name"), "%" + criteria.getValue().toString() + "%");
        Predicate descriptionPredicate = builder.like(join.get("description"), "%" + criteria.getValue().toString() + "%");
        return builder.or(namePredicate, descriptionPredicate);
    }

    private Predicate buildJoinAddressPredicate(Join<T, ?> join, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Expression<String> fullName = builder.concat(join.get("name"), join.get("city"));
        fullName = builder.concat(fullName, join.get("country"));
        return builder.like(builder.lower(fullName), "%" + criteria.getValue().toString().toLowerCase() + "%");
    }
}
