package com.dario.shortly.view;

import com.vaadin.flow.component.html.H1;

public class Headline extends H1 {

    public Headline() {
        setText("Shortly!");
        addClassName("headline");
    }
}
