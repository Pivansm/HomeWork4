package HW_L12_T2_CrudRepository;

import HW_L12_T2_CrudRepository.domain.Product;
import HW_L12_T2_CrudRepository.domain.ProductCategory;
import HW_L12_T2_CrudRepository.impl.ProductRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ProductRepositoryImplTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private final LocalDateTime actualTime = LocalDateTime.now();

    private ProductRepositoryImpl productRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        this.connection = mock(Connection.class);
        this.preparedStatement = mock(PreparedStatement.class);
        this.resultSet = mock(ResultSet.class);
        System.setOut(new PrintStream(outputStreamCaptor));

        this.productRepository = new ProductRepositoryImpl(this.connection);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.getString("name")).thenReturn("some name");
        when(resultSet.getString("description")).thenReturn("some description");
        when(resultSet.getString("category")).thenReturn(ProductCategory.FOOD.name());
        when(resultSet.getTimestamp("manufactureDateTime")).thenReturn(Timestamp.valueOf(actualTime));
        when(resultSet.getString("manufacturer")).thenReturn("some manufacturer");
        when(resultSet.getBoolean("hasExpiryTime")).thenReturn(Boolean.TRUE);
        when(resultSet.getInt("stock")).thenReturn(1);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testFindById() throws SQLException {

        //setup
        UUID productId = UUID.randomUUID();

        when(resultSet.getString("id")).thenReturn(productId.toString());
        when(resultSet.next()).thenReturn(Boolean.TRUE);

        //logic
        Product result = this.productRepository.findById(productId);

        //assert
        assertEquals(productId, result.getId());
        assertEquals("some name", result.getName());
        assertEquals("some description", result.getDescription());
        assertEquals(ProductCategory.FOOD, result.getCategory());
        assertEquals(actualTime, result.getManufactureDateTime());
        assertEquals("some manufacturer", result.getManufacturer());
        assertTrue(result.getHasExpiryTime());
        assertEquals(1, result.getStock());
    }

    @Test
    public void testFindByIdNull() throws SQLException {

        //setup
        UUID productId = null;

        //logic
        Product result = this.productRepository.findById(productId);

        //assert
        assertNull(result);
    }

    @Test
    public void testFindByIdNotFound() throws SQLException {

        //setup
        UUID productId = UUID.randomUUID();

        when(resultSet.next()).thenReturn(Boolean.FALSE);

        //logic
        Product result = this.productRepository.findById(productId);

        //assert
        assertNull(result);
    }

    @Test
    public void testDeleteById() throws SQLException {

        //setup
        UUID productId = UUID.randomUUID();

        when(preparedStatement.executeUpdate()).thenReturn(1);

        //logic
        this.productRepository.deleteById(productId);
    }

    @Test
    public void testDeleteByIdError() throws SQLException {

        //setup
        UUID productId = UUID.randomUUID();

        when(preparedStatement.executeUpdate()).thenReturn(0);

        //logic
        this.productRepository.deleteById(productId);

        //assert
        assertEquals("Возникла ошибка удаления объекта", outputStreamCaptor.toString().trim());
    }

    @Test
    public void testSaveNewProduct() throws SQLException {

        //setup
        UUID productId = UUID.randomUUID();

        when(resultSet.getString("id")).thenReturn(productId.toString());
        when(resultSet.next()).thenReturn(Boolean.TRUE);

        when(preparedStatement.executeUpdate()).thenReturn(1);

        Product newObject = new Product();
        newObject.setManufactureDateTime(actualTime);
        newObject.setCategory(ProductCategory.FOOD);
        newObject.setHasExpiryTime(Boolean.TRUE);
        newObject.setStock(1);

        //logic
        Product result = this.productRepository.save(newObject);

        //assert
        assertEquals(productId, result.getId());
        assertEquals("some name", result.getName());
        assertEquals("some description", result.getDescription());
        assertEquals(ProductCategory.FOOD, result.getCategory());
        assertEquals(actualTime, result.getManufactureDateTime());
        assertEquals("some manufacturer", result.getManufacturer());
        assertTrue(result.getHasExpiryTime());
        assertEquals(1, result.getStock());
    }

    @Test
    public void testSaveUpdateProduct() throws SQLException {

        //setup
        UUID productId = UUID.randomUUID();

        when(resultSet.getString("id")).thenReturn(productId.toString());
        when(resultSet.next()).thenReturn(Boolean.TRUE);

        when(preparedStatement.executeUpdate()).thenReturn(1);

        Product newObject = new Product();
        newObject.setId(productId);
        newObject.setManufactureDateTime(actualTime);
        newObject.setCategory(ProductCategory.FOOD);
        newObject.setHasExpiryTime(Boolean.TRUE);
        newObject.setStock(1);

        //logic
        Product result = this.productRepository.save(newObject);

        //assert
        assertEquals(productId, result.getId());
        assertEquals("some name", result.getName());
        assertEquals("some description", result.getDescription());
        assertEquals(ProductCategory.FOOD, result.getCategory());
        assertEquals(actualTime, result.getManufactureDateTime());
        assertEquals("some manufacturer", result.getManufacturer());
        assertTrue(result.getHasExpiryTime());
        assertEquals(1, result.getStock());
    }

    @Test
    public void testSaveProductError() throws SQLException {

        //setup
        UUID productId = UUID.randomUUID();

        when(resultSet.getString("id")).thenReturn(productId.toString());
        when(resultSet.next()).thenReturn(Boolean.TRUE);

        when(preparedStatement.executeUpdate()).thenReturn(0);

        Product newObject = new Product();
        newObject.setId(productId);
        newObject.setManufactureDateTime(actualTime);
        newObject.setCategory(ProductCategory.FOOD);
        newObject.setHasExpiryTime(Boolean.TRUE);
        newObject.setStock(1);

        //logic
        Product result = this.productRepository.save(newObject);

        //assert
        assertEquals("Возникла ошибка удаления объекта", outputStreamCaptor.toString().trim());
    }

    @Test
    public void testFindAllProducts() throws SQLException {

        //setup
        UUID productId = UUID.randomUUID();

        when(resultSet.getString("id")).thenReturn(productId.toString());
        when(resultSet.next()).thenReturn(Boolean.TRUE).thenReturn(Boolean.FALSE);

        //logic
        List<Product> result = this.productRepository.findAllByCategory(ProductCategory.FOOD);
        Product firstResult = result.get(0);

        //assert
        assertEquals(productId, firstResult.getId());
        assertEquals("some name", firstResult.getName());
        assertEquals("some description", firstResult.getDescription());
        assertEquals(ProductCategory.FOOD, firstResult.getCategory());
        assertEquals(actualTime, firstResult.getManufactureDateTime());
        assertEquals("some manufacturer", firstResult.getManufacturer());
        assertTrue(firstResult.getHasExpiryTime());
        assertEquals(1, firstResult.getStock());
    }

    @Test
    public void testFindAllProductsEmpty() throws SQLException {

        //setup
        when(resultSet.next()).thenReturn(Boolean.FALSE);

        //logic
        List<Product> result = this.productRepository.findAllByCategory(ProductCategory.FOOD);

        //assert
        assertTrue(result.isEmpty());
    }


}
