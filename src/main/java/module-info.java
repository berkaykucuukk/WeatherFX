module com.example.hava101 {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.json;


    opens com.example.hava101 to javafx.fxml;
    exports com.example.hava101;
}