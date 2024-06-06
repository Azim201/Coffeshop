package com.example.groupproject.controller;


import com.example.groupproject.dbconfig.DatabaseConfig;
import com.example.groupproject.dto.CartDto;
import com.example.groupproject.dto.OrderDto;
import com.example.groupproject.dto.ProductDto;
import com.example.groupproject.model.Cart;
import com.example.groupproject.model.Order;
import com.example.groupproject.model.Product;

import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class OrderController {


    @GetMapping("/order")
    public String getCartPage(Model model) {
        List<OrderDto> orders = new ArrayList<>();
        List<CartDto> carts = new ArrayList<>();
        List<ProductDto> products = new ArrayList<>();

        try {
            // Get the database connection
            Connection connection = DriverManager.getConnection(DatabaseConfig.JDBC_URL, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);

            // Retrieve orders from the database
            String orderQuery = "SELECT * FROM orders";
            Statement orderStatement = connection.createStatement();
            ResultSet orderResultSet = orderStatement.executeQuery(orderQuery);

            while (orderResultSet.next()) {
                int userId = orderResultSet.getInt("user_id");
                int orderId = orderResultSet.getInt("order_id");
                int cartId = orderResultSet.getInt("cart_id");
                double orderTotal = orderResultSet.getDouble("order_total");
                int orderQuantity = orderResultSet.getInt("order_quantity");
                String orderStatus = orderResultSet.getString("order_status");

                // Retrieve cart details from the database
                String cartQuery = "SELECT * FROM cart WHERE cart_id = ?";
                PreparedStatement cartStatement = connection.prepareStatement(cartQuery);
                cartStatement.setInt(1, cartId);
                ResultSet cartResultSet = cartStatement.executeQuery();

                if (cartResultSet.next()) {
                    int userIdCart = cartResultSet.getInt("user_id");
                    int productId = cartResultSet.getInt("product_id");
                    int quantity = cartResultSet.getInt("cart_quantity");
                    double total = cartResultSet.getDouble("cart_total");

                    // Retrieve product details from the database
                    String productQuery = "SELECT * FROM product WHERE product_id = ?";
                    PreparedStatement productStatement = connection.prepareStatement(productQuery);
                    productStatement.setInt(1, productId);
                    ResultSet productResultSet = productStatement.executeQuery();

                    if (productResultSet.next()) {
                        String productName = productResultSet.getString("product_name");
                        String productDescription = productResultSet.getString("product_description");
                        double productPrice = productResultSet.getDouble("product_price");
                        String productCategory = productResultSet.getString("product_category");
                        int productQuantity = productResultSet.getInt("product_quantity");
                        String productImage = productResultSet.getString("product_image");

                        ProductDto productDto = new ProductDto(productName, productId, productDescription, productPrice, productQuantity, productCategory, productImage);
                        CartDto cartDto = new CartDto(userIdCart, cartId, productId, quantity, total, productDto);
                        OrderDto orderDto = new OrderDto(userId, orderId, cartId, orderTotal, orderQuantity, orderStatus, List.of(productDto));

                        orders.add(orderDto);
                        carts.add(cartDto);
                        products.add(productDto);
                    }

                    productStatement.close();
                }

                cartStatement.close();
            }

            orderStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        model.addAttribute("carts", carts);
        model.addAttribute("orders", orders);
        model.addAttribute("products", products);
        return "order";
    }


    @GetMapping("/adminOrder")
    public String getAdminCartPage(Model model) {
        List<OrderDto> orders = new ArrayList<>();
        List<CartDto> carts = new ArrayList<>();
        List<ProductDto> products = new ArrayList<>();

        try {
            // Get the database connection
            Connection connection = DriverManager.getConnection(DatabaseConfig.JDBC_URL, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);

            // Retrieve orders from the database
            String orderQuery = "SELECT * FROM orders";
            Statement orderStatement = connection.createStatement();
            ResultSet orderResultSet = orderStatement.executeQuery(orderQuery);

            while (orderResultSet.next()) {
                int userId = orderResultSet.getInt("user_id");
                int orderId = orderResultSet.getInt("order_id");
                int cartId = orderResultSet.getInt("cart_id");
                double orderTotal = orderResultSet.getDouble("order_total");
                int orderQuantity = orderResultSet.getInt("order_quantity");
                String orderStatus = orderResultSet.getString("order_status");

                // Retrieve cart details from the database
                String cartQuery = "SELECT * FROM cart WHERE cart_id = ?";
                PreparedStatement cartStatement = connection.prepareStatement(cartQuery);
                cartStatement.setInt(1, cartId);
                ResultSet cartResultSet = cartStatement.executeQuery();

                if (cartResultSet.next()) {
                    int userIdCart = cartResultSet.getInt("user_id");
                    int productId = cartResultSet.getInt("product_id");
                    int quantity = cartResultSet.getInt("cart_quantity");
                    double total = cartResultSet.getDouble("cart_total");

                    // Retrieve product details from the database
                    String productQuery = "SELECT * FROM product WHERE product_id = ?";
                    PreparedStatement productStatement = connection.prepareStatement(productQuery);
                    productStatement.setInt(1, productId);
                    ResultSet productResultSet = productStatement.executeQuery();

                    if (productResultSet.next()) {
                        String productName = productResultSet.getString("product_name");
                        String productDescription = productResultSet.getString("product_description");
                        double productPrice = productResultSet.getDouble("product_price");
                        String productCategory = productResultSet.getString("product_category");
                        int productQuantity = productResultSet.getInt("product_quantity");
                        String productImage = productResultSet.getString("product_image");

                        ProductDto productDto = new ProductDto(productName, productId, productDescription, productPrice, productQuantity, productCategory, productImage);
                        CartDto cartDto = new CartDto(userIdCart, cartId, productId, quantity, total, productDto);
                        OrderDto orderDto = new OrderDto(userId, orderId, cartId, orderTotal, orderQuantity, orderStatus, List.of(productDto));

                        orders.add(orderDto);
                        carts.add(cartDto);
                        products.add(productDto);
                    }

                    productStatement.close();
                }

                cartStatement.close();
            }

            orderStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        model.addAttribute("carts", carts);
        model.addAttribute("orders", orders);
        model.addAttribute("products", products);
        return "/admin/adminOrder";
    }

    @PostMapping("/add_status")
    public String updateOrderDatabase(@RequestParam("order_id") int order_id)
    {
        updateOrder(order_id);
        return "redirect:/adminOrder";
    }

    public void updateOrder(int orderId) {
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        //Database table and column names
        String tableName = "orders";
        String updateStatus = "order_status";


        String status = "completed";


        Connection connection = null;
        PreparedStatement statement = null;

        try {
            //Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            //Create a statement object


            String sql = "UPDATE " + tableName + " SET " + updateStatus + " = ? WHERE order_id = ?";


            statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, orderId);


            //Execute the UPDATE statement
            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}




