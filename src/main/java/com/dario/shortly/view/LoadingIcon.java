package com.dario.shortly.view;

import com.dario.shortly.util.PathResolver;
import com.vaadin.flow.component.html.Image;

public class LoadingIcon extends Image {

    public LoadingIcon() {
        setSrc(PathResolver.resolve("/images/loading.gif"));
        setAlt("loading");

        getStyle().set("vertical-align", "middle");
        getStyle().set("height", "var(--lumo-size-xs)");
    }
}
