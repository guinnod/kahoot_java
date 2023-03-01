module com.example.frontproject3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.frontproject3 to javafx.fxml;
    exports com.example.frontproject3;
}