module ni.edu.uam.demologin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jdk.jshell;

    opens ni.edu.uam.demologin.model to javafx.base;
    opens ni.edu.uam.demologin to javafx.fxml;
    opens ni.edu.uam.demologin.controller to javafx.fxml;
    opens ni.edu.uam.demologin.service to javafx.fxml;
    exports ni.edu.uam.demologin;
    exports ni.edu.uam.demologin.controller;
    exports ni.edu.uam.demologin.service;
}