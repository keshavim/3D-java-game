#type VERTEX
#version 330 core
layout (location = 0) in vec3 aPosition;


void main(){
    gl_Position = vec4(aPosition, 1.0f);
}


#type FRAGMENT
#version 330 core

out vec4 frag_Color;

uniform vec4 uColor;

void main(){
    frag_Color = uColor;
}
