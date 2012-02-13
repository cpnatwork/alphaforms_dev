/**************************************************************************
 * alpha-Flow
 * ==============================================
 * Copyright (C) 2009-2011 by Christoph P. Neumann
 * (http://www.chr15t0ph.de)
 **************************************************************************
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 **************************************************************************
 * $Id$
 *************************************************************************/
package alpha.forms.util;

import java.util.Collection;

/**
 * The Class SimpleListSerializer.
 */
public class SimpleListSerializer {

	/** The start token. */
	private String startToken;

	/** The end token. */
	private String endToken;

	/** The separator. */
	private String separator;

	/**
	 * Instantiates a new simple list serializer.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param separator
	 *            the separator
	 */
	public SimpleListSerializer(String start, String end, String separator) {
		this.startToken = start;
		this.endToken = end;
		this.separator = separator;
	}

	/**
	 * Instantiates a new simple list serializer.
	 * 
	 * @param separator
	 *            the separator
	 */
	public SimpleListSerializer(String separator) {
		this("[", "]", separator);
	}

	/**
	 * Instantiates a new simple list serializer.
	 */
	public SimpleListSerializer() {
		this(",");
	}

	/**
	 * Serialize.
	 * 
	 * @param list
	 *            the list
	 * @return the string
	 */
	public String serialize(Collection<?> list) {
		StringBuilder sb = new StringBuilder();
		boolean start = true;
		sb.append(startToken);
		for (Object item : list) {
			if (!start) {
				sb.append(separator);
			}
			start = false;
			if (item instanceof Collection) {
				sb.append(serialize((Collection<?>) item));
			} else {
				sb.append(item.toString());
			}
		}
		sb.append(endToken);
		return sb.toString();
	}

}
