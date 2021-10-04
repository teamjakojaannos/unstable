package fi.jakojaannos.unstable.resources;

import fi.jakojaannos.unstable.renderer.TextRenderer;

import java.util.List;

public record PopUp(
        List<TextRenderer.TextOnScreen> lines,
        Background photo
) {
    public enum Background {
        Newspaper,
        Photo;

        public float height() {
            return switch (this) {
                case Photo -> 0.75f;
                default -> 1.0f;
            };
        }

        public boolean needsPixel() {
            return switch (this) {
                case Newspaper -> true;
                default -> false;
            };
        }
    }
}
