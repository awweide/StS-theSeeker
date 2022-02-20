package theSeeker.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theSeeker.DefaultMod;
import theSeeker.util.TextureLoader;

import static theSeeker.DefaultMod.makePowerPath;

public class StoicismPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID(StoicismPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public boolean activateAtEndOfTurn = false;

    public StoicismPower(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.activateAtEndOfTurn = false;

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if ((power.ID.equals(InspirationPower.POWER_ID)) && (power.amount >= 1)) {
            this.flash();
            this.activateAtEndOfTurn = true;
            updateDescription();
        }
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        this.flash();
        if (this.activateAtEndOfTurn) {
            this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
            this.activateAtEndOfTurn = false;
        }
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.activateAtEndOfTurn = false;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (!this.activateAtEndOfTurn) {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

}
