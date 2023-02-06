package com.deadlist.core.rendering;

import com.deadlist.core.Camera;
import com.deadlist.core.entity.Model;
import com.deadlist.core.lighting.DirectionalLight;
import com.deadlist.core.lighting.PointLight;
import com.deadlist.core.lighting.SpotLight;

public class EntityRenderer implements IRenderer {


    @Override
    public void init() throws Exception {

    }

    @Override
    public void renderer(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {

    }

    @Override
    public void bind(Model model) {

    }

    @Override
    public void unbind() {

    }

    @Override
    public void prepare(Object o, Camera camera) {

    }

    @Override
    public void cleanup() {

    }
}
