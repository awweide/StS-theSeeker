package theSeeker.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theSeeker.DefaultMod;
import theSeeker.util.TextureLoader;

import static theSeeker.DefaultMod.makePowerPath;

public class DecayingDexterityPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID(DecayingDexterityPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public DecayingDexterityPower(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.canGoNegative = true;

        this.type = (amount >= 0) ? PowerType.DEBUFF : PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.addToTop(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, stackAmount), stackAmount));

        if (this.amount == 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, DecayingDexterityPower.POWER_ID));
        }

        if (this.amount >= 999) {
            this.amount = 999;
        }

        if (this.amount <= -999) {
            this.amount = -999;
        }

        if (this.amount > 0) {
            this.type = PowerType.BUFF;
        } else {
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.stackPower(reduceAmount);
    }

    @Override
    public void onInitialApplication() {
        this.addToTop(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.amount), this.amount));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        if (this.amount > 0) {
            // Decay dexterity bonus
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new DecayingDexterityPower(this.owner, -1), -1));
        } else if (this.amount < 0) {
            // Decay dexterity penalty
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new DecayingDexterityPower(this.owner, 1), 1));
        }
    }


    @Override
    public void updateDescription() {
        if (this.amount > 0) {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else if (this.amount < 0) {
            description = DESCRIPTIONS[0] + (-this.amount) + DESCRIPTIONS[2];
        }
    }
}
