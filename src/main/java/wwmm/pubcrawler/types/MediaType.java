/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wwmm.pubcrawler.types;

/**
 * @author Sam Adams
 */
public class MediaType {

    public static final MediaType TEXT_HTML = new MediaType("text/html");
    public static final MediaType APPLICATION_PDF = new MediaType("application/pdf");
    public static final MediaType CHEMICAL_CIF = new MediaType("chemical/x-cif");

    private String name;

    public MediaType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof MediaType) {
            MediaType other = (MediaType) o;
            return getName().equals(other.getName());
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
