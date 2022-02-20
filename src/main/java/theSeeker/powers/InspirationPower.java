package theSeeker.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theSeeker.DefaultMod;
import theSeeker.util.TextureLoader;

import static theSeeker.DefaultMod.makePowerPath;

public class InspirationPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID(InspirationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final int INSPIRATION_REQUIRED_BASE = 2;
    private final int INSPIRATION_REQUIRED_INCREMENT = 1;

    private int inspirationRequiredCurrent;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public InspirationPower(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.inspirationRequiredCurrent = INSPIRATION_REQUIRED_BASE;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        // Check for Inspiration to Dominion conversion in case a large amount is gained on construction
        stackPower(0);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_MANTRA", 0.05F);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        // Find current amount of Dominion stacks to calculate required Inspiration
        int dominionPowerStacksCurrent = 0;
        AbstractPower playerDominionPower = this.owner.getPower(DominionPower.POWER_ID);
        if (playerDominionPower != null) {
            dominionPowerStacksCurrent = playerDominionPower.amount;
        }

        // Calculate required Inspiration, exchange into Dominion stack if sufficient
        // TODO: Untested and ugly
        boolean isDone = false;
        while (!isDone){
            this.inspirationRequiredCurrent = INSPIRATION_REQUIRED_BASE + dominionPowerStacksCurrent * INSPIRATION_REQUIRED_INCREMENT;
            if (this.amount >= this.inspirationRequiredCurrent) {
                this.addToTop(new ApplyPowerAction(this.owner, this.owner, new DominionPower(owner, 1), 1));
                dominionPowerStacksCurrent += 1;
                this.amount -= this.inspirationRequiredCurrent;
                if (this.amount <= 0) {
                    this.addToTop(new RemoveSpecificPowerAction(owner, owner, InspirationPower.POWER_ID));
                }
            } else {
                isDone = true;
            }
        }
    }

    // When PresciencePower, InspirationPower.amount * PresciencePower.amount works like Dexterity stacks
    public float modifyBlock(float blockAmount) {
        AbstractPower playerPresciencePower = owner.getPower(PresciencePower.POWER_ID);
        if (playerPresciencePower != null) {
            return (blockAmount += (float) this.amount * playerPresciencePower.amount) < 0.0F ? 0.0F : blockAmount;
        }
        else {
            return blockAmount;
        }
    }

}
