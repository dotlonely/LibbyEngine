package com.deadlist.core;

import com.deadlist.core.entity.terrain.Terrain;

public interface ILogic {

    void init() throws Exception;

    void input(MouseInput mouseInput);

    void update(MouseInput mouseInput);

    void render();

    void cleanup();

}
