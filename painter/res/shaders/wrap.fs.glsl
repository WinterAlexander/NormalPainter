#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec2 u_offset;
uniform vec4 u_uv;

#include lighting.h.glsl

vec2 wrap(vec2 coords)
{
    vec2 size = u_uv.zw - u_uv.xy;
    return (1.0 - abs(mod((coords - u_uv.xy) / size - u_offset, 2.0) - 1.0)) * size + u_uv.xy;
}

void main()
{
    vec2 coords = wrap(v_texCoords);

    #ifdef LIGHTING_ENABLED
        vec2 normalCoords = wrap(v_normalCoords);
        vec2 preshadedCoords = wrap(v_preshadedCoords);
    #else
        vec2 normalCoords = vec2(0.0);
        vec2 preshadedCoords = vec2(0.0);
    #endif
    gl_FragColor = sunPreshaded(u_texture, coords, normalCoords, preshadedCoords) * v_color;
}