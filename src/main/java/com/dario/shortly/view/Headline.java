package com.dario.shortly.view;

import com.vaadin.flow.component.html.H1;

public class Headline extends H1 { // TODO make it nice

    public Headline() {
        setText("Shortly!");
        getStyle().set("font-family", "cursive");
    }
}
