package com.shopping_app.shoppingApp.controller;

import com.shopping_app.shoppingApp.model.AbstractClass.Response.ApiResponse;
import com.shopping_app.shoppingApp.model.Product.Request.ProductRequest;
import com.shopping_app.shoppingApp.payload.MockPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Map;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductControllerTest extends AbstractController {

    @Test
    public void testAddProduct() {
        addUser();
        ProductRequest request = MockPayload.getProductRequestMockPayload();
        HttpEntity<ProductRequest> entity = getEntity(request, getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/product/", HttpMethod.POST, entity, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
        request.setProductName(null);
        HttpEntity<ProductRequest> entity2 = getEntity(request, getHeader());
        ResponseEntity<ApiResponse> response2 = httpCall("/product/", HttpMethod.POST, entity2, ApiResponse.class);
        assertEquals(400, response2.getStatusCodeValue());
    }

    @Test
    public void testGetAllProduct() {
        addUser();
        ProductRequest request = MockPayload.getProductRequestMockPayload();
        HttpEntity<ProductRequest> entity = getEntity(request, getHeader());
        httpCall("/product/", HttpMethod.POST, entity, ApiResponse.class);
        HttpEntity<Object> entity2 = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/product/", HttpMethod.GET, entity, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testGetProductBId() {
        addUser();
        ProductRequest request = MockPayload.getProductRequestMockPayload();
        HttpEntity<ProductRequest> entity = getEntity(request, getHeader());
        ResponseEntity<ApiResponse> productResponse = httpCall("/product/", HttpMethod.POST, entity, ApiResponse.class);
        Map<String, Object> product = (Map<String, Object>) productResponse.getBody().getData();
        HttpEntity<Object> entity2 = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/product/" + product.get("id"), HttpMethod.GET, entity2, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
        ResponseEntity<ApiResponse> response2 = httpCall("/product/100", HttpMethod.GET, entity2, ApiResponse.class);
        assertEquals(404, response2.getStatusCodeValue());
    }

    @Test
    public void testUpdateProduct() {
        addUser();
        ProductRequest productRequest = MockPayload.getProductRequestMockPayload();
        HttpEntity<ProductRequest> entity = getEntity(productRequest, getHeader());
        ResponseEntity<ApiResponse> productResponse = httpCall("/product/", HttpMethod.POST, entity, ApiResponse.class);
        Map<String, Object> product = (Map<String, Object>) productResponse.getBody().getData();
        ProductRequest request = MockPayload.getProductRequestMockPayload();
        request.setProductName("update_product_name");
        HttpEntity<ProductRequest> entity1 = getEntity(request, getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/product/" + product.get("id"), HttpMethod.PUT, entity1, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
        HttpEntity<ProductRequest> entity2 = getEntity(request, getHeader());
        ResponseEntity<ApiResponse> response2 = httpCall("/product/100", HttpMethod.PUT, entity2, ApiResponse.class);
        assertEquals(404, response2.getStatusCodeValue());
        request.setProductName(null);
        ResponseEntity<ApiResponse> response3 = httpCall("/product/"+product.get("id"), HttpMethod.PUT, entity2, ApiResponse.class);
        assertEquals(400, response3.getStatusCodeValue());
    }

    @Test
    public void testDeleteProductBId() {
        addUser();
        ProductRequest request = MockPayload.getProductRequestMockPayload();
        HttpEntity<ProductRequest> entity = getEntity(request, getHeader());
        ResponseEntity<ApiResponse> productResponse = httpCall("/product/", HttpMethod.POST, entity, ApiResponse.class);
        Map<String, Object> product = (Map<String, Object>) productResponse.getBody().getData();
        HttpEntity<Object> entity2 = new HttpEntity<>(getHeader());
        ResponseEntity<ApiResponse> response = httpCall("/product/" + product.get("id"), HttpMethod.DELETE, entity2, ApiResponse.class);
        assertEquals(200, response.getStatusCodeValue());
        ResponseEntity<ApiResponse> response2 = httpCall("/product/100", HttpMethod.DELETE, entity2, ApiResponse.class);
        assertEquals(404, response2.getStatusCodeValue());
    }
}
  /*
        ResponseEntity<List<Rate>> rateResponse =
        restTemplate.exchange("https://bitpay.com/api/rates",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Rate>>() {
            });
        List<Rate> rates = rateResponse.getBody();
         */