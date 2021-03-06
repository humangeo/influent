/**
 * Copyright (c) 2013-2014 Oculus Info Inc.
 * http://www.oculusinfo.com/
 *
 * Released under the MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package influent.bitcoin.server.spi;

import influent.bitcoin.server.dataaccess.BitcoinDataAccess;
import influent.idl.FL_DataAccess;
import influent.idl.FL_EntitySearch;
import influent.server.dataaccess.DataNamespaceHandler;
import influent.server.dataaccess.MSSQLDataNamespaceHandler;
import influent.server.utilities.SQLConnectionPool;
import oculus.aperture.spi.common.Properties;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;


/**
 *
 */
public class BitcoinFLDataAccessModule extends AbstractModule {

	private static Logger s_logger = LoggerFactory.getLogger(BitcoinFLDataAccessModule.class);
	
	@Override
	protected void configure() {
	}
	
	@Provides @Singleton
	public DataNamespaceHandler namespaceHandler(@Named("aperture.server.config") Properties config) {
		try {
			return new MSSQLDataNamespaceHandler(config);
		} catch (JSONException e) {
			s_logger.warn("Failed to load tables from json. ", e);
		}
		
		return new MSSQLDataNamespaceHandler();
	}
	
	/*
	 * Provide the database service
	 */
	@Provides @Singleton
	public FL_DataAccess connect (
		SQLConnectionPool connectionPool,
		FL_EntitySearch search,
		DataNamespaceHandler namespaceHandler,
		@Named("influent.data.view.tables") String tableNamesJson
	) {

		try {
			return new BitcoinDataAccess(
				connectionPool,
				search,
				namespaceHandler
			);
		} catch (Exception e) {
			addError("Failed to load Data Access", e);
			return null;
		}
	}
}
