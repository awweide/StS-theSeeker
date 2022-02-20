package theSeeker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import theSeeker.DefaultMod;
import theSeeker.util.TextureLoader;

import static theSeeker.DefaultMod.makeRelicOutlinePath;
import static theSeeker.DefaultMod.makeRelicPath;

public class ApocryphaRelic extends CustomRelic {
    // Dummy relic. Functionality implemented in PensivePower

    public static final String ID = DefaultMod.makeID(ApocryphaRelic.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    public ApocryphaRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}

