package com.deadlist.core;

import com.deadlist.core.entity.Material;
import com.deadlist.core.entity.Model;
import com.deadlist.core.lighting.DirectionalLight;
import com.deadlist.core.lighting.PointLight;
import com.deadlist.core.lighting.SpotLight;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

public class ShaderManager {

    private final int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private final Map<String, Integer> uniforms;

    public ShaderManager() throws Exception{
        programID = GL20.glCreateProgram();
        if(programID == 0){
            throw new Exception("Could not create shader");
        }

        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) throws Exception{
        int uniformLocation = GL20.glGetUniformLocation(programID, uniformName);
        if(uniformLocation < 0){
            throw new Exception("Could not find uniform " + uniformName);
        }

        uniforms.put(uniformName, uniformLocation);
    }

    public void createDirectionalLightUniform(String uniformName) throws Exception{
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    public void createPointLightUniform(String uniformName) throws Exception{
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".constant");
        createUniform(uniformName + ".linear");
        createUniform(uniformName + ".exponent");
    }

    public void createSpotLightUniform(String uniformName) throws Exception{
        createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    public void createPointLightListUniform(String uniformName, int size) throws Exception{
        for (int i = 0; i < size; i++) {
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createSpotLightListUniform(String uniformName, int size) throws Exception{
        for (int i = 0; i < size; i++) {
            createSpotLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createMaterialUniform(String uniformName) throws Exception{
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    public void setUniforms(String uniformName, Matrix4f value){
        try(MemoryStack stack = MemoryStack.stackPush()){
            GL20.glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, int value){
        GL20.glUniform1i(uniforms.get(uniformName), value);
    }
    public void setUniform(String uniformName, float value){
        GL20.glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, DirectionalLight directionalLight){
        setUniform(uniformName + ".color", directionalLight.getColor());
        setUniform(uniformName + ".direction", directionalLight.getDirection());
        setUniform(uniformName + ".intensity", directionalLight.getIntensity());
    }

    public void setUniform(String uniformName, PointLight pointLight){
        setUniform(uniformName + ".color", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        setUniform(uniformName + ".constant", pointLight.getConstant());
        setUniform(uniformName + ".linear", pointLight.getLinear());
        setUniform(uniformName + ".exponent", pointLight.getExponent());
    }

    public void setUniform(String uniformName, SpotLight spotLight){
        setUniform(uniformName + ".pl", spotLight.getPointLight());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutOff());
    }

    public void setUniform(String uniformName, PointLight[] pointLights){
        int numLights = pointLights != null ? pointLights.length : 0;
        for(int i = 0; i < numLights; i++){
            setUniform(uniformName, pointLights[i], i);
        }
    }

    public void setUniform(String uniformName, PointLight pointLight, int pos){
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }

    public void setUniform(String uniformName, SpotLight[] spotLights){
        int numLights = spotLights != null ? spotLights.length : 0;
        for(int i = 0; i < numLights; i++){
            setUniform(uniformName, spotLights[i], i);
        }
    }

    public void setUniform(String uniformName, SpotLight spotLight, int pos){
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }


    public void setUniform(String uniformName, Material material){
        setUniform(uniformName + ".ambient", material.getAmbientColor());
        setUniform(uniformName + ".diffuse", material.getDiffuseColor());
        setUniform(uniformName + ".specular", material.getSpecularColor());
        setUniform(uniformName + ".hasTexture", material.hasTexture() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }
    public void setUniform(String uniformName, Vector4f value){
        GL20.glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }
    public void setUniform(String uniformName, Vector3f value){
        GL20.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, boolean value){
        float res = 0;
        if(value){
            res = 1;
        }
        GL20.glUniform1f(uniforms.get(uniformName), res);
    }
    public void createVertexShader(String shaderCode) throws Exception{
        vertexShaderID = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception{
        fragmentShaderID = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) throws Exception{
        int shaderID = GL20.glCreateShader(shaderType);
        if(shaderID == 0){
            throw new Exception("Error creating shader. TYPE: " + shaderType);
        }

        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0){
            throw new Exception("Error compiling shader code. TYPE: " + " INFO " + GL20.glGetShaderInfoLog(shaderID, 1024));
        }

        GL20.glAttachShader(programID, shaderID);

        return shaderID;
    }

    public void link() throws Exception{
        GL20.glLinkProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0){
            throw new Exception("Error linking shader code." + " INFO " + GL20.glGetProgramInfoLog(programID, 1024));
        }

        if(vertexShaderID != 0){
            GL20.glDetachShader(programID, vertexShaderID);
        }

        if(fragmentShaderID != 0){
            GL20.glDetachShader(programID, fragmentShaderID);
        }

        GL20.glValidateProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0){
            throw new Exception("Unable to validate shader code " + GL20.glGetShaderInfoLog(programID, 1024));
        }
    }

    public void bind(){
        GL20.glUseProgram(programID);
    }

    public void unbind(){
        GL20.glUseProgram(0);
    }

    public void cleanup(){
        unbind();
        if(programID != 0){
            GL20.glDeleteProgram(programID);
        }
    }
}
