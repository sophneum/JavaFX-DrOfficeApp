module yohealth {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive javafx.graphics;

    opens com.yohealth to javafx.fxml;
    exports com.yohealth;

    exports com.yohealth.controllers to javafx.fxml;
    opens com.yohealth.controllers to javafx.fxml;
}
