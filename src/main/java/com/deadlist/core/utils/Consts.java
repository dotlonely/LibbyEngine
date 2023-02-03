package com.deadlist.core.utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Consts {

    public static final String TITLE = "Libby Engine";

    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    public static final float MOUSE_SENS = 0.2f;
    public static final float CAMERA_STEP = 0.05f;

    public static final float SPECULAR_POWER = 10f;
    public static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Vector3f AMBIENT_LIGHT = new Vector3f(.3f, .3f, .3f);
}
