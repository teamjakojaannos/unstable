package fi.jakojaannos.unstable.resources;

import fi.jakojaannos.unstable.renderer.TextRenderer;

import java.util.List;

public record PopUp(
        List<TextRenderer.TextOnScreen> lines
) {
}
