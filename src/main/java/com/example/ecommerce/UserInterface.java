package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class UserInterface {

    GridPane loginPage;

    HBox headerBar;
    HBox footerBar;
    VBox body;
    Customer loggedInCustomer;
    ProductList productList = new ProductList();
    VBox productPage;
    Button signInButton;
    Button placeOrderButton = new Button("Place Order");
    Label welcomeLabel;
    ObservableList<Product> itemsInCart = FXCollections.observableArrayList();
    UserInterface() {
        createLoginPage();
        createHeaderBar();
        createFooterBar();
    }

    public BorderPane createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);
        //root.getChildren().add(loginPage);
        root.setTop(headerBar);
        //root.setCenter(loginPage);
        body = new VBox();
        body.setPadding(new Insets(10));
        body.setAlignment(Pos.CENTER);
        root.setCenter(body);
        productPage = productList.getAllProducts();
        body.getChildren().add(productPage);

        root.setBottom(footerBar);

        return root;
    }

    private void createLoginPage() {
        Text userNameText = new Text("User Name");
        Text passwordText = new Text("Password");

        TextField userName = new TextField("nitin@gmail.com");
        userName.setPromptText("Type your USER NAME here");
        PasswordField password = new PasswordField();
        password.setText("1234");
        password.setPromptText("Type your PASSWORD here");
        Button loginButton = new Button("Login");
        Label messageLabel = new Label("Hi");

        loginPage = new GridPane();

        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);
        loginPage.add(userNameText, 0, 0);
        loginPage.add(userName, 1, 0);
        loginPage.add(passwordText, 0, 1);
        loginPage.add(password, 1, 1);
        loginPage.add(loginButton, 1, 2);
        loginPage.add(messageLabel, 0, 2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = userName.getText();
                String pass = password.getText();
                Login login = new Login();
                loggedInCustomer = login.customerLogin(name, pass);
                if(loggedInCustomer != null) {
                    messageLabel.setText("Welcome " + loggedInCustomer.getName());
                    welcomeLabel.setText("Welcome :- " + loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);
                }
            }
        });
    }

    private void createHeaderBar() {
        Button homeButton = new Button();
        Image image = new Image("E:\\AccioProjects\\Ecommerce\\src\\main\\resources\\shoppy.PNG");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(80);
        homeButton.setGraphic(imageView);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search Here");
        searchBar.setPrefWidth(180);
        Button searchButton = new Button("Search");
        signInButton = new Button("Sign In");
        welcomeLabel = new Label();

        Button cartButton = new Button("Cart");

        //incomplete
        Button orderButton = new Button("Orders");

        headerBar = new HBox();
        headerBar.setStyle("-fx-background-color:grey;");
        headerBar.setPadding(new Insets(10));
        headerBar.setAlignment(Pos.CENTER);
        headerBar.setSpacing(20);
        headerBar.getChildren().addAll(homeButton, searchBar, searchButton, signInButton, cartButton, orderButton);

        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(loginPage);
                headerBar.getChildren().remove(signInButton);
            }
        });

        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                VBox prodPage = productList.getProductsInCart(itemsInCart);
                prodPage.setAlignment(Pos.CENTER);
                prodPage.setSpacing(10);
                prodPage.getChildren().add(placeOrderButton);
                body.getChildren().add(prodPage);
                footerBar.setVisible(false); // all cases needed to be handled
            }
        });

        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //need a list of product and a customer
                if(itemsInCart == null) {
                    //please select a product to place order
                    showDialog("Please add some products in cart to place order!!");
                    return;
                }
                if(loggedInCustomer == null) {
                    showDialog("Please login to place order");
                    return;
                }
                int count = Order.placeMultipleOrder(loggedInCustomer, itemsInCart);
                if(count != 0) {
                    showDialog("Order for "+count+" products placed successfully!!");
                }
                else {
                    showDialog("Order failed!!");
                }
            }
        });

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(productPage);
                footerBar.setVisible(true);
                if(loggedInCustomer == null && headerBar.getChildren().indexOf(signInButton) == -1) {
                    headerBar.getChildren().add(signInButton);
                }
            }
        });
    }

    private void createFooterBar() {
        Button buyNowButton = new Button("BuyNow");
        Button addToCartButton = new Button("Add to Cart");
        footerBar = new HBox();
        //footerBar.setStyle("-fx-background-color:grey;");
        footerBar.setPadding(new Insets(10));
        footerBar.setAlignment(Pos.CENTER);
        footerBar.setSpacing(20);
        footerBar.getChildren().addAll(addToCartButton, buyNowButton);

        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product= productList.getSelectedProduct();
                if(product == null) {
                    //please select a product to place order
                    showDialog("Please select a product to place order!!");
                    return;
                }
                if(loggedInCustomer == null) {
                    showDialog("Please login to place order");
                    return;
                }
                boolean status = Order.placeOrder(loggedInCustomer, product);
                if(status == true) {
                    showDialog("Order placed successfully!!");
                }
                else {
                    showDialog("Order failed!!");
                }
            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product= productList.getSelectedProduct();
                if(product == null) {
                    //please select a product to place order
                    showDialog("Please select a product to add it to the Cart!!");
                    return;
                }
                itemsInCart.add(product);
                showDialog("Selected items added to the Cart");
            }
        });
    }

    private void showDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle("Message");
        alert.showAndWait();
    }
}
