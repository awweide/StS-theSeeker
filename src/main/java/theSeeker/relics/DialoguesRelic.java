package theSeeker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theSeeker.DefaultMod;
import theSeeker.cards.CleansingBrew;
import theSeeker.cards.Dialogue;
import theSeeker.powers.DecayingStrengthPower;
import theSeeker.util.TextureLoader;

import static theSeeker.DefaultMod.makeRelicOutlinePath;
import static theSeeker.DefaultMod.makeRelicPath;

public class DialoguesRelic extends CustomRelic {

    public static final String ID = DefaultMod.makeID(DialoguesRelic.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    public DialoguesRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }
    //TODO: CORRECTLY IMPLEMENT AS BOSS UPGRADE RELIC
    private static final String starterRelicID = MeditationsRelic.ID;


    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(starterRelicID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(starterRelicID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(starterRelicID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStartPostDraw() {
        this.flash();
        this.addToTop(new MakeTempCardInHandAction(new Dialogue(), 1));
    }
}
