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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Sam Adams
 */
public class MediaTypeTest {

    @Test
    public void testEqualsSame() {
        MediaType mt0 = new MediaType("text/plain");
        assertTrue(mt0.equals(mt0));
    }

    @Test
    public void testEqual() {
        MediaType mt0 = new MediaType("text/plain");
        MediaType mt1 = new MediaType("text/plain");
        assertTrue(mt0.equals(mt1));
        assertTrue(mt1.equals(mt0));
    }

    @Test
    public void testNotEqual() {
        MediaType mt0 = new MediaType("text/plain");
        MediaType mt1 = new MediaType("text/html");
        assertFalse(mt0.equals(mt1));
        assertFalse(mt1.equals(mt0));
    }

    @Test
    public void testNotEqualNull() {
        MediaType mt0 = new MediaType("text/plain");
        assertFalse(mt0.equals(null));
    }

    @Test
    public void testHashcodeEqual() {
        MediaType mt0 = new MediaType("text/plain");
        MediaType mt1 = new MediaType("text/plain");
        assertEquals(mt0.hashCode(), mt1.hashCode());
    }

}
