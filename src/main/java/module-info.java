module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires static lombok;
    requires mysql.connector.j;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
    opens org.example.demo.classes to javafx.fxml, org.hibernate.orm.core;
    exports org.example.demo.classes;
}