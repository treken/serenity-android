/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.kingargyle.plexapp;

import java.net.HttpURLConnection;
import java.net.URL;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.github.kingargyle.plexapp.config.IConfiguration;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;

/**
 * This class acts as a factory for retrieving items from Plex.
 * 
 * This is a singleton so only one of these will ever exist currently.
 * 
 * @author dcarver
 * 
 */
public class PlexappFactory {

	private static PlexappFactory instance = null;

	private ResourcePaths resourcePath = null;
	private Serializer serializer = null;

	protected PlexappFactory(IConfiguration config) {
		resourcePath = new ResourcePaths(config);
		serializer = new Persister();
	}

	public static PlexappFactory getInstance(IConfiguration config) {
		if (instance == null) {
			instance = new PlexappFactory(config);
		}
		return instance;
	}

	public MediaContainer retrieveRootData() throws Exception {
		MediaContainer mediaContainer = null;

		return mediaContainer;
	}

	/**
	 * This retrieves the available libraries.  This can include such
	 * things as Movies, and TV shows.
	 * 
	 * @return MediaContainer the media container for the library
	 * @throws Exception
	 */
	public MediaContainer retrieveLibrary() throws Exception {
		MediaContainer mediaContainer = null;

		String libraryURL = resourcePath.getLibraryURL();

		URL url = new URL(libraryURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		mediaContainer = serializer.read(MediaContainer.class,
				con.getInputStream(), false);

		return mediaContainer;
	}

}
