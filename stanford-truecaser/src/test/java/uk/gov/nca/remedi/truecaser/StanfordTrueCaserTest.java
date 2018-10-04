/*
National Crime Agency (c) Crown Copyright 2018

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package uk.gov.nca.remedi.truecaser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import uk.gov.nca.remedi.truecaser.StanfordTrueCaser;

public class StanfordTrueCaserTest {

  @Test
  public void test(){
    StanfordTrueCaser trueCaser = new StanfordTrueCaser();
    assertEquals("Hello James , did you go to London last Tuesday ? What was the weather like ?",
        trueCaser.trueCase("hello james, did you go to london last tuesday? what was the weather like?", "en"));

    assertEquals("hello james, did you go to london last tuesday? what was the weather like?",
        trueCaser.trueCase("hello james, did you go to london last tuesday? what was the weather like?", "Dutch"));
  }

}
