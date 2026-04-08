package com.example.tickets_app.GUI.Controller.Misc;

import javafx.scene.control.TextField;

public class BaseController {

    public String Method(TextField field){
        String value = field != null ? field.getText() : "";
        return value;
    }
}
