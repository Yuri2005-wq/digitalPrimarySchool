module com.digitalprimaryschool.digitalprimaryschool {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires org.xerial.sqlitejdbc;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires atlantafx.base;
    requires kernel;
    requires layout;
    requires java.desktop;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.digitalprimaryschool.digitalprimaryschool to javafx.fxml;
    exports com.digitalprimaryschool.digitalprimaryschool;
    exports com.digitalprimaryschool.digitalprimaryschool.dao;
    opens com.digitalprimaryschool.digitalprimaryschool.dao to javafx.fxml;

    opens com.digitalprimaryschool.digitalprimaryschool.controller to javafx.fxml;
    exports com.digitalprimaryschool.digitalprimaryschool.controller;

    opens com.digitalprimaryschool.digitalprimaryschool.service to javafx.fxml;
    exports com.digitalprimaryschool.digitalprimaryschool.service;
    opens com.digitalprimaryschool.digitalprimaryschool.model to javafx.base, javafx.fxml;

}