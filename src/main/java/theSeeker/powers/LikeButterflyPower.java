package theSeeker.powers;

import basemod.devcommands.draw.Draw;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import theSeeker.DefaultMod;
import theSeeker.stances.ImpetuousStance;
import theSeeker.util.TextureLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import static theSeeker.DefaultMod.makePowerPath;

public class LikeButterflyPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID(LikeButterflyPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));
    private ArrayList<String> changedToStances;

    public LikeButterflyPower(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        changedToStances = new ArrayList<>();
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        changedToStances.clear();
        updateDescription();
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (!changedToStances.contains(newStance.ID)) {
            changedToStances.add(newStance.ID);
            addToBot(new DrawCardAction(amount));
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        String changedToStancesString = String.join(", ", changedToStances);
        if (amount == 1) {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
        description = description + "(Already seen: " + changedToStancesString + ")";
    }

}
