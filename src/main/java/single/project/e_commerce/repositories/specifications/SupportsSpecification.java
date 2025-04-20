package single.project.e_commerce.repositories.specifications;


/**
 * this interface is abstract for entity which support specification filter in project
 */
public interface SupportsSpecification {
    default String description() {
        return "Can create JPA specifications from this entity";
    }
}
