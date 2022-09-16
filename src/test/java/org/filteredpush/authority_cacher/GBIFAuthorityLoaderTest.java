package org.filteredpush.authority_cacher;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class GBIFAuthorityLoaderTest {

	private static final Log logger = LogFactory.getLog(GBIFAuthorityLoaderTest.class);

	@Test
	public void testGetValuesMapCopy() {
		AuthorityLoader loader = new GBIFAuthorityLoader("https://rs.gbif.org/vocabulary/gbif/rank.xml");
		
		Map<String,String> map = loader.getValuesMapCopy();
		Iterator<String> i = loader.getValuesMapCopy().keySet().iterator();
		logger.debug(loader.getValuesMapCopy().size());
		while(i.hasNext()) { 
			String key = i.next();
			logger.debug(key + ":" + loader.getValuesMapCopy().get(key));
		}
		
		assertEquals("subphylum", map.get("subdivision"));
		
	}

}
