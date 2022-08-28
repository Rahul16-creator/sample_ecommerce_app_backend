package com.shopping_app.shoppingApp.service;

import com.shopping_app.shoppingApp.Exceptions.NotFoundException;
import com.shopping_app.shoppingApp.domain.Product;
import com.shopping_app.shoppingApp.mapping.ProductMapper;
import com.shopping_app.shoppingApp.mapping.ProductMapperImpl;
import com.shopping_app.shoppingApp.model.Product.Request.ProductRequest;
import com.shopping_app.shoppingApp.model.Product.Response.ProductResponse;
import com.shopping_app.shoppingApp.payload.MockPayload;
import com.shopping_app.shoppingApp.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpServerErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.shopping_app.shoppingApp.payload.MockPayload.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest()
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private ProductMapper productMapperImpl;

    @Before
    public void setUp() {
        productMapperImpl = new ProductMapperImpl();
    }

    @Test
    public void testAddProductSuccess() {
        ProductRequest request = MockPayload.getProductRequestMockPayload();
        Product product = productMapperImpl.convertToProductDomain(request);
        when(productMapper.convertToProductDomain(any(ProductRequest.class))).thenReturn(product);
        when(productMapper.convertToProductResponse(any(Product.class))).thenReturn(MockPayload.getProductResponseMockPayload());
        ProductResponse response = productService.addProduct(request);
        assertNotNull(response.getId());
        assertEquals(request.getProductName(), response.getProductName());
    }

    @Test
    public void testAddProductFailure() {
        ProductRequest request = MockPayload.getProductRequestMockPayload();
        request.setProductName(null);
        when(productService.addProduct(any())).thenThrow(HttpServerErrorException.InternalServerError.class);
        assertThrows(HttpServerErrorException.InternalServerError.class, () -> productService.addProduct(request));
    }

    @Test
    public void testGetAllProductsSuccess() {
        when(productRepository.findAll()).thenReturn(getAllProductsMockData());
        List<ProductResponse> response = productService.getAllProducts();
        assertEquals(5, response.size());
    }

    @Test
    public void testGetAllProductsEmpty() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());
        List<ProductResponse> response = productService.getAllProducts();
        assertEquals(0, response.size());
    }

    @Test
    public void testGetProductByIdSuccess() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getProductMockPayload()));
        when(productMapper.convertToProductResponse(any(Product.class))).thenReturn(MockPayload.getProductResponseMockPayload());
        ProductResponse response = productService.getProductById(1L);
        assertNotNull(response);
        assertEquals(getProductMockPayload().getProductName(), response.getProductName());
    }

    @Test
    public void testGetProductByIdException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    public void testDeleteProductByIdSuccess() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getProductMockPayload()));
        when(productMapper.convertToProductResponse(any(Product.class))).thenReturn(MockPayload.getProductResponseMockPayload());
        ProductResponse response = productService.deleteProductById(1L);
        assertEquals(getProductMockPayload().getProductName(), response.getProductName());
    }

    @Test
    public void testDeleteProductByIdException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.deleteProductById(1L));
    }

    @Test
    public void testUpdateProductSuccess() {
        doReturn(getProductResponseMockPayload()).when(productMapper).convertToProductResponse(any());
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(getProductMockPayload()));
        when(productRepository.save(any())).thenReturn(getProductMockPayload());
        ProductResponse response = productService.updateProductById(getProductRequestMockPayload(), 1L);
        assertNotNull(response.getId());
    }

    @Test
    public void testUpdateProductFailure() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.updateProductById(getProductRequestMockPayload(), 100L));
    }
}