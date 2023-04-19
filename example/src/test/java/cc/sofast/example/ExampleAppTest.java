package cc.sofast.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class ExampleAppTest {

    private static final Logger logger = LoggerFactory.getLogger(ExampleAppTest.class);

    @org.junit.jupiter.api.Test
    void main() {
        logger.atInfo().addKeyValue("oldT", 11).addKeyValue("newT", 11).log("Temperature changed.");
    }
}