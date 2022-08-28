#version 330 core

layout(location = 0) in vec4 vPosition;
layout(location = 1) in vec4 vColor;
out vec4 color;

uniform mat4 ModelView;
uniform mat4 Projection;

void main()
{
	gl_Position = Projection*ModelView*vPosition;
	color = vColor;
	color.a = 0.3; // setting alpha value to less than 1 to allow for transparency of polygon object
}