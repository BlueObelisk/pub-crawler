package wwmm.pubcrawler.tasks.repository;

import com.google.inject.Injector;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class GuiceTaskSpecificationFactory implements TaskSpecificationFactory {

    private final Injector injector;

    @Inject
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
