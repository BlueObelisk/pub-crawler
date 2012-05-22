package wwmm.pubcrawler.repositories.mongo;

import wwmm.pubcrawler.tasks.TaskSpecification;

/**
 * @author Sam Adams
 */
public interface TaskSpecificationFactory {

    <T> TaskSpecification<T> getTaskSpecification(final String typeName);

}
