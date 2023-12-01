package test.org.example;


import main.org.example.Decoder;
import org.junit.Test;

import static org.junit.Assert.*;

public class DecoderTest {

    @Test
    public void vowelSubstitutions(){
       assertEquals( Decoder.vowelSubstitutions("t2st3ng"), "testing");
    }

    @Test
    public void consonantSubstitution(){
        assertEquals( Decoder.consonantSubstitution("vetviph"), "testing");
    }

    @Test
    public void findKindOfEncodingByWord(){
        assertTrue( Decoder.findKindOfEncodingByWord("vetviph").contains("consonant"));
    }
}
