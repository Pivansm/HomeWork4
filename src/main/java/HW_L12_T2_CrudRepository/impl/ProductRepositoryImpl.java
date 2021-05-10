package HW_L12_T2_CrudRepository.impl;

import HW_L12_T2_CrudRepository.ProductRepository;
import HW_L12_T2_CrudRepository.domain.Product;
import HW_L12_T2_CrudRepository.domain.ProductCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductRepositoryImpl implements ProductRepository {
    private static final String FIND_BY_ID_SQL = "select * from Product where id = ?";
    private static final String DELETE_BY_ID_SQL = "delete from Product where id = ?";
    private static final String SAVE_SQL = "insert into Product " +
            "(id, name, description, category, manufactureDateTime, manufacturer, hasExpiryTime, stock) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "update Product " +
            "set id = ?, name = ?, description = ?, category = ?," +
            "manufactureDateTime = ?, manufacturer = ?, hasExpiryTime = ?, stock = ? " +
            "where id = ?";
    private static final String FIND_BY_CATEGORY = "select * from Product where category = ?";

    private final Connection connection;

    public ProductRepositoryImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Product findById(UUID id) {
        return this.selectProductById(id);
    }

    @Override
    public void deleteById(UUID id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {

            statement.setObject(1, id);

            int result = statement.executeUpdate();
            if (result != 1) {
                System.out.println("Возникла ошибка удаления объекта");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product save(Product product) {
        Product dbProduct = selectProductById(product.getId());

        try (PreparedStatement saveOrUpdateStatement = connection.prepareStatement(
                dbProduct == null ? SAVE_SQL : UPDATE_SQL
        )) {

            if (dbProduct == null) {
                //Генерируем новый id для нового объекта (для случая, если генерация не на стороне БД)
                product.setId(UUID.randomUUID());
                saveOrUpdateStatement.setObject(9, product.getId());
            }

            saveOrUpdateStatement.setObject(1, product.getId());
            saveOrUpdateStatement.setString(2, product.getName());
            saveOrUpdateStatement.setString(3, product.getDescription());
            saveOrUpdateStatement.setString(4, product.getCategory().name());
            saveOrUpdateStatement.setTimestamp(5, Timestamp.valueOf(product.getManufactureDateTime()));
            saveOrUpdateStatement.setString(6, product.getManufacturer());
            saveOrUpdateStatement.setBoolean(7, product.getHasExpiryTime());
            saveOrUpdateStatement.setInt(8, product.getStock());

            int saveResult = saveOrUpdateStatement.executeUpdate();

            if (saveResult != 1) {
                System.out.println("Возникла ошибка удаления объекта");
            } else {
                /*
                    Возвращаем обновленный/созданный в БД объект (в JDBC persist) чтобы он был привязан к сессии БД,
                    также там могут быть различные триггеры, меняющие значения полей при обновлении/создании,
                    а значит нам нужен актуальный объект
                */
                dbProduct = selectProductById(product.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
            Для случая, если генерация на стороне БД, новый объект мы не сможем вытащить из БД без id, возвращаем переданный
            return dbProduct == null ? product : dbProduct;
         */
        return dbProduct;

    }

    @Override
    public List<Product> findAllByCategory(ProductCategory category) {
        List<Product> result = new ArrayList<>();

        if (category != null) {
            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_CATEGORY)) {

                statement.setString(1, category.name());

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    result.add(createProductFromResult(resultSet));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;

    }

    /**
     * Метод для получения объекта Product из БД
     * Возвращает null, если переданный id == null или объекта нет в БД
     *
     * @param id идентификатор объекта
     * @return объект из БД или null
     */
    private Product selectProductById(UUID id) {
        Product result = null;

        if (id != null) {
            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

                statement.setObject(1, id);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    result = createProductFromResult(resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    /**
     * Метод для формирования объекта Product из результата выполнения SQL запроса
     *
     * @param resultSet результат выполнения запроса
     * @return объект Product
     */
    private Product createProductFromResult(ResultSet resultSet) throws SQLException {

        Product product = new Product();
        product.setId(UUID.fromString(resultSet.getString("id")));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setCategory(ProductCategory.valueOf(resultSet.getString("category")));
        product.setManufactureDateTime(resultSet.getTimestamp("manufactureDateTime").toLocalDateTime());
        product.setManufacturer(resultSet.getString("manufacturer"));
        product.setHasExpiryTime(resultSet.getBoolean("hasExpiryTime"));
        product.setStock(resultSet.getInt("stock"));

        return product;
    }

}
