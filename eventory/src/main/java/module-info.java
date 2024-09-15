module grupa8 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires kernel;
    requires layout;
 

   

    opens grupa8 to javafx.fxml, org.hibernate.orm.core;
    exports grupa8;
}
