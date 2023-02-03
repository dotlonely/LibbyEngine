#version 400 core

in vec2 fragTextureCoord;
in vec3 fragNormal;
in vec3 fragPos;

out vec4 fragColor;


struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

struct DirectionalLight{
    vec3 color;
    vec3 direction;
    float intensity;
};

struct PointLight{
    vec3 color;
    vec3 position;
    float intensity;
    float constant;
    float linear;
    float exponent;
};

struct SpotLight{
    PointLight pl;
    vec3 conedir;
    float cutoff;
};

uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform Material material;
uniform float specularPower;
uniform DirectionalLight directionalLight;
uniform PointLight pointLight;
uniform SpotLight spotLight;

const float levels = 3;

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColors(Material material, vec2 textCoord){
    if(material.hasTexture == 1){
        ambientC = texture(textureSampler, fragTextureCoord);
        diffuseC = ambientC;
        specularC = ambientC;
    }
    else{
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }
}

vec4 calcLightColor(vec3 light_color, float light_intensity, vec3 position, vec3 to_light_direction, vec3 normal){
    vec4 diffuseColor = vec4(0,0,0,0);
    vec4 specColor = vec4(0,0,0,0);

    //diffuse light
    float diffuseFactor = max(dot(normal, to_light_direction), 0.0);

    //These two lines cel shade
    float level = floor(diffuseFactor * levels);
    diffuseFactor = level / levels;


    diffuseColor = diffuseC * vec4(light_color, 1.0) * light_intensity * diffuseFactor;

    //specular color
    vec3 camera_dir = normalize(-position);
    vec3 from_light_direction = -to_light_direction;
    vec3 reflectedLight = normalize(reflect(from_light_direction, normal));
    float specularFactor = max(dot(camera_dir, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);

    //These two lines cel shade
    level = floor(specularFactor * levels);
    specularFactor = level / levels;


    specColor = specularC * light_intensity * specularFactor * material.reflectance * vec4(light_color, 1.0);

    return (diffuseColor + specColor);

}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal){
    vec3 light_dir = light.position - position;
    vec3 to_light_dir = normalize(light_dir);
    vec4 light_color = calcLightColor(light.color, light.intensity, position, to_light_dir, normal);

    //attentuation
    float distance = length(light_dir);
    float attenuationInv = light.constant + light.linear * distance + light.exponent * distance * distance;
    return light_color / attenuationInv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal){
    vec3 light_dir = light.pl.position - position;
    vec3 to_light_dir = normalize(light_dir);
    vec3 from_light_dir = -to_light_dir;
    float spot_alpha = dot(from_light_dir, normalize(light.conedir));

    vec4 color = vec4(0,0,0,0);

    if(spot_alpha > light.cutoff){
        color = calcPointLight(light.pl, position, normal);
        color *= (1.0 - (1.0 - spot_alpha) / (1.0 - light.cutoff));
    }

    return color;
}


vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal){
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}


void main(){

    setupColors(material, fragTextureCoord);

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, fragPos, fragNormal);
    diffuseSpecularComp += calcPointLight(pointLight, fragPos, fragNormal);
    diffuseSpecularComp += calcSpotLight(spotLight, fragPos, fragNormal);

    fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}



