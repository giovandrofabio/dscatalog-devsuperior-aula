package com.devsuperior.dscatalog.dscatalog.services;

import com.devsuperior.dscatalog.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dscatalog.entities.Category;
import com.devsuperior.dscatalog.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();

        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
    }
}
