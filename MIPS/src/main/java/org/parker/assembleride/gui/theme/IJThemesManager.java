/*
 * Copyright 2019 FormDev Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.parker.assembleride.gui.theme;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.json.Json;
import com.formdev.flatlaf.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

/**
 * @author Karl Tauber
 */
public class IJThemesManager
{
	private static final Map<String, IJThemeInfo> themes = new HashMap<String, IJThemeInfo>();
	public static final List<IJThemeInfo> bundledThemes = new ArrayList<>();
	public static final List<IJThemeInfo> moreThemes = new ArrayList<>();
	private static final Map<File,Long> lastModifiedMap = new HashMap<>();


	static {
		loadBundledThemes();
		loadThemesFromDirectory();

			themes.put("Flat Light", new IJThemeInfo("Flat Light", null, false, null, null, null, null, null,
					FlatLightLaf.class.getName()));

			themes.put("Flat Dark",new IJThemeInfo("Flat Dark", null, true, null, null, null, null, null,
					FlatDarkLaf.class.getName()));
			themes.put("Flat IntelliJ",new IJThemeInfo("Flat IntelliJ", null, false, null, null, null, null, null,
					FlatIntelliJLaf.class.getName()));
			themes.put("Flat Darcula",new IJThemeInfo("Flat Darcula", null, true, null, null, null, null, null,
					FlatDarculaLaf.class.getName()));
	}


	public static IJThemeInfo getTheme(String name){
		if(themes.containsKey(name)) {
			return themes.get(name);
		}else{
			return null;
		}
	}

	@SuppressWarnings( "unchecked" )
	public static void loadBundledThemes() {
		bundledThemes.clear();

		// load themes.json
		Map<String, Object> json;
		InputStream stream = IJThemesManager.class.getResourceAsStream( "/Themes/GUIThemes.json" );
	    try( Reader reader = new InputStreamReader( stream, StandardCharsets.UTF_8 ) ) {
	    		json = (Map<String, Object>) Json.parse( reader );
		} catch( Exception ex ) {
			ex.printStackTrace();
			return;
		}

		// add info about bundled themes
		for( Map.Entry<String, Object> e : json.entrySet() ) {
			String resourceName = e.getKey();
			Map<String, String> value = (Map<String, String>) e.getValue();
			String name = value.get( "name" ).replace("Material Theme UI Lite / ", "");
			boolean dark = Boolean.parseBoolean( value.get( "dark" ) );
			String license = value.get( "license" );
			String licenseFile = value.get( "licenseFile" );
			String sourceCodeUrl = value.get( "sourceCodeUrl" );
			String sourceCodePath = value.get( "sourceCodePath" );

			IJThemeInfo theme = new IJThemeInfo( name, resourceName, dark, license, licenseFile, sourceCodeUrl, sourceCodePath, null, null );
			bundledThemes.add(theme);
			themes.put(theme.name, theme);
		}
	}

	public static void loadThemesFromDirectory() {
		// get current working directory
		File directory = new File( "" ).getAbsoluteFile();

		File[] themeFiles = directory.listFiles( (dir, name) -> {
			return name.endsWith( ".theme.json" ) || name.endsWith( ".properties" );
		} );
		if( themeFiles == null )
			return;

		lastModifiedMap.clear();
		lastModifiedMap.put( directory, directory.lastModified() );

		moreThemes.clear();
		for( File f : themeFiles ) {
			String fname = f.getName();
			String name = fname.endsWith( ".properties" )
				? StringUtils.removeTrailing( fname, ".properties" )
				: StringUtils.removeTrailing( fname, ".theme.json" );
			IJThemeInfo theme = new IJThemeInfo( name, null, false, null, null, null, null, f, null );
			moreThemes.add(theme);
			themes.put(theme.name, theme);
			lastModifiedMap.put( f, f.lastModified() );
		}
	}

	public static boolean hasThemesFromDirectoryChanged() {
		for( Map.Entry<File, Long> e : lastModifiedMap.entrySet() ) {
			if( e.getKey().lastModified() != e.getValue().longValue() )
				return true;
		}
		return false;
	}
}