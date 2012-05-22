package wwmm.pubcrawler.repositories.mongo;

import com.google.inject.Injector;
import wwmm.pubcrawler.tasks.TaskSpecification;

/**
 * @author Sam Adams
 */
public class GuiceTaskSpecificationFactory implements TaskSpecificationFactory {

    private final Injector injector;

    public GuiceTaskSpecificationFactory(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public <T> TaskSpecification<T> getTaskSpecification(final String typeName) {
        final Class<? extends TaskSpecification<T>> type = getType(typeName);
        return injector.getInstance(type);
    }

    @SuppressWarnings("unchecked")
    private <T> Class<? extends TaskSpecification<T>> getType(final String typeName) {
        try {
            return (Class<? extends TaskSpecification<T>>) Class.forName(typeName);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("cannot create task specification '" + typeName + "'", e);
        }
    }

}
