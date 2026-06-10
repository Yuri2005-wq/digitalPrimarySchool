module com.digitalprimaryschool.digitalprimaryschool {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.digitalprimaryschool.digitalprimaryschool to javafx.fxml;
    exports com.digitalprimaryschool.digitalprimaryschool;
}