#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_worldPos;

uniform sampler2D u_texture;
uniform sampler2D u_player;

uniform vec2 u_player_pos;
uniform vec2 u_player_origin;
uniform vec2 u_player_size;

#include lighting.h.glsl
#include math.h.glsl

void main()
{
    vec4 color = sunPreshaded(u_texture, v_texCoords) * v_color;

    if(color.a == 0.0)
        discard;

    float distance = u_player_pos.y - v_worldPos.y;

    //reflect it with a line just under the player
    vec2 reflectPos = vec2(v_worldPos.x, u_player_pos.y + distance);

	//convert world position of player to tex coordinates
    vec2 p_texCoords = (reflectPos - (u_player_pos + u_player_origin)) / u_player_size;

	//find player color
    vec4 playerColor = texture2D(u_player, p_texCoords);

	//adjust player color opacity from y
	gl_FragColor = vec4(color.rgb + (playerColor.rgb * sigmoid(100.0 / distance) * 0.8), color.a);

    //DEBUG
    //gl_FragColor = playerColor;
}
