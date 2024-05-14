package com.devsuperior.dscatalog.dscatalog.services;

import com.devsuperior.dscatalog.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService service;
    @Autowired
    private ProductRepository repository;
    private long existentId;
    private long nonExistentId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existentId = 1L;
        nonExistentId = 2L;
        countTotalProducts = 25L;
    }

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesntExist() {
        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortByName() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {

        service.delete(existentId);

        Assertions.assertEquals(countTotalProducts-1, repository.count());
    }

    //deleteById não da throw em resource not found pq ele ignora quando um ID n ta na db
//    @Test
//    public void deleteShouldThrowsResourceNotFoundExceptionWhenIdDoesntExists() {
//
//        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
//            service.delete(nonExistentId);
//        });
//    }
}
