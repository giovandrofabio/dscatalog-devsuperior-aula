package com.devsuperior.dscatalog.dscatalog.dto;

import com.devsuperior.dscatalog.dscatalog.entities.Category;

import java.io.Serializable;
import java.util.Objects;

public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryDTO that = (CategoryDTO) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
