#type VERTEX
#version 330 core
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoords;

struct Material{
    vec4 diffuse;
};
uniform Material material;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uModel;


out vec2 fTexCoords;
out vec3 fPosition;
out vec3 fNormal;

void main(){
    fTexCoords = aTexCoords;
    fPosition = aPosition.xyz;
    //fNormal = uView * uModel * vec4(aNormal, 0,0);
    gl_Position = uProj * uView * uModel * vec4(aPosition, 1.0f) + material.diffuse;
}


#type FRAGMENT
#version 330 core

out vec4 frag_Color;

in vec2 fTexCoords;
out vec3 fPosition;
out vec3 fNormal;

uniform sampler2D uTexture;

void main(){
    frag_Color = texture(uTexture, fTexCoords);
}
