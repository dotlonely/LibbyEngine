package com.deadlist.core;

import com.deadlist.core.entity.Player;

public interface ILogic {

    void init() throws Exception;

    void input(WindowManager window, MouseInput mouseInput);

    void update();

    void render();

    void cleanup();

}
