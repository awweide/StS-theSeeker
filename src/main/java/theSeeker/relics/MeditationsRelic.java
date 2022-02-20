package theSeeker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theSeeker.DefaultMod;
import theSeeker.actions.PonderAction;
import theSeeker.stances.PensiveStance;
import theSeeker.util.TextureLoader;

import static theSeeker.DefaultMod.makeRelicOutlinePath;
import static theSeeker.DefaultMod.makeRelicPath;

public class MeditationsRelic extends CustomRelic {

    /*
     * Starter Relic.
     * Start of each turn, enter Pensive Stance.
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("MeditationsRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    public MeditationsRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    public void atTurnStart() {
        this.flash();
        this.addToTop(new PonderAction(AbstractDungeon.player));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }


    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
