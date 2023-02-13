#type VERTEX
#version 330 core
layout (location = 0) in vec3 aPosition;


uniform mat4 uProj;
uniform mat4 uView;
uniform mat4 uModel;


out vec2 fTexCoords;

void main(){
    gl_Position = uProj * uView * uModel * vec4(aPosition, 1.0f);
}


#type FRAGMENT
#version 330 core

out vec4 frag_Color;



uniform vec4 u_Color;

void main(){
    frag_Color = u_Color;
}
