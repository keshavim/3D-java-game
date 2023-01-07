#type VERTEX
#version 330 core
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoords;

uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uViewProj;
uniform mat4 uModel;

out vec2 fTexCoords;

void main(){
    fTexCoords = aTexCoords;
    gl_Position = uViewProj * uModel * vec4(aPosition, 1.0f);
}


#type FRAGMENT
#version 330 core

out vec4 frag_Color;

in vec2 fTexCoords;

uniform sampler2D uTexture;

void main(){
    frag_Color = texture(uTexture, fTexCoords);
}
