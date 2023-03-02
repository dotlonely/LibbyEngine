package com.deadlist.core.rendering;

import com.deadlist.core.Camera;
import com.deadlist.core.entity.Model;
import com.deadlist.core.entity.SceneManager;
import com.deadlist.core.lighting.DirectionalLight;
import com.deadlist.core.lighting.PointLight;
import com.deadlist.core.lighting.SpotLight;

public interface IRenderer <T> {

    public void init() throws Exception;

    public void renderer(Camera camera, SceneManager sceneManager);

    abstract void bind(Model model);

    public void unbind();

    public void prepare(T t, Camera camera);

    public void cleanup();
}
