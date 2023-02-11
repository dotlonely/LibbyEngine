#version 400 core

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 fragTextureCoord;
out vec3 fragNormal;
out vec3 fragPos;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec4 skyColor;

//for some reason numberOfRows when set to float causes some objects to not render,
//so it is set to int for now until fix is found
//texture atlasing wont work until it is fixed
uniform int numberOfRows;
uniform vec2 offset;

const float density = 0.007f;
const float gradient = 1.5f;

void main() {
    vec4 worldPos = transformationMatrix * vec4(position, 1.0);

    vec4 positionRelativeToCamera = viewMatrix * worldPos;

    gl_Position = projectionMatrix * positionRelativeToCamera;

    fragNormal = normalize(worldPos.xyz);
    fragPos  = worldPos.xyz;

    fragTextureCoord = (textureCoord / numberOfRows) + offset;
    //fragTextureCoord = textureCoord;

    float distance = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);

}



