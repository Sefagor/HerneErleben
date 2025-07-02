package de.fh.dortmund.eventApp.repo;

import de.fh.dortmund.eventApp.entity.Category;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends ListCrudRepository<Category, Long> {
}
