/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.theme;

/**
 *
 * @author parke
 */

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

import java.io.File;

/**
 * @author Karl Tauber
 */
public class IJThemeInfo {
	public final String name;
	public final String resourceName;
	public final boolean dark;
	public final String license;
	public final String licenseFile;
	public final String sourceCodeUrl;
	public final String sourceCodePath;
	public final File themeFile;
	public final String lafClassName;

	public IJThemeInfo(String name, String resourceName, boolean dark, String license, String licenseFile,
			String sourceCodeUrl, String sourceCodePath, File themeFile, String lafClassName) {
		this.name = name;
		this.resourceName = resourceName;
		this.dark = dark;
		this.license = license;
		this.licenseFile = licenseFile;
		this.sourceCodeUrl = sourceCodeUrl;
		this.sourceCodePath = sourceCodePath;
		this.themeFile = themeFile;
		this.lafClassName = lafClassName;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof IJThemeInfo) {
			IJThemeInfo info = (IJThemeInfo)o;
			return info.name.equals(this.name);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
