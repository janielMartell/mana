package org.mana.controller;

import java.io.IOException;

public interface ParametrizedController<T extends ParametrizedController> extends IController {

    void showView(T controller) throws IOException;
}
