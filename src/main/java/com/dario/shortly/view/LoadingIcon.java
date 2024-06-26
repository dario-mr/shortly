package com.dario.shortly.view;

import com.vaadin.flow.component.html.Image;

public class LoadingIcon extends Image {

    public LoadingIcon() {
        setSrc("/images/loading.gif");
        setAlt("loading");

        getStyle().set("vertical-align", "middle");
        getStyle().set("height", "var(--lumo-size-xs)");
    }
}
