package com.company.saucedemo.stepDef.hooks;

import com.company.saucedemo.tasks.LogOut;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnlineCast;

import static net.serenitybdd.screenplay.actors.OnStage.*;

public class Hooks {

    @Before
    public void setUp() {
        setTheStage(new OnlineCast());
        theActorCalled("Lucas");
    }

    @After
    public void closeDriver() {
        theActorInTheSpotlight().attemptsTo(
                LogOut.app()
        );
    }
}