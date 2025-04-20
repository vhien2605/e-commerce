package single.project.e_commerce.repositories.specifications;

import lombok.Getter;
import single.project.e_commerce.utils.enums.SearchOperation;

import java.util.Objects;

@Getter
public class SpecSearchCriteria {
    private final String key;
    private final SearchOperation operation;
    private final Object value;
    private final Boolean orPredicate;


    //spec search with prefix, suffix
    public SpecSearchCriteria(String orPredicate, String key, SearchOperation searchOperation, Object value, String prefix, String suffix) {
        if (Objects.equals(searchOperation, SearchOperation.EQUALITY)) {
            boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
            boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
            if (startWithAsterisk && endWithAsterisk) {
                searchOperation = SearchOperation.CONTAINS;
            } else if (startWithAsterisk) {
                searchOperation = SearchOperation.ENDS_WITH;
            } else if (endWithAsterisk) {
                searchOperation = SearchOperation.STARTS_WITH;
            }
        }
        this.key = key;
        this.operation = searchOperation;
        this.value = value;
        this.orPredicate = orPredicate != null && orPredicate.equals(SearchOperation.OR_PREDICATE_FLAG);
    }
}
