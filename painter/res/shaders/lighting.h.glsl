
#define AMBIENT 0.3

#ifdef LIGHTING_ENABLED
	varying vec2 v_normalCoords;
	varying vec2 v_preshadedCoords;
	varying vec2 v_maskCoords;
	varying vec2 v_normalRot;
	varying vec2 v_inverted;
	varying vec2 v_lightMode;

	uniform sampler2D u_normal;
	uniform sampler2D u_preshaded;
	uniform sampler2D u_lightMask;
	#ifdef LIGHTING_COLOR_ENABLED
		uniform sampler2D u_colorLightMask;
	#endif
#endif

#ifdef LIGHTING_ENABLED
	vec3 normalDir(sampler2D normal, vec2 normalCoords)
	{
		vec4 normalFrag = texture2D(normal, normalCoords);
		vec3 normalDir = 2.0 * normalFrag.rgb - 1.0;

		float tmpX = normalDir.x * v_normalRot.x - normalDir.y * v_normalRot.y;
		normalDir.y = normalDir.x * v_normalRot.y + normalDir.y * v_normalRot.x;
		normalDir.x = tmpX;
		normalDir.xy *= v_inverted;

		return normalDir;
	}
#endif

float celshaded(float light, int levels, float margin)
{
	float floored = floor(light * levels);
	float raw = floored / (levels - 1.0);

	bool start = floored == 0.0;
	bool end = floored == levels - 1.0;
	float stepSize = 1.0 / levels;
	margin *= stepSize;
	float halfVertStepSize = 0.5 / (levels - 1);

	float local = mod(light, stepSize);

	float startOfStep = float(!start && local <= margin) * halfVertStepSize;
	float endOfStep = float(!end && local >= stepSize - margin) * halfVertStepSize;

	float bleedLeft = 1.0 - local / margin;
	float bleedRight = 1.0 - (stepSize - local) / margin;

	return raw - startOfStep * bleedLeft + endOfStep * bleedRight;
}

float flatten(float light)
{
	if(light > 0.55)
		return 1.0;

	if(light > 0.45)
		return 5.0 * (light - 0.5) + 0.75;

	return 0.5;
}

float cap(float light)
{
	if(light > 0.75)
		return 2.5;

	if(light > 0.66666)
		return 10.0 * light - 5.0;

	if(light > -0.5)
		return 1.0 + light;

	if(light > -0.6)
		return -5.0 * light - 2;

	return 1.0;
}

vec4 getLightLevelWithSun(vec2 normalCoords)
{
	#ifndef LIGHTING_ENABLED
		return vec4(1.0);
	#else
		if(v_normalCoords.y > 1.5)
			return vec4(1.0);

		vec4 lightMap = texture2D(u_lightMask, v_maskCoords);
		vec3 lightDir = vec3(2.0 * lightMap.rg - 1.0, 0.0);

		float sun = max(lightMap.b, 0.0);
		float light = lightMap.a;

		#ifdef LIGHTING_COLOR_ENABLED
			vec3 color = texture2D(u_colorLightMask, v_maskCoords).rgb;
		#else
			vec3 color = vec3(1.0);
		#endif

		if(v_normalCoords.x < 1.5)
		{
			vec3 normalDir = normalDir(u_normal, normalCoords);

			float celshaded = v_lightMode.x;
			float capped = v_lightMode.y;

			float norm = length(lightDir);
			lightDir.z = -0.5;
			lightDir = normalize(lightDir);

			float d = dot(lightDir, normalDir);

			float dsun = sign(d) * d * d;
			dsun = dsun + 1.0;

			float dnotsun = d + 1.4;

			dsun = dsun * 0.8 + dnotsun * 0.2 - 0.08 * sun;

			d = dsun * sun + dnotsun * (1.0 - sun);

			light = light * (d + 1.1 * (1.0 - norm)) * (1.0 - celshaded) * (1.0 - capped)
					+ light * flatten(d + 0.5) * celshaded
					+ light * cap(d) * capped;
		}

		return vec4(light * (1.0 - AMBIENT) * color + vec3(AMBIENT), sun); // ambient
	#endif
}

vec4 getLightLevelWithSun()
{
	#ifndef LIGHTING_ENABLED
		return vec4(1.0);
	#else
		return getLightLevelWithSun(v_normalCoords);
	#endif
}

vec3 getLightLevel(vec2 normalCoords)
{
	return getLightLevelWithSun(normalCoords).rgb;
}

vec3 getLightLevel()
{
	#ifndef LIGHTING_ENABLED
		return vec3(1.0);
	#else
		return getLightLevel(v_normalCoords);
	#endif
}

vec4 sunPreshaded(sampler2D flatTexture, vec2 texCoords, vec2 normalCoords, vec2 preshadedCoords)
{
	#ifndef LIGHTING_ENABLED
		return texture2D(flatTexture, texCoords);
	#else
		if(v_normalCoords.y > 1.5)
		{
			if(v_preshadedCoords.x > 1.5)
				return texture2D(flatTexture, texCoords);
			else
				return texture2D(u_preshaded, preshadedCoords);
		}

		vec4 lightWithSun = getLightLevelWithSun(normalCoords);

		vec4 finalLight = vec4(lightWithSun.rgb, 1.0);
		vec4 flatFrag = texture2D(flatTexture, texCoords);

		if(v_preshadedCoords.x > 1.5)
			return flatFrag * finalLight;

		vec4 preshaded = texture2D(u_preshaded, preshadedCoords);

		return lightWithSun.a * preshaded + (1.0 - lightWithSun.a) * flatFrag * finalLight;
	#endif
}

vec4 sunPreshaded(sampler2D flatTexture, vec2 texCoords)
{
	#ifndef LIGHTING_ENABLED
		return texture2D(flatTexture, texCoords);
	#else
		return sunPreshaded(flatTexture, texCoords, v_normalCoords, v_preshadedCoords);
	#endif
}