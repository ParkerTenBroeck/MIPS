package org.parker.mips.gui.theme;

import com.formdev.flatlaf.json.Json;
import com.formdev.flatlaf.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Karl Tauber
 */
public class IJThemesManager
{
	public static final List<IJThemeInfo> bundledThemes = new ArrayList<>();
	public static final List<IJThemeInfo> moreThemes = new ArrayList<>();
	private static final Map<File,Long> lastModifiedMap = new HashMap<>();

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
			String name = value.get( "name" );
			boolean dark = Boolean.parseBoolean( value.get( "dark" ) );
			String license = value.get( "license" );
			String licenseFile = value.get( "licenseFile" );
			String sourceCodeUrl = value.get( "sourceCodeUrl" );
			String sourceCodePath = value.get( "sourceCodePath" );

			bundledThemes.add( new IJThemeInfo( name, resourceName, dark, license, licenseFile, sourceCodeUrl, sourceCodePath, null, null ) );
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
			moreThemes.add( new IJThemeInfo( name, null, false, null, null, null, null, f, null ) );
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