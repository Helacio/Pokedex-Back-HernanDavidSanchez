package com.pokedex.controller.handler;

import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Nested
    @DisplayName("ResourceNotFoundException")
    class ResourceNotFoundExceptionTest {

        @Test
        @DisplayName("Dado recurso y campo, cuando se construye, entonces el mensaje contiene los valores")
        void givenResourceAndField_whenConstructed_thenMessageContainsValues() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Pokemon", "id", 25L);

            assertThat(ex.getMessage()).contains("Pokemon").contains("id").contains("25");
            assertThat(ex.getErrorCode()).isEqualTo("NOT_FOUND");
        }
    }

    @Nested
    @DisplayName("DuplicateResourceException")
    class DuplicateResourceExceptionTest {

        @Test
        @DisplayName("Dado recurso duplicado, cuando se construye, entonces el mensaje contiene los valores")
        void givenDuplicateResource_whenConstructed_thenMessageContainsValues() {
            DuplicateResourceException ex = new DuplicateResourceException("Pokemon", "nationalNumber", 25);

            assertThat(ex.getMessage()).contains("Pokemon").contains("nationalNumber").contains("25");
            assertThat(ex.getErrorCode()).isEqualTo("DUPLICATE");
        }
    }

    @Nested
    @DisplayName("BusinessException")
    class BusinessExceptionTest {

        @Test
        @DisplayName("Dado mensaje y código, cuando se construye, entonces los expone correctamente")
        void givenMessageAndCode_whenConstructed_thenExposesCorrectly() {
            BusinessException ex = new BusinessException("Error de negocio", "BUSINESS_ERROR");

            assertThat(ex.getMessage()).isEqualTo("Error de negocio");
            assertThat(ex.getErrorCode()).isEqualTo("BUSINESS_ERROR");
        }

        @Test
        @DisplayName("Dado que es RuntimeException, cuando se lanza, entonces no requiere checked handling")
        void givenBusinessException_whenThrown_thenIsRuntimeException() {
            assertThat(new BusinessException("msg", "CODE"))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("GlobalExceptionHandler — métodos del handler")
    class HandlerMethods {

        private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
        private MockHttpServletRequest request;

        @BeforeEach
        void setUp() {
            request = new MockHttpServletRequest();
            request.setRequestURI("/v1/test");
        }

        @Test
        @DisplayName("handleNotFound: dado ResourceNotFoundException, retorna 404 con código NOT_FOUND y path correcto")
        void handleNotFound_returns404WithCorrectBody() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Pokemon", "id", 1L);

            ResponseEntity<ApiError> response = handler.handleNotFound(ex, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody().status()).isEqualTo(404);
            assertThat(response.getBody().errorCode()).isEqualTo("NOT_FOUND");
            assertThat(response.getBody().path()).isEqualTo("/v1/test");
            assertThat(response.getBody().fieldErrors()).isEmpty();
            assertThat(response.getBody().timestamp()).isNotNull();
        }

        @Test
        @DisplayName("handleDuplicate: dado DuplicateResourceException, retorna 409 con código DUPLICATE")
        void handleDuplicate_returns409WithCorrectBody() {
            DuplicateResourceException ex = new DuplicateResourceException("Pokemon", "nationalNumber", 25);

            ResponseEntity<ApiError> response = handler.handleDuplicate(ex, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody().status()).isEqualTo(409);
            assertThat(response.getBody().errorCode()).isEqualTo("DUPLICATE");
            assertThat(response.getBody().fieldErrors()).isEmpty();
        }

        @Test
        @DisplayName("handleValidation: dado MethodArgumentNotValidException con errores de campo, retorna 400 con fieldErrors poblados")
        void handleValidation_returns400WithFieldErrors() {
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            org.springframework.validation.FieldError fieldError =
                    new org.springframework.validation.FieldError("team", "name", "must not be blank");

            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

            ResponseEntity<ApiError> response = handler.handleValidation(ex, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody().status()).isEqualTo(400);
            assertThat(response.getBody().errorCode()).isEqualTo("VALIDATION_ERROR");
            assertThat(response.getBody().fieldErrors()).hasSize(1);
            assertThat(response.getBody().fieldErrors().get(0).field()).isEqualTo("name");
            assertThat(response.getBody().fieldErrors().get(0).message()).isEqualTo("must not be blank");
        }

        @Test
        @DisplayName("handleBusiness: dado BusinessException, retorna 422 con el código de negocio")
        void handleBusiness_returns422WithBusinessCode() {
            BusinessException ex = new BusinessException("El equipo ya está en favoritos", "DUPLICATE_FAVORITE");

            ResponseEntity<ApiError> response = handler.handleBusiness(ex, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
            assertThat(response.getBody().status()).isEqualTo(422);
            assertThat(response.getBody().errorCode()).isEqualTo("DUPLICATE_FAVORITE");
            assertThat(response.getBody().message()).isEqualTo("El equipo ya está en favoritos");
            assertThat(response.getBody().fieldErrors()).isEmpty();
        }

        @Test
        @DisplayName("handleGeneric: dado Exception inesperada, retorna 500 con código INTERNAL_ERROR")
        void handleGeneric_returns500WithInternalErrorCode() {
            Exception ex = new RuntimeException("Error inesperado");

            ResponseEntity<ApiError> response = handler.handleGeneric(ex, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody().status()).isEqualTo(500);
            assertThat(response.getBody().errorCode()).isEqualTo("INTERNAL_ERROR");
            assertThat(response.getBody().path()).isEqualTo("/v1/test");
        }
    }
}
