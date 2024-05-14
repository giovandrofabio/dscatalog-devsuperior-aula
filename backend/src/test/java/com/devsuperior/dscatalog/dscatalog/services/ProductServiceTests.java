package com.devsuperior.dscatalog.dscatalog.services;

import com.devsuperior.dscatalog.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.dscatalog.entities.Category;
import com.devsuperior.dscatalog.dscatalog.entities.Product;
import com.devsuperior.dscatalog.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.dscatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;
    @Mock
    private CategoryRepository categoryRepository;
    private long existentId;
    private long nonExistentId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {
        existentId = 1L;
        nonExistentId = 2L;
        dependentId = 3L;

        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        category = Factory.createCategory();
        productDTO = Factory.createProductDTO();

        //findAll pageable
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        //insert
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        //findById
        Mockito.when(repository.findById(existentId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        //update
        Mockito.when(repository.getReferenceById(existentId)).thenReturn(product);
        Mockito.when(repository.getReferenceById(nonExistentId)).thenThrow(EntityNotFoundException.class);
        Mockito.when(categoryRepository.getReferenceById(existentId)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(nonExistentId)).thenThrow(EntityNotFoundException.class);

        //delete
        Mockito.doNothing().when(repository).deleteById(existentId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistentId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void deleteByIdShouldDoNothingWhenIdExists() {


        Assertions.assertDoesNotThrow(() -> {
            service.delete(existentId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(existentId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesntExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistentId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistentId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdisDependent() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    public void findAllPagedShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {

        ProductDTO result = service.findById(existentId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowsResourceNotFoundExceptionWhenIdDoesntExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistentId);
        });
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {

        ProductDTO result = service.update(existentId, productDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowsResourceNotFoundExceptionWhenIdDoesntExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistentId, productDTO);
        });
    }
}
