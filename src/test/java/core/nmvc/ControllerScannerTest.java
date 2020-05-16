package core.nmvc;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.Assert.*;

public class ControllerScannerTest {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScannerTest.class);
    private ControllerScanner controllerScanner;

    @Before
    public void init() {
        controllerScanner = new ControllerScanner("core.nmvc");
    }

    @Test
    public void getControllers() {
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.keySet().forEach(c -> logger.debug("controller: {}", c.getName()));
    }

}