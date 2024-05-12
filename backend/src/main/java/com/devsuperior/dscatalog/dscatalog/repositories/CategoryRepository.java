package com.devsuperior.dscatalog.dscatalog.repositories;

import com.devsuperior.dscatalog.dscatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
