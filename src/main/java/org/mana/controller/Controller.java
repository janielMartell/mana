package org.mana.controller;

import java.io.IOException;

public interface Controller {
    void showView() throws IOException;
    String getView();
}
